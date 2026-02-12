const API_STATUS = '/student/process-status';
const API_LOGOUT = '/auth/logout';
const PHASES = ["Registrado", "Doc Inicial", "Carta de Aceptación", "Finalización de informes", "Doc Término", "Liberación"];

document.addEventListener('DOMContentLoaded', () => {
    loadUserProfile();
    loadData();
    setupLogout();
});

async function loadUserProfile() {
    try {
        const resp = await fetch('/student/my-name');
        if (resp.ok) {
            const data = await resp.json();
            const firstName = data.name.split(' ')[0];
            const lastName = data.fLastName.split(' ')[0];

            const nameEl = document.getElementById('user-pill-name');
            const initialEl = document.getElementById('user-pill-initial');

            if(nameEl) nameEl.textContent = `${firstName} ${lastName}`;
            if(initialEl) initialEl.textContent = firstName.charAt(0).toUpperCase();
        }
    } catch (error) {
        console.error("Error al cargar perfil:", error);
    }
}

async function loadData() {
    let stagesData = [];
    try {
        const resp = await fetch(API_STATUS);
        if (resp.ok) stagesData = await resp.json();
    } catch (e) { console.warn("Modo Offline"); }

    renderProgress(stagesData);
}

function renderProgress(apiData) {
    const stepper = document.getElementById('main-stepper');

    stepper.innerHTML = PHASES.map((name, idx) => {
        const data = apiData[idx] || {};
        const done = data.date && data.date !== "" && data.date !== "-";
        const current = data.isCurrent || false;
        let statusClass = done && !current ? 'completed' : (current ? 'active' : '');

        return `
                    <div class="step ${statusClass}">
                        <div class="dot">${(done && !current) ? '✓' : idx + 1}</div>
                        <div class="step-info">
                            <span class="label">${name}</span>
                            <div class="date-container">
                                <span class="date-badge">${done ? 'Inició: ' + fmt(data.date) : (current ? 'En progreso' : '—')}</span>
                            </div>
                        </div>
                    </div>
                `;
    }).join('');

    const docIniciado = apiData[1] && apiData[1].date && apiData[1].date !== "-";
    if (docIniciado) {
        document.getElementById('card-seguimiento').classList.remove('locked');
        const tag = document.getElementById('lock-tag');
        if (tag) tag.style.display = 'none';
    } else {
        document.getElementById('card-seguimiento').onclick = (e) => e.preventDefault();
    }
}

function setupLogout() {
    document.getElementById('logoutBtn').addEventListener('click', async () => {
        try {
            const response = await fetch(API_LOGOUT, { method: 'POST' });
            if (response.ok) {
                window.location.href = '/index.html';
            }
        } catch (error) {
            console.error("Error al cerrar sesión:", error);
        }
    });
}

function fmt(d) {
    try {
        const date = new Date(d);
        if (isNaN(date.getTime())) return d;
        return date.toLocaleDateString('es-MX', { day: '2-digit', month: '2-digit', year: 'numeric' });
    } catch (e) { return d; }
}