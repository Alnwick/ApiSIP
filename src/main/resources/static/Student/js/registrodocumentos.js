// ========== CONFIGURACIÓN ==========
const UPLOAD_URL = '/documents/upload';
const STATUS_URL = '/documents/my-status';

const DOC_MAP = {
    'cedula': 'Cedula de Registro',
    'imss': 'Constancia de Vigencia',
    'sisae-empresa': 'Captura de Empresa',
    'sisae-alumno': 'Captura de Alumno',
    'horario': 'Copia de Horario'
};

document.addEventListener('DOMContentLoaded', () => {
    // 1. Cargar el estado actual de los documentos del alumno
    fetchDocumentStatus();
    // 2. Configurar los eventos para la selección de archivos y botones
    setupUIListeners();
});

// ========== 1. CARGAR ESTADOS DESDE EL SERVIDOR (my-status) ==========
async function fetchDocumentStatus() {
    try {
        const response = await fetch(STATUS_URL);
        if (!response.ok) return;

        const documents = await response.json();

        // El backend devuelve una lista de objetos con el estado de cada documento
        documents.forEach(doc => {
            // Buscamos a qué ID de nuestro HTML corresponde según el typeCode (descripción)
            const htmlId = Object.keys(DOC_MAP).find(key => DOC_MAP[key] === doc.typeCode);
            if (htmlId) {
                updateSectionUI(htmlId, doc);
            }
        });
    } catch (error) {
        console.warn("No se pudieron cargar los estados iniciales:", error);
    }
}

/**
 * Actualiza visualmente cada sección (tarjeta) de documento según la respuesta del servidor.
 */
function updateSectionUI(id, data) {
    const section = document.getElementById(`section-${id}`);
    const nameSpan = document.getElementById(`name-${id}`);
    const commentText = document.getElementById(`comment-${id}`);
    const dateSpan = document.getElementById(`date-${id}`);
    const input = document.getElementById(`input-${id}`);

    if (!section) return;

    // Actualizar color de la tarjeta: 'accepted', 'rejected' o 'pending'
    const statusClass = data.status.toLowerCase();
    section.className = `file-section ${statusClass}`;

    // Mostrar el nombre del archivo registrado en BD
    if (data.fileName && nameSpan) {
        nameSpan.textContent = data.fileName;
    }

    // Mostrar comentarios de revisión
    if (data.comment && commentText) {
        commentText.textContent = data.comment;
    }

    // Mostrar fecha de revisión si el elemento existe
    if (data.reviewDate && dateSpan) {
        dateSpan.textContent = `Revisado el: ${data.reviewDate}`;
    }

    // SI EL ESTADO ES "ACCEPTED" (Aprobado), bloqueamos el campo para evitar cambios
    if (data.status === 'ACCEPTED' && input) {
        input.disabled = true;
        const label = document.querySelector(`label[for="input-${id}"]`);
        if (label) {
            label.style.pointerEvents = 'none';
            label.style.opacity = '0.5';
        }
    }
}

// ========== 2. INTERACTIVIDAD DE LA INTERFAZ LOCAL ==========
function setupUIListeners() {
    Object.keys(DOC_MAP).forEach(id => {
        const input = document.getElementById(`input-${id}`);
        const nameSpan = document.getElementById(`name-${id}`);
        const deleteIcon = document.getElementById(`delete-${id}`);

        if (input && nameSpan) {
            // EVENTO CHANGE: Muestra el nombre del archivo apenas lo seleccionas
            input.addEventListener('change', (e) => {
                if (e.target.files.length > 0) {
                    nameSpan.textContent = e.target.files[0].name;
                    if (deleteIcon) deleteIcon.style.display = 'block';
                }
            });
        }

        // Evento para limpiar la selección actual (antes de subir)
        if (deleteIcon) {
            deleteIcon.addEventListener('click', () => {
                input.value = '';
                nameSpan.textContent = 'No se ha seleccionado ningún archivo';
                deleteIcon.style.display = 'none';
            });
        }
    });

    // Botón principal de guardado
    const btnSave = document.getElementById('btn-save-all');
    if (btnSave) {
        btnSave.addEventListener('click', handleUploadProcess);
    }
}

// ========== 3. PROCESO DE CARGA (PETICIONES INDIVIDUALES) ==========
async function handleUploadProcess() {
    const btn = document.getElementById('btn-save-all');
    let filesToUpload = [];

    // Recolectar archivos seleccionados en los campos habilitados
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
    btn.textContent = 'Subiendo archivos...';

    // Se realizan peticiones POST individuales por cada archivo
    for (const item of filesToUpload) {
        await uploadFile(item.file, item.typeName);
    }

    alert('Carga de documentos finalizada.');
    location.reload(); // Recargar para reflejar los nuevos nombres y estados
}

async function uploadFile(file, typeName) {
    const formData = new FormData();
    // 'file' y 'type' coinciden con los @RequestParam en DocumentController.java
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
        console.log(`Documento "${typeName}" subido con éxito.`);
    } catch (error) {
        console.error(error);
        alert(`Error: ${error.message}`);
    }
}