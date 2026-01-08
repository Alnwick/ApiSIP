// ========== CONFIGURACIÓN ==========
const UPLOAD_URL = '/documents/upload';
const STATUS_URL = '/documents/my-status';
const VIEW_URL_PREFIX = '/view-documents/';

const DOC_MAP = {
    'cedula': 'Cedula de Registro',
    'imss': 'Constancia de Vigencia',
    'sisae-empresa': 'Captura de Empresa',
    'sisae-alumno': 'Captura de Alumno',
    'horario': 'Copia de Horario'
};

document.addEventListener('DOMContentLoaded', () => {
    fetchDocumentStatus();
    setupUIListeners();
});

async function fetchDocumentStatus() {
    try {
        const response = await fetch(STATUS_URL);
        if (!response.ok) return;
        const documents = await response.json();
        documents.forEach(doc => {
            const htmlId = Object.keys(DOC_MAP).find(key => DOC_MAP[key] === doc.typeCode);
            if (htmlId) updateSectionUI(htmlId, doc);
        });
    } catch (error) {
        console.warn("Error al cargar estados:", error);
    }
}

function updateSectionUI(id, data) {
    const section = document.getElementById(`section-${id}`);
    const nameSpan = document.getElementById(`name-${id}`);
    const commentText = document.getElementById(`comment-${id}`);
    const input = document.getElementById(`input-${id}`);

    if (!section) return;

    // 1. RESET DE CLASES (Volver al Gris)
    section.className = 'file-section';

    // 2. DETECTAR SI HAY ARCHIVO REAL
    const hasFile = data.fileName && data.fileName.trim() !== "" && data.fileName !== "null";

    if (!hasFile) {
        // --- ESTADO: SIN ARCHIVO (GRIS) ---
        nameSpan.textContent = 'No se ha seleccionado ningún archivo';
        if (commentText) commentText.textContent = "Pendiente de cargar.";
    }
    else {
        // --- ESTADO: CON ARCHIVO (NARANJA, VERDE O ROJO) ---
        let statusClass = data.status.toLowerCase();

        // Mapeo de estados del backend a clases CSS
        if (statusClass === 'revisado_correcto') statusClass = 'accepted';
        if (statusClass === 'revisado_incorrecto') statusClass = 'rejected';

        // Aplicamos clase de color
        section.classList.add(statusClass);

        // Crear link de visualización
        nameSpan.innerHTML = '';
        const link = document.createElement('a');
        link.href = `${VIEW_URL_PREFIX}${data.fileName}`;
        link.textContent = data.fileName;
        link.target = "_blank";
        link.className = "file-link-style";
        nameSpan.appendChild(link);

        // Mensaje de comentario
        if (commentText) {
            if (data.comment && data.comment.trim() !== "" && data.comment !== "Pendiente de cargar") {
                commentText.textContent = data.comment;
            } else {
                commentText.textContent = "Pendiente de Revision.";
            }
        }
    }

    // Bloqueo si ya está aprobado
    if ((data.status === 'REVISADO_CORRECTO' || data.status === 'ACCEPTED') && input) {
        input.disabled = true;
        const label = document.querySelector(`label[for="input-${id}"]`);
        if (label) { label.style.pointerEvents = 'none'; label.style.opacity = '0.5'; }
    }
}

function setupUIListeners() {
    Object.keys(DOC_MAP).forEach(id => {
        const input = document.getElementById(`input-${id}`);
        const nameSpan = document.getElementById(`name-${id}`);
        const deleteIcon = document.getElementById(`delete-${id}`);

        if (input && nameSpan) {
            input.addEventListener('change', (e) => {
                if (e.target.files.length > 0) {
                    nameSpan.textContent = e.target.files[0].name;
                    if (deleteIcon) deleteIcon.style.display = 'block';
                }
            });
        }
        if (deleteIcon) {
            deleteIcon.addEventListener('click', () => {
                input.value = '';
                nameSpan.textContent = 'No se ha seleccionado ningún archivo';
                deleteIcon.style.display = 'none';
            });
        }
    });
    const btnSave = document.getElementById('btn-save-all');
    if (btnSave) btnSave.addEventListener('click', handleUploadProcess);
}

async function handleUploadProcess() {
    const btn = document.getElementById('btn-save-all');
    let filesToUpload = [];
    Object.keys(DOC_MAP).forEach(id => {
        const input = document.getElementById(`input-${id}`);
        if (input && input.files.length > 0) {
            filesToUpload.push({ file: input.files[0], typeName: DOC_MAP[id] });
        }
    });
    if (filesToUpload.length === 0) return alert("Selecciona un archivo.");
    btn.disabled = true;
    btn.textContent = 'Subiendo...';
    for (const item of filesToUpload) {
        await uploadFile(item.file, item.typeName);
    }
    alert('Carga finalizada.');
    location.reload();
}

async function uploadFile(file, typeName) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', typeName);
    try { await fetch(UPLOAD_URL, { method: 'POST', body: formData }); } catch (error) { console.error(error); }
}