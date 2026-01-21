const API_URL = '/student/process-status';
const PHASES = ["Registrado", "Doc Inicial", "Carta de Aceptación", "Informes Finales", "Doc Término", "Liberación"];

document.addEventListener('DOMContentLoaded', loadData);

async function loadData() {
    let stagesData = [];
    try {
        const resp = await fetch(API_URL);
        if (resp.ok) stagesData = await resp.json();
    } catch (e) { console.warn("API offline"); }

    renderProgress(stagesData);
    updateHeaderInfo();
}

function renderProgress(apiData) {
    const stepper = document.getElementById('main-stepper');

    stepper.innerHTML = PHASES.map((name, idx) => {
        const data = apiData[idx] || {};
        const done = data.date && data.date !== "" && data.date !== "-";
        const current = data.isCurrent || false;

        let cls = done && !current ? 'completed' : (current ? 'active' : '');

        return `
                <div class="step ${cls}">
                    <div class="dot">${(done && !current) ? '✓' : idx + 1}</div>
                    <div class="step-info">
                        <span class="label">${name}</span>
                        <span class="date-badge">${done ? 'Inició: ' + fmt(data.date) : (current ? 'Fase actual' : '—')}</span>
                    </div>
                </div>
            `;
    }).join('');

    // Lógica Bloqueo
    const docIniciada = apiData[1] && apiData[1].date && apiData[1].date !== "-";
    if (docIniciada) {
        document.getElementById('card-seguimiento').classList.remove('locked');
        document.getElementById('lock-tag').style.display = 'none';
    } else {
        document.getElementById('card-seguimiento').onclick = (e) => e.preventDefault();
    }
}

function updateHeaderInfo() {
    // Simulación de datos de sesión
    document.getElementById('user-pill-name').textContent = "Santiago Martínez";
    document.getElementById('user-pill-initial').textContent = "S";
}

function fmt(d) {
    try {
        const date = new Date(d);
        if (isNaN(date.getTime())) return d;
        return date.toLocaleDateString('es-MX', { day: '2-digit', month: '2-digit', year: 'numeric' });
    } catch (e) { return d; }
}