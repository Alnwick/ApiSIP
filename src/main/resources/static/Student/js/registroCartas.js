const API_GET_STATUS = '/documents/my-status'; //get
const API_DOWNLOAD_LETTER = '/documents/downloadLetter'; // El nuevo endpoint POST
const DOC_PATH = '/view-documents/'; //post


document.addEventListener('DOMContentLoaded', async () => {
    // 1. Renderizamos los componentes visuales comunes
    renderUniversalHeader('students');
    volverAtras(); 
    tituloFijo(
        "Cartas de Presentación y Aceptación",
        "Por favor, descarga tu carta de presentación y carga tu carta de aceptación en formato PDF."
    );
    renderUniversalFooter();

    // 2. ¡Llamamos a tu función de carga de datos!
    await cargarCartasAlumno();
});



// Esta función es la que alimentará a cargarCartasAlumno
async function obtenerEstadoAlumno() {
    try {
        const response = await fetch(API_DOWNLOAD_LETTER, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                // A veces es necesario enviar un objeto vacío si el POST no lleva parámetros
            },
            body: JSON.stringify({}) 
        });

        if (response.status === 404) {
            // Si sigue saliendo 404, intenta cambiar la ruta a '/ApiSIP/documents/downloadLetter' 
            // o como se llame tu proyecto en el server
            console.error("Ruta no encontrada (404)");
            return null;
        }

        if (!response.ok) return null;

        return await response.json(); 
    } catch (error) {
        console.error("Error al obtener la carta del operador:", error);
        return null;
    }
}

// 2. LA FUNCIÓN QUE TE MARCABA ERROR POR NO ESTAR DEFINIDA
function setupUploadListenerAlumno(id, typeCode) {
    const input = document.getElementById(`file-${id}`);
    const label = document.getElementById(`name-${id}`);

    if (input) {
        input.addEventListener('change', async (e) => {
            if (e.target.files.length > 0) {
                const file = e.target.files[0];
                if (label) label.textContent = "Seleccionado: " + file.name;
                
                // Aquí llamarías a tu función de subir (upload)
                console.log("Subiendo archivo para:", typeCode);
                await manejarSubidaAlumno(file, typeCode);
            }
        });
    }
}

// 3. Función para manejar la subida (Para la Carta de Aceptación)
async function manejarSubidaAlumno(file, typeCode) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', typeCode);

    try {
        const response = await fetch('/documents/upload', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            alert("Archivo subido correctamente");
            location.reload();
        } else {
            alert("Error al subir el archivo");
        }
    } catch (error) {
        console.error("Error en la subida:", error);
    }
}

async function cargarCartasAlumno() {
    const container = document.getElementById('docs-container');
    if (!container) return;

    // 1. Obtenemos la Carta de Presentación (la que manda el operador)
    const cartaPresentacion = await obtenerEstadoAlumno(); 

    container.innerHTML = ""; 

    // 2. Si existe la carta, la mostramos con tu diseño de éxito
    if (cartaPresentacion) {
        const tarjetaCP = TarjetaDocumentoAlumno(cartaPresentacion, { 
            index: 'CP',
            docPath: DOC_PATH 
        });
        container.appendChild(tarjetaCP);
    }

    // 3. Ahora cargamos la Carta de Aceptación (que el alumno debe subir)
    // Para esta usamos el otro endpoint que ya tenías: /my-status?processStatus=CARTAS
    await cargarCartaAceptacionPersonalizada();
}

async function cargarCartaAceptacionPersonalizada() {
    const container = document.getElementById('docs-container');
    
    // Llamamos al GET /my-status que devuelve la LISTA de documentos del alumno
    const resp = await fetch('/documents/my-status?processStatus=CARTAS');
    const docs = await resp.json();

    const dataCA = docs.find(d => d.typeCode === 'CARTA_ACEPTACION') || {};

    const htmlCA = crearTarjetaDocumento(
        { id: 'CA', label: 'Carta de Aceptación', typeCode: 'CARTA_ACEPTACION' }, 
        dataCA, 
        "", 
        true
    );
    
    container.insertAdjacentHTML('beforeend', htmlCA);
    setupUploadListenerAlumno('CA', 'CARTA_ACEPTACION');
}