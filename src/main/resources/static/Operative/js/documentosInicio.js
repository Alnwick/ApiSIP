const urlParams = new URLSearchParams(window.location.search);
const enrollment = urlParams.get('enrollment'); // Obtenemos la boleta

// Endpoints ajustados al Controller (sin /api/ si tu controller no lo tiene)
const API_REVIEW_DATA = `/operatives/student-review?enrollment=${enrollment}`;
// Endpoints de acción
const API_SAVE_DOC = `/operatives/review-document`;
const API_FINALIZE = `/operatives/finalize-review`;
const API_APPROVE_ACTA = `/operatives/approve-acceptance-act`;

// Variable global para mantener el estado actual de los documentos cargados
let currentDocuments = [];

document.addEventListener('DOMContentLoaded', () => {
    if (!enrollment) {
        alert("No se especificó la boleta del alumno.");
        window.location.href = 'home.html';
        return;
    }
    loadStudentReview();
    setupActionButtons();
});

async function loadStudentReview() {
    try {
        console.log("Consultando:", API_REVIEW_DATA); // Debug
        const resp = await fetch(API_REVIEW_DATA);

        // Verificación de tipo de contenido para evitar el error "<"
        const contentType = resp.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
            throw new Error(`Respuesta no válida del servidor (posible 404 o HTML de error). Status: ${resp.status}`);
        }

        if (!resp.ok) {
            console.error("Error HTTP:", resp.status);
            return;
        }

        // Mapeo directo del StudentReviewDto
        const data = await resp.json();

        // 1. Llenar datos del encabezado
        document.getElementById('st-name').textContent = data.name || '--';
        document.getElementById('st-enrollment').textContent = data.enrollment || '--';
        document.getElementById('st-career').textContent = data.career || '--';
        document.getElementById('st-semester').textContent = data.semester || '--';
        document.getElementById('st-syllabus').textContent = data.syllabus || '--';

        // 2. Renderizar documentos desde la lista del DTO
        currentDocuments = data.documents || [];
        renderDocuments(currentDocuments);

    } catch (e) {
        console.error("Error cargando datos:", e);
        // Opcional: Mostrar mensaje en UI
        document.getElementById('docs-list').innerHTML = `<div style="color:red; text-align:center;">Error al cargar datos: ${e.message}</div>`;
    }
}

function renderDocuments(docs) {
    const container = document.getElementById('docs-list');

    if (!docs || docs.length === 0) {
        container.innerHTML = '<div style="padding:20px; text-align:center; color:#666;">No hay documentos requeridos para este estado del proceso.</div>';
        return;
    }

    container.innerHTML = docs.map((doc, index) => {
        // Lógica de estilos basada en el status del DocumentStatusDto
        const isRevisado = doc.status === 'REVISADO_CORRECTO';
        const isIncorrecto = doc.status === 'REVISADO_INCORRECTO';
        const isSinDoc = !doc.fileName || doc.status === 'SIN_CARGAR';

        // isCargado: tiene archivo y aún no está aprobado definitivamente (o fue rechazado)
        // NOTA: Si está 'REVISADO_INCORRECTO', se permite volver a revisar/comentar.
        const isCargado = (doc.status === 'CARGADO' || doc.status === 'EN_REVISION' || isIncorrecto) && !isSinDoc;

        let cardClass = '';
        if (isSinDoc) cardClass = 'card-sin-doc';
        else if (isRevisado) cardClass = 'card-revisado';
        else if (isIncorrecto) cardClass = 'card-cargado'; // Estilo base cargado
        else cardClass = 'card-cargado';

        let uploadDateStr = doc.uploadDate ? new Date(doc.uploadDate).toLocaleString('es-MX') : "Sin archivo cargado";

        // Identificador único para inputs
        const uniqueId = doc.typeCode.replace(/\s+/g, '_') + '_' + index;

        return `
            <div class="doc-review-card ${cardClass}" data-typecode="${doc.typeCode}">
                <div class="doc-header">
                    <div class="doc-title-box">
                        <span class="doc-name">${doc.typeCode}</span>
                        <span class="doc-date">${uploadDateStr}</span>
                    </div>
                    <div style="display:flex; gap:0.8rem; align-items:center;">
                        ${isRevisado ? '<span class="locked-badge">Revisado Correcto</span>' : ''}
                        ${isIncorrecto ? '<span class="locked-badge" style="background:var(--error);">Corrección Solicitada</span>' : ''}
                        
                        ${!isSinDoc && doc.viewUrl ?
            `<button class="btn-view" onclick="viewPdf('${doc.viewUrl}', '${doc.typeCode}')">Ver Archivo</button>` :
            '<button class="btn-view" disabled style="opacity:0.5; cursor:not-allowed;">Sin Archivo</button>'
        }
                    </div>
                </div>

                <!-- Panel de Acciones -->
                ${isCargado ? `
                <div class="status-actions">
                    <label class="action-label opt-ok">
                        <input type="radio" name="st-${uniqueId}" value="REVISADO_CORRECTO" ${doc.status === 'REVISADO_CORRECTO' ? 'checked' : ''}> 
                        Correcto
                    </label>
                    <label class="action-label opt-err">
                        <input type="radio" name="st-${uniqueId}" value="REVISADO_INCORRECTO" ${doc.status === 'REVISADO_INCORRECTO' ? 'checked' : ''}> 
                        Incorrecto
                    </label>
                </div>
                <textarea class="comment-area" id="comm-${uniqueId}" placeholder="Observaciones de revisión...">${doc.comment || ''}</textarea>
                ` : ''}
                
                ${isSinDoc ? `<div style="font-size:0.85rem; color:var(--text-muted); font-style:italic;">El alumno aún no ha cargado este documento.</div>` : ''}
            </div>
        `;
    }).join('');
}

function viewPdf(url, title) {
    if (!url) return;
    document.getElementById('pdf-title').textContent = title;
    // iframe apuntando a la URL que viene del DTO
    document.getElementById('pdfContainer').innerHTML = `<iframe src="${url}" style="width:100%; height:100%; border:none;"></iframe>`;
}

function setupActionButtons() {
    const btnFinalize = document.getElementById('btn-finalize-review');
    if (btnFinalize) {
        btnFinalize.onclick = async () => {
            if (!enrollment) return;

            const reviews = [];

            // Recopilar datos
            currentDocuments.forEach((doc, index) => {
                if (!doc.fileName) return;

                const uniqueId = doc.typeCode.replace(/\s+/g, '_') + '_' + index;
                const radio = document.querySelector(`input[name="st-${uniqueId}"]:checked`);
                const commentArea = document.getElementById(`comm-${uniqueId}`);

                // Solo enviamos si se seleccionó una opción
                if (radio) {
                    reviews.push({
                        typeCode: doc.typeCode,
                        status: radio.value,
                        comment: commentArea ? commentArea.value : ""
                    });
                }
            });

            if (reviews.length === 0) {
                alert("No has seleccionado 'Correcto' o 'Incorrecto' para ningún documento.");
                return;
            }

            btnFinalize.disabled = true;
            btnFinalize.textContent = "Guardando...";

            try {
                // Ajustar URL si es necesario
                const res = await fetch(`${API_SAVE_DOC}?enrollment=${enrollment}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(reviews)
                });

                if (res.ok) {
                    alert("Revisión guardada correctamente.");
                    loadStudentReview();
                } else {
                    alert("Hubo un error al guardar la revisión.");
                }
            } catch (e) {
                console.error(e);
                alert("Error de conexión.");
            } finally {
                btnFinalize.disabled = false;
                btnFinalize.textContent = "Finalizar Revisión General";
            }
        };
    }

    const btnApprove = document.getElementById('btn-approve-acta');
    if (btnApprove) {
        btnApprove.onclick = async () => {
            if(!confirm("¿Confirmar la aprobación del Acta de Aceptación?")) return;

            try {
                const res = await fetch(`${API_APPROVE_ACTA}?enrollment=${enrollment}`, { method: 'POST' });
                if (res.ok) alert("Acta aprobada con éxito.");
                else alert("Error al aprobar el acta.");
            } catch (e) {
                alert("Error de conexión.");
            }
        };
    }
}