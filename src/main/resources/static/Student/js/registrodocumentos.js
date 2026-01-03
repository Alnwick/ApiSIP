// ========== CONFIGURACIÓN ==========
const UPLOAD_URL = '/documents/upload';
const STATUS_URL = '/api/documents/my-status';

// Mapeo exacto entre IDs del HTML y descripciones en la BD (SIP_CTIPODOC)
const DOC_MAP = {
    'cedula': 'Cedula de Registro',
    'imss': 'Constancia de Vigencia',
    'sisae-empresa': 'Captura del registro de la empresa (SISAE-SIBOLTRA)',
    'sisae-alumno': 'Captura del registro del alumno (SISAE-SIBOLTRA)',
    'horario': 'Copia de horario (SAES)'
};

document.addEventListener('DOMContentLoaded', () => {
    // 1. Intentar cargar estados previos si el endpoint existe
    fetchDocumentStatus();
    // 2. Configurar los eventos de la interfaz
    setupUIListeners();
});

// ========== 1. INTERACTIVIDAD (ACTUALIZACIÓN DE LEYENDA) ==========
function setupUIListeners() {
    Object.keys(DOC_MAP).forEach(id => {
        const input = document.getElementById(`input-${id}`);
        const nameSpan = document.getElementById(`name-${id}`);
        const deleteIcon = document.getElementById(`delete-${id}`);

        if (input && nameSpan) {
            // Este evento es el que actualiza la leyenda al seleccionar un archivo
            input.addEventListener('change', (e) => {
                if (e.target.files.length > 0) {
                    const fileName = e.target.files[0].name;
                    nameSpan.textContent = fileName; // Se muestra el nombre del PDF
                    if (deleteIcon) deleteIcon.style.display = 'block';
                } else {
                    nameSpan.textContent = 'No se ha seleccionado ningún archivo';
                    if (deleteIcon) deleteIcon.style.display = 'none';
                }
            });
        }

        // Configuración del icono de borrar (limpieza local)
        if (deleteIcon) {
            deleteIcon.addEventListener('click', () => {
                input.value = '';
                nameSpan.textContent = 'No se ha seleccionado ningún archivo';
                deleteIcon.style.display = 'none';
            });
        }
    });

    // Listener del botón de guardado masivo (peticiones individuales)
    const btnSave = document.getElementById('btn-save-all');
    if (btnSave) {
        btnSave.addEventListener('click', handleUploadProcess);
    }
}

// ========== 2. PROCESO DE CARGA INDIVIDUAL ==========
async function handleUploadProcess() {
    const btn = document.getElementById('btn-save-all');
    let filesToUpload = [];

    // Identificar qué campos tienen archivos cargados localmente
    Object.keys(DOC_MAP).forEach(id => {
        const input = document.getElementById(`input-${id}`);
        if (input && input.files.length > 0) {
            filesToUpload.push({
                file: input.files[0],
                typeName: DOC_MAP[id]
            });
        }
    });

    if (filesToUpload.length === 0) {
        alert("Por favor, selecciona al menos un archivo nuevo para subir.");
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Subiendo...';

    // Ejecutar peticiones individuales una tras otra (secuencial)
    for (const item of filesToUpload) {
        await uploadFile(item.file, item.typeName);
    }

    alert('Carga de documentos finalizada.');
    location.reload(); // Recargar para ver los cambios aplicados por el backend
}

async function uploadFile(file, typeName) {
    const formData = new FormData();
    // Coinciden con @RequestParam("file") y @RequestParam("type") de tu Controller
    formData.append('file', file);
    formData.append('type', typeName);

    try {
        const response = await fetch(UPLOAD_URL, {
            method: 'POST',
            body: formData
            // IMPORTANTE: No añadir headers manuales de Content-Type
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            throw new Error(errorMsg || `Error al subir ${typeName}`);
        }
        console.log(`Éxito: ${typeName} subido correctamente.`);
    } catch (error) {
        console.error(error);
        alert(`Error: ${error.message}`);
    }
}

// ========== 3. CARGAR ESTADO DESDE LA API ==========
async function fetchDocumentStatus() {
    try {
        const response = await fetch(STATUS_URL);
        if (!response.ok) return;

        const documents = await response.json();
        documents.forEach(doc => {
            const htmlId = Object.keys(DOC_MAP).find(key => DOC_MAP[key] === doc.typeCode);
            if (htmlId) {
                updateUIWithData(htmlId, doc);
            }
        });
    } catch (e) {
        console.warn("Endpoint de estado aún no disponible o vacío.");
    }
}

function updateUIWithData(id, data) {
    const section = document.getElementById(`section-${id}`);
    const nameSpan = document.getElementById(`name-${id}`);
    const commentText = document.getElementById(`comment-${id}`);
    const input = document.getElementById(`input-${id}`);

    if (section) section.className = `file-section ${data.status.toLowerCase()}`;
    if (nameSpan && data.fileName) nameSpan.textContent = data.fileName;
    if (commentText && data.comment) commentText.textContent = data.comment;

    // Bloquear el input si el estado ya es "CORRECTO" (ACCEPTED)
    if (data.status === 'ACCEPTED' && input) {
        input.disabled = true;
    }
}