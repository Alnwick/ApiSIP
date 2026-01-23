const API_STATES = '/catalogs/states';
const API_CEDULA_DATA = '/student/cedula-data'; // GET: Obtener datos guardados
const API_GENERATE_CEDULA = '/student/generate-cedula'; // POST: Enviar 3 JSON y generar PDF
const API_VIEW_PDF = '/student/view-cedula-pdf'; // GET: Obtener stream del PDF

document.addEventListener('DOMContentLoaded', async () => {
    setupLogout();
    await loadStates();
    await loadExistingData();
    setupForm();
});

/**
 * 1. Carga los estados en los selectores
 */
async function loadStates() {
    try {
        const resp = await fetch(API_STATES);
        if (resp.ok) {
            const states = await resp.json();
            const options = states.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
            document.getElementById('studentStateId').innerHTML += options;
            document.getElementById('companyStateId').innerHTML += options;
        }
    } catch (e) { console.error("Error al cargar estados"); }
}

/**
 * 2. Intenta cargar datos existentes y previsualizar PDF
 */
async function loadExistingData() {
    try {
        const resp = await fetch(API_CEDULA_DATA);
        if (resp.ok) {
            const data = await resp.json();
            if (data) {
                fillFormData(data);
                // Si hay datos, intentamos cargar el PDF generado anteriormente
                loadPdfPreview();
            }
        }
    } catch (e) { console.warn("No hay datos previos del alumno"); }
}

/**
 * 3. Rellena los campos del formulario con el objeto compuesto
 */
function fillFormData(data) {
    const { studentAddress, companyInfo, companyAddress } = data;

    // Dirección Alumno
    if (studentAddress) {
        document.getElementById('studentStreet').value = studentAddress.street || '';
        document.getElementById('studentNumber').value = studentAddress.number || '';
        document.getElementById('studentPostalCode').value = studentAddress.postalCode || '';
        document.getElementById('studentNeighborhood').value = studentAddress.neighborhood || '';
        document.getElementById('studentStateId').value = studentAddress.stateId || '';
    }

    // Información Empresa
    if (companyInfo) {
        document.getElementById('companyName').value = companyInfo.name || '';
        document.getElementById('companyEmail').value = companyInfo.email || '';
        document.getElementById('companyPhone').value = companyInfo.phone || '';
        document.getElementById('companyExtension').value = companyInfo.extension || '';
        document.getElementById('companyFax').value = companyInfo.fax || '';
        document.getElementById('companySector').value = companyInfo.sector || 'PUBLICO';
        document.getElementById('bossGrade').value = companyInfo.bossGrade || 'Lic.';
        document.getElementById('bossName').value = companyInfo.bossName || '';
        document.getElementById('bossJob').value = companyInfo.bossJob || '';
        document.getElementById('studentJob').value = companyInfo.studentJob || '';
    }

    // Dirección Empresa
    if (companyAddress) {
        document.getElementById('companyStreet').value = companyAddress.street || '';
        document.getElementById('companyNumber').value = companyAddress.number || '';
        document.getElementById('companyPostalCode').value = companyAddress.postalCode || '';
        document.getElementById('companyNeighborhood').value = companyAddress.neighborhood || '';
        document.getElementById('companyStateId').value = companyAddress.stateId || '';
    }
}

/**
 * 4. Maneja el envío del formulario (Generar Cédula)
 */
function setupForm() {
    const form = document.getElementById('cedulaForm');
    const btn = document.getElementById('submitBtn');

    form.onsubmit = async (e) => {
        e.preventDefault();
        btn.disabled = true;
        btn.textContent = "Generando...";

        // Construir los 3 objetos JSON
        const payload = {
            studentAddress: {
                street: document.getElementById('studentStreet').value,
                number: document.getElementById('studentNumber').value,
                postalCode: document.getElementById('studentPostalCode').value,
                neighborhood: document.getElementById('studentNeighborhood').value,
                stateId: document.getElementById('studentStateId').value
            },
            companyInfo: {
                name: document.getElementById('companyName').value,
                email: document.getElementById('companyEmail').value,
                phone: document.getElementById('companyPhone').value,
                extension: document.getElementById('companyExtension').value,
                fax: document.getElementById('companyFax').value,
                sector: document.getElementById('companySector').value,
                bossGrade: document.getElementById('bossGrade').value,
                bossName: document.getElementById('bossName').value,
                bossJob: document.getElementById('bossJob').value,
                studentJob: document.getElementById('studentJob').value
            },
            companyAddress: {
                street: document.getElementById('companyStreet').value,
                number: document.getElementById('companyNumber').value,
                postalCode: document.getElementById('companyPostalCode').value,
                neighborhood: document.getElementById('companyNeighborhood').value,
                stateId: document.getElementById('companyStateId').value
            }
        };

        try {
            const response = await fetch(API_GENERATE_CEDULA, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                // Recargar el PDF en el visor
                await loadPdfPreview();
                alert("Cédula generada y guardada correctamente.");
            } else {
                alert("Error al procesar los datos. Revisa la información.");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Problema de conexión con el servidor.");
        } finally {
            btn.disabled = false;
            btn.textContent = "Generar PDF de Cédula";
        }
    };
}

/**
 * 5. Carga el stream del PDF en el Visor
 */
async function loadPdfPreview() {
    const viewer = document.getElementById('viewerContainer');
    const downloadBtn = document.getElementById('downloadBtn');

    try {
        const response = await fetch(API_VIEW_PDF);
        if (response.ok) {
            const blob = await response.blob();
            const url = URL.createObjectURL(blob);

            viewer.innerHTML = `<iframe src="${url}"></iframe>`;
            downloadBtn.href = url;
            downloadBtn.style.display = "block";
        }
    } catch (e) { console.warn("Aún no se ha generado el PDF"); }
}

async function setupLogout() {
    document.getElementById('logoutBtn').addEventListener('click', async () => {
        try {
            const response = await fetch('/auth/logout', { method: 'POST' });
            if (response.ok) window.location.href = '/index.html';
        } catch (error) { console.error("Error logout:", error); }
    });
}