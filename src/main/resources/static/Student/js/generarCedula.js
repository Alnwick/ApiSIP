const API_STATES = '/catalogs/states'; // Endpoint para estados
const API_GENERATE = '/student/generate-cedula'; // Endpoint para generar PDF

document.addEventListener('DOMContentLoaded', () => {
    loadStates();
    setupForm();
    setupLogout();
});

/**
 * Carga los estados en ambos selects
 */
async function loadStates() {
    try {
        const resp = await fetch(API_STATES);
        if (resp.ok) {
            const states = await resp.json();
            const options = states.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
            document.getElementById('studentEstado').innerHTML += options;
            document.getElementById('companyEstado').innerHTML += options;
        }
    } catch (e) { console.warn("Error cargando estados"); }
}

/**
 * Maneja el envío del formulario
 */
function setupForm() {
    const form = document.getElementById('cedulaForm');
    const viewer = document.getElementById('viewerContainer');
    const downloadBtn = document.getElementById('downloadBtn');

    form.onsubmit = async (e) => {
        e.preventDefault();
        const btn = form.querySelector('.btn-generate');
        btn.disabled = true;
        btn.textContent = "Generando...";

        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        try {
            const response = await fetch(API_GENERATE, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                const blob = await response.blob();
                const url = URL.createObjectURL(blob);

                // Actualizar Visor
                viewer.innerHTML = `<iframe src="${url}"></iframe>`;

                // Configurar Descarga
                downloadBtn.href = url;
                downloadBtn.style.display = "block";
            } else {
                alert("Error al generar la cédula. Por favor revisa los datos.");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Hubo un problema de conexión.");
        } finally {
            btn.disabled = false;
            btn.textContent = "Generar PDF de Cédula";
        }
    };
}

async function setupLogout() {
    document.getElementById('logoutBtn').addEventListener('click', async () => {
        try {
            const response = await fetch('/auth/logout', { method: 'POST' });
            if (response.ok) window.location.href = '/index.html';
        } catch (error) { console.error("Error logout:", error); }
    });
}