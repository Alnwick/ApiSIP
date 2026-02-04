const urlParams = new URLSearchParams(window.location.search);
const enrollment = urlParams.get('enrollment');

const API_REVIEW_DATA = `/api/operatives/student-review/${enrollment}`;
const API_SAVE_DOC = `/api/operatives/review-document/${enrollment}`;
const API_FINALIZE = `/api/operatives/finalize-review/${enrollment}`;
const API_APPROVE_ACTA = `/api/operatives/approve-acceptance-act/${enrollment}`;
const API_VIEW_PDF = `/view-documents/`;

const DOC_TYPES = [
    { key: 'cedula', label: 'Cédula de Registro', typeCode: 'Cedula de Registro' },
    { key: 'imss', label: 'Constancia de Vigencia (IMSS)', typeCode: 'Constancia de Vigencia' },
    { key: 'sisae-empresa', label: 'Captura Empresa (SISAE)', typeCode: 'Captura de Empresa' },
    { key: 'sisae-alumno', label: 'Captura Alumno (SISAE)', typeCode: 'Captura de Alumno' },
    { key: 'horario', label: 'Copia de Horario (SAES)', typeCode: 'Copia de Horario' }
];

let initialDocsData = [];

document.addEventListener('DOMContentLoaded', () => {
    // Inicializar UI con estados vacíos antes del fetch
    renderDocuments([]);

    if (!enrollment) {
        console.error("Sin boleta");
        return;
    }
    loadStudentReview();
    setupActionButtons();
});

async function loadStudentReview() {
    try {
        const resp = await fetch(API_REVIEW_DATA);
        if (!resp.ok) return;
        const data = await resp.json();

        document.getElementById('st-name').textContent = data.student?.name || '--';
        document.getElementById('st-enrollment').textContent = data.student?.enrollment || '--';
        document.getElementById('st-career').textContent = data.student?.career || '--';
        document.getElementById('st-semester').textContent = data.student?.semester || '--';
        document.getElementById('st-syllabus').textContent = data.student?.syllabus || '--';

        initialDocsData = data.documents || [];
        renderDocuments(initialDocsData);
    } catch (e) { console.error("Error cargando datos:", e); }
}

function renderDocuments(apiDocs) {
    const container = document.getElementById('docs-list');
    // Usamos DOC_TYPES como base para asegurar que siempre aparezcan los 5
    container.innerHTML = DOC_TYPES.map(type => {
        const doc = apiDocs.find(d => d.typeCode === type.typeCode) || { status: 'SIN_CARGAR' };

        const isRevisado = doc.status === 'REVISADO_CORRECTO';
        const isSinDoc = doc.status === 'SIN_CARGAR' || !doc.status;
        const isCargado = doc.status === 'CARGADO' || doc.status === 'REVISADO_INCORRECTO';

        let cardClass = isSinDoc ? 'card-sin-doc' : (isRevisado ? 'card-revisado' : 'card-cargado');
        let uploadDateStr = doc.uploadDate ? new Date(doc.uploadDate).toLocaleString('es-MX') : "Sin archivo cargado";

        return `
                <div class="doc-review-card ${cardClass}" data-typecode="${type.typeCode}">
                    <div class="doc-header">
                        <div class="doc-title-box">
                            <span class="doc-name">${type.label}</span>
                            <span class="doc-date">${uploadDateStr}</span>
                        </div>
                        <div style="display:flex; gap:0.8rem; align-items:center;">
                            ${isRevisado ? '<span class="locked-badge">Revisado</span>' : ''}
                            ${!isSinDoc ? `<button class="btn-view" onclick="viewPdf('${doc.fileName}', '${type.label}')">Ver Archivo</button>` : ''}
                        </div>
                    </div>

                    ${isCargado ? `
                    <div class="status-actions">
                        <label class="action-label opt-ok"><input type="radio" name="st-${type.key}" value="REVISADO_CORRECTO" ${doc.status === 'REVISADO_CORRECTO' ? 'checked' : ''}> Correcto</label>
                        <label class="action-label opt-err"><input type="radio" name="st-${type.key}" value="REVISADO_INCORRECTO" ${doc.status === 'REVISADO_INCORRECTO' ? 'checked' : ''}> Incorrecto</label>
                    </div>
                    <textarea class="comment-area" id="comm-${type.key}" placeholder="Observaciones de revisión...">${doc.comment || ''}</textarea>
                    ` : ''}

                    ${isRevisado ? `
                    <div class="comment-area" style="background:#f0fdf4; color:#166534; font-size:0.85rem; padding:1rem; border-color:#bbf7d0">
                        <strong>Dictamen finalizado:</strong><br>${doc.comment || 'Sin observaciones registradas.'}
                    </div>
                    ` : ''}
                </div>
            `;
    }).join('');
}

function viewPdf(fileName, title) {
    if(!fileName || fileName === 'undefined') return;
    document.getElementById('pdf-title').textContent = title;
    document.getElementById('pdfContainer').innerHTML = `<iframe src="${API_VIEW_PDF}${fileName}"></iframe>`;
}

function setupActionButtons() {
    document.getElementById('btn-finalize-review').onclick = async () => {
        const btn = document.getElementById('btn-finalize-review');
        btn.disabled = true;
        btn.textContent = "Procesando...";

        try {
            // Revisar los 5 tipos definidos
            for (const type of DOC_TYPES) {
                const radio = document.querySelector(`input[name="st-${type.key}"]:checked`);
                const commentArea = document.getElementById(`comm-${type.key}`);

                if (radio && commentArea) {
                    const newStatus = radio.value;
                    const newComment = commentArea.value;
                    const original = initialDocsData.find(d => d.typeCode === type.typeCode) || {};

                    // Solo enviar actualización si cambió el radio o el texto
                    if (newStatus !== original.status || newComment !== original.comment) {
                        await fetch(API_SAVE_DOC, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({
                                typeCode: type.typeCode,
                                status: newStatus,
                                comment: newComment
                            })
                        });
                    }
                }
            }

            // Disparar acción global de finalización
            const res = await fetch(API_FINALIZE, { method: 'POST' });
            if (res.ok) {
                alert("Se ha guardado la revisión y actualizado el trámite.");
                window.location.href = 'home.html';
            }
        } catch (e) {
            alert("Error de conexión al guardar la revisión.");
        } finally {
            btn.disabled = false;
            btn.textContent = "Finalizar Revisión General";
        }
    };

    document.getElementById('btn-approve-acta').onclick = async () => {
        if(!confirm("¿Aprobar el Acta de Aceptación para este alumno?")) return;
        const res = await fetch(API_APPROVE_ACTA, { method: 'POST' });
        if (res.ok) alert("Acta aprobada.");
    };
}