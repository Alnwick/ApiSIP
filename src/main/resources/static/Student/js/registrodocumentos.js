const API_GET_STATUS = '/documents/my-status';
const API_POST_UPLOAD = '/documents/upload';
const DOC_PATH = '/view-documents/';

// Mapeo exacto según tu catálogo de backend
const DOC_CONFIG = [
    { id: 'cedula', label: 'Cédula de Registro', typeCode: 'Cedula de Registro' },
    { id: 'imss', label: 'Constancia de Vigencia (IMSS)', typeCode: 'Constancia de Vigencia' },
    { id: 'sisae-empresa', label: 'Captura Empresa (SISAE)', typeCode: 'Captura de Empresa' },
    { id: 'sisae-alumno', label: 'Captura Alumno (SISAE)', typeCode: 'Captura de Alumno' },
    { id: 'horario', label: 'Copia de Horario (SAES)', typeCode: 'Copia de Horario' }
];

document.addEventListener('DOMContentLoaded', () => {
    initUI();
    loadStatus();
});

/**
 * Renderiza los esqueletos de las tarjetas (Estado Gris Inicial)
 */
function initUI() {
    const container = document.getElementById('docs-container');
    container.innerHTML = DOC_CONFIG.map(doc => `
            <div class="doc-card status-none" id="card-${doc.id}">
                <div class="doc-header">
                    <span class="doc-title">${doc.label}</span>
                    <span class="status-badge badge-none" id="badge-${doc.id}">Sin Cargar</span>
                </div>
                <div class="doc-body">
                    <div class="upload-area">
                        <div class="file-controls">
                            <input type="file" id="file-${doc.id}" style="display:none" accept=".pdf">
                            <label for="file-${doc.id}" class="btn-browse" id="btn-${doc.id}">Seleccionar PDF</label>
                            <span class="file-display" id="name-${doc.id}">No se ha seleccionado archivo</span>
                        </div>
                    </div>
                    <div class="comment-area">
                        <span class="comment-label">Observaciones</span>
                        <p class="comment-text" id="comment-${doc.id}">Pendiente de carga inicial.</p>
                    </div>
                </div>
            </div>
        `).join('');

    // Listeners para cambios de archivo local
    DOC_CONFIG.forEach(doc => {
        document.getElementById(`file-${doc.id}`).addEventListener('change', (e) => {
            if (e.target.files.length > 0) {
                document.getElementById(`name-${doc.id}`).textContent = e.target.files[0].name;
            }
        });
    });

    document.getElementById('btn-global-save').addEventListener('click', handleGlobalUpload);
}

/**
 * Carga el estado real desde el API
 */
async function loadStatus() {
    try {
        const resp = await fetch(API_GET_STATUS);
        if (!resp.ok) return;
        const data = await resp.json();

        data.forEach(item => {
            const config = DOC_CONFIG.find(c => c.typeCode === item.typeCode);
            if (config) updateCard(config.id, item);
        });
    } catch (e) { console.error("Error cargando estatus:", e); }
}

/**
 * Actualiza visualmente una tarjeta según el estado del servidor
 */
function updateCard(id, data) {
    const card = document.getElementById(`card-${id}`);
    const badge = document.getElementById(`badge-${id}`);
    const comment = document.getElementById(`comment-${id}`);
    const display = document.getElementById(`name-${id}`);
    const input = document.getElementById(`file-${id}`);
    const labelBtn = document.getElementById(`btn-${id}`);

    card.className = "doc-card"; // Reset
    let statusCls = "status-none", badgeCls = "badge-none", label = "Sin Cargar";

    // Mapeo de estados del backend
    if (data.status === "REVISADO_CORRECTO") {
        statusCls = "status-correct"; badgeCls = "badge-correct"; label = "Aceptado";
        input.disabled = true;
        labelBtn.style.opacity = "0.5";
        labelBtn.style.pointerEvents = "none";
    } else if (data.status === "REVISADO_INCORRECTO") {
        statusCls = "status-incorrect"; badgeCls = "badge-incorrect"; label = "Rechazado";
    } else if (data.status === "EN_REVISION") {
        statusCls = "status-pending"; badgeCls = "badge-pending"; label = "En Revisión";
    } else if (data.fileName) {
        statusCls = "status-pending"; badgeCls = "badge-pending"; label = "Pendiente";
    }

    card.classList.add(statusCls);
    badge.textContent = label;
    badge.className = `status-badge ${badgeCls}`;
    comment.textContent = data.comment || "Sin observaciones.";

    if (data.fileName) {
        display.innerHTML = `<a href="${DOC_PATH}${data.fileName}" target="_blank" class="file-link">Ver archivo actual: ${data.fileName}</a>`;
    }
}

/**
 * Procesa la subida de múltiples archivos
 */
async function handleGlobalUpload() {
    const btn = document.getElementById('btn-global-save');
    let filesSent = 0;

    btn.disabled = true;
    btn.textContent = "Subiendo archivos...";

    for (const config of DOC_CONFIG) {
        const input = document.getElementById(`file-${config.id}`);
        if (input.files.length > 0) {
            const formData = new FormData();
            formData.append('file', input.files[0]);
            formData.append('type', config.typeCode);
            try {
                await fetch(API_POST_UPLOAD, { method: 'POST', body: formData });
                filesSent++;
            } catch (e) { console.error("Error subiendo " + config.label); }
        }
    }

    if (filesSent > 0) {
        alert("Documentos enviados correctamente.");
        location.reload();
    } else {
        alert("No has seleccionado nuevos archivos para subir.");
        btn.disabled = false;
        btn.textContent = "Guardar Cambios y Enviar";
    }
}