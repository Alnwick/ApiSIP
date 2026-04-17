const API_OPERADOR_UPLOAD = '/documents/uploadLetter'; 
const DOC_PATH = '/view-documents/'; 

const DOC_CONFIG = [
    { id: 'CP', label: 'Carta de Presentación', typeCode: 'CARTA_PRESENTACION' },
    
];
const MAPA_DOCS_INICIALES = {
    'CARTA_ACEPTACION': 'Carta de Aceptación',
};
document.addEventListener('DOMContentLoaded', () => {
    renderUniversalHeader('users');
    volverAtras();
    initOperadorCartas();
     InicializarSeccionRevision({
        statusSeccion: 'CARTAS',
        mapaNombres: MAPA_DOCS_INICIALES,
        endpointPost: '/documents/review',
        proximaEtapa: { 
            idBoton: 'irTermino', 
            functionVerificar: verificarAccesoACartas 
        }
    });
    renderUniversalFooter();
    const btnGuardar = document.getElementById('btn-global-save');
    if (btnGuardar) {
        btnGuardar.addEventListener('click', handleGlobalUpload);
    }
});

function verificarAccesoACartas(documentos, idBotonContenedor) {
    const btnContenedor = document.getElementById(idBotonContenedor);
    if (!btnContenedor) return;

    // Buscamos específicamente el documento de tipo CARTA_ACEPTACION
    const cartaAceptacion = documentos.find(doc => doc.typeCode === 'CARTA_ACEPTACION');

    // La condición ahora es: que exista la carta y que su status sea CORRECTO
    const accesoHabilitado = cartaAceptacion && cartaAceptacion.status === 'CORRECTO';

    if (accesoHabilitado) {
        btnContenedor.classList.add("visible");
        console.log("Acceso habilitado: La CARTA_ACEPTACION está CORRECTA.");
        
        const btnReal = btnContenedor.querySelector('button');
        if (btnReal) {
            btnReal.onclick = () => {
                // Asegúrate de que la variable 'enrollment' esté disponible en este scope
                window.location.href = `documentosTermino.html?enrollment=${enrollment}`;
            };
        }
    } else {
        btnContenedor.classList.remove("visible");
        console.log("Acceso denegado: CARTA_ACEPTACION aún no está CORRECTA.");
    }
}


const CONFIG_CARTAS = [
    { id: 'CP', label: 'Carta de Presentación', typeCode: 'CARTA_PRESENTACION' }
];

async function initOperadorCartas() {
    const urlParams = new URLSearchParams(window.location.search);
    const enrollment = urlParams.get('enrollment');
    
    if (!enrollment) return;

    const container = document.getElementById('docs-container');
    
    // 1. Consultamos el estado actual para ver si ya se subió antes
    const data = await obtenerEstadoAlumno(enrollment);
    const cartaData = data.documents.find(d => d.typeCode === 'CARTA_PRESENTACION') || {};

    // 2. Renderizamos usando TU diseño
    container.innerHTML = CONFIG_CARTAS.map(doc => {
        // Para el operador, 'mostrarObservaciones' será false para que se estire el diseño
        return crearTarjetaDocumento(doc, cartaData, "", false);
    }).join('');

    // 3. Activar el evento de selección de archivo
    setupUploadListener(enrollment);
}

function setupUploadListener(enrollment) {
    const input = document.getElementById('file-CP');
    const label = document.getElementById('name-CP'); 

    if (input) {
        input.addEventListener('change', (e) => { // Quitamos el async
            if (e.target.files.length > 0) {
                const file = e.target.files[0];
                // Solo damos feedback visual, NO subimos aún
                if(label) label.textContent = `Archivo seleccionado: ${file.name}`;
            }
        });
    }
}
async function subirCartaPresentacion(enrollment, file) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('enrollment', enrollment); // Este es el que el Back usará para buscar al alumno
    
    // IMPORTANTE: No mandes 'type' si el Back no lo espera en ese método
    // formData.append('type', 'CARTA_PRESENTACION'); 

    try {
        const response = await fetch('/documents/uploadLetter', {
            method: 'POST',
            body: formData 
            // NO envíes headers de Content-Type manualmente, deja que el navegador ponga el boundary del FormData
        });

        if (response.ok) {
            showModal('¡Éxito!', 'Archivo vinculado a la boleta ' + enrollment, 'success');
        } else {
            // Si da 500 aquí, es porque el Back a fuerza quiere el userId del Alumno 
            // y no el del Operador.
        }
    } catch (error) {
        console.error("Error:", error);
    }
}
async function obtenerEstadoAlumno(enrollment) {
    try {
        // Ajusta esta URL según tu backend, es la que devuelve el JSON que me mostraste
        const response = await fetch(`/students/toReview?enrollment=${enrollment}&processStatus=CARTAS`);
        if (!response.ok) throw new Error("Error al obtener datos del alumno");
        return await response.json();
    } catch (error) {
        console.error("Error en obtenerEstadoAlumno:", error);
        return { documents: [] }; // Devolvemos estructura vacía para que no truene el .find()
    }
}
async function handleGlobalUpload() {
    const urlParams = new URLSearchParams(window.location.search);
    const enrollment = urlParams.get('enrollment'); 

    if (!enrollment) {
        showModal('Error', 'No se detectó la boleta del alumno.', 'error');
        return;
    }

    const btn = document.getElementById('btn-global-save');
    const inputsConArchivos = DOC_CONFIG.map(config => ({
        config,
        input: document.getElementById(`file-${config.id}`)
    })).filter(item => item.input && item.input.files.length > 0);

    if (inputsConArchivos.length === 0) {
        showModal('Sin cambios', 'No has seleccionado archivos nuevos.', 'warning');
        return;
    }

    btn.disabled = true;
    btn.textContent = "Subiendo documentos...";

    for (const item of inputsConArchivos) {
        const formData = new FormData();
        // MANDAR SOLO LO QUE PIDE EL CONTROLLER: file y enrollment
        formData.append('file', item.input.files[0]);
        formData.append('enrollment', enrollment); 

        try {
            const response = await fetch(API_OPERADOR_UPLOAD, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                showModal('¡Éxito!', `Carta asignada correctamente a la boleta ${enrollment}`, 'success', () => location.reload());
            } else {
                const errorMsg = await response.text();
                console.error("Error en el servidor:", errorMsg);
                showModal('Error', 'El servidor no pudo procesar el archivo.', 'error');
            }
        } catch (e) {
            console.error("Error de conexión:", e);
        } finally {
            btn.disabled = false;
            btn.textContent = "Finalizar Revisión";
        }
    }
}