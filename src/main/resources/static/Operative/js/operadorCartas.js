const urlParams = new URLSearchParams(window.location.search);
const enrollment = urlParams.get('enrollment');
document.addEventListener('DOMContentLoaded', async () => {
    // 1. Validaciones iniciales
    if (!enrollment) {
        alert("No se especificó la boleta del alumno.");
        window.location.href = 'home.html';
        return;
    }

    renderUniversalHeader('operative');

    const data = await SeccionInfoEstudiante(enrollment, 'CARTAS'); 
    if (data && data.documents) {
        renderCartas(data); 
    } else {
        console.error("No se recibieron documentos del API");
    }
    renderUniversalFooter();
});

function renderCartas(data) {
    const container = document.getElementById('docs-list');
    if (!container || !data.documents) return;

    container.innerHTML = ''; 
    const fragment = document.createDocumentFragment();

    // Ahora iteramos sobre lo que realmente viene del API
    data.documents.forEach((doc, index) => {
        const opciones = {
            index: index,
            // Si el status es 'SIN_CARGA', habilitamos el modo subida
            isUploadMode: doc.status === 'SIN_CARGA', 
            labelPersonalizado: doc.typeCode.replace(/_/g, ' '), // Quita guiones bajos
            onUpload: (event) => manejarSubidaArchivo(event, doc.typeCode)
        };

        const tarjeta = TarjetaOperador(doc, opciones);
        fragment.appendChild(tarjeta);
    });

    container.appendChild(fragment);
}

function manejarSubidaArchivo(e) {
    const archivo = e.target.files[0];
    const visor = document.getElementById('visor-pdf-operador');
    const botonPublicar = document.getElementById('btn-subir-carta');
    const labelNombre = document.querySelector('.file-name-display'); // O el ID que uses en tu componente

    if (archivo && archivo.type === 'application/pdf') {
        // Validar tamaño 1MB
        if (archivo.size > 1024 * 1024) {
            showModal('Error', 'El archivo supera 1MB', 'error');
            e.target.value = ''; // Limpiamos el input
            return;
        }

        if (labelNombre) labelNombre.textContent = archivo.name;
        if (botonPublicar) botonPublicar.disabled = false;

        // Mostrar vista previa
        mostrarVistaPrevia(archivo, visor);
    } else {
        showModal('Error', 'Por favor selecciona un archivo PDF válido', 'warning');
    }
}