const UPLOAD_URL = '/documents/upload';
const STATUS_URL = '/api/documents/my-status';

const DOC_MAP = {
    'cedula': 'Cedula de Registro',
    'imss': 'Constancia de Vigencia',
    'sisae-empresa': 'Captura de Empresa',
    'sisae-alumno': 'Captura de Alumno',
    'horario': 'Copia de horario'
};

document.addEventListener('DOMContentLoaded', () => {
    fetchDocumentStatus();
    setupUIListeners();
});

function setupUIListeners() {
    Object.keys(DOC_MAP).forEach(id => {
        const input = document.getElementById(`input-${id}`);
        const nameSpan = document.getElementById(`name-${id}`);
        const deleteIcon = document.getElementById(`delete-${id}`);

        if (input && nameSpan) {
            // Actualiza la leyenda al seleccionar un archivo
            input.addEventListener('change', (e) => {
                if (e.target.files.length > 0) {
                    const fileName = e.target.files[0].name;
                    nameSpan.textContent = fileName;
                    if (deleteIcon) deleteIcon.style.display = 'block';
                } else {
                    nameSpan.textContent = 'No se ha seleccionado ningún archivo';
                    if (deleteIcon) deleteIcon.style.display = 'none';
                }
            });
        }

        // Icono de borrar archivo en local
        if (deleteIcon) {
            deleteIcon.addEventListener('click', () => {
                input.value = '';
                nameSpan.textContent = 'No se ha seleccionado ningún archivo';
                deleteIcon.style.display = 'none';
            });
        }
    });

    // Listener del botón de guardado por peticiones individuales
    const btnSave = document.getElementById('btn-save-all');
    if (btnSave) {
        btnSave.addEventListener('click', handleUploadProcess);
    }
}

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

    for (const item of filesToUpload) {
        await uploadFile(item.file, item.typeName);
    }

    alert('Carga de documentos finalizada.');
    location.reload();
}

async function uploadFile(file, typeName) {
    const formData = new FormData();

    formData.append('file', file);
    formData.append('type', typeName);

    try {
        const response = await fetch(UPLOAD_URL, {
            method: 'POST',
            body: formData
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

    if (data.status === 'ACCEPTED' && input) {
        input.disabled = true;
    }
}