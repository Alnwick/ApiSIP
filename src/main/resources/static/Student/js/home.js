const API_STATUS = '/students/process-status';//get
const API_LOGOUT = '/auth/logout';//post
const API_DOCS_STATUS = '/documents/my-status';//get
const PHASES = ["Registrado", "Doc Inicial", "Cartas", "Doc Término", "Liberación" ];// solo para vista

document.addEventListener('DOMContentLoaded', () => {
    loadUserProfile();
    loadData();
    setupLogout();
});

async function loadUserProfile() {
    try {
        const resp = await fetch('/students/data');
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
    let docsData = [];

    try {
        // Usamos credentials: 'include' para que Spring Security nos deje pasar
        const [respStatus, respDocs] = await Promise.all([
            fetch(API_STATUS, { credentials: 'include' }),
            fetch(API_DOCS_STATUS, { credentials: 'include' })
        ]);

        if (respStatus.ok) stagesData = await respStatus.json();
        if (respDocs.ok) docsData = await respDocs.json();

        console.log("Docs recibidos:", docsData); // Revisa esto en tu consola, amiga
    } catch (e) {
        console.warn("Error cargando datos", e);
    }

    // MANDAMOS AMBOS
    renderProgress(stagesData, docsData);
}

function renderProgress(apiData, docsData) {
    const stepper = document.getElementById('main-stepper');
    if (!stepper) return;

    const docsObligatorios = ["CEDULA_REGISTRO", "CONSTANCIA_IMSS", "CAPTURA_EMPRESA", "CAPTURA_ALUMNO", "HORARIO"];

    // 1. ¿Está TODO en CORRECTO? (Paso a la fase 3)
    const todoAprobadoReal = docsObligatorios.every(type => {
        const doc = docsData.find(d => d.typeCode === type);
        return doc && doc.status === 'CORRECTO';
    });

    // 2. ¿Falta subir algo? (Estado SIN_CARGA o no existe el doc)
    const faltaSubirArchivo = docsObligatorios.some(type => {
        const doc = docsData.find(d => d.typeCode === type);
        return !doc || doc.status === 'SIN_CARGA' || !doc.fileName;
    });

    // 3. ¿Ya subió todo pero hay cosas en PENDIENTE?
    // Si no falta subir nada, pero tampoco está todo aprobado, es que están revisando.
    const estaEnRevision = !faltaSubirArchivo && !todoAprobadoReal;

    stepper.innerHTML = PHASES.map((name, idx) => {
        const data = apiData[idx] || {};
        let done = data.date && data.date !== "" && data.date !== "-";
        let current = data.isCurrent || false;
        let displayDate = data.date;
        let customStatus = "";

        // --- LÓGICA PARA FASE 2 (Doc Inicial) ---
        if (idx === 1) {
            if (todoAprobadoReal) {
                done = true;      // Palomita verde
                current = false;
            } else {
                done = false;     // Se queda el número 2
                current = true;   // Círculo verde activo

                if (faltaSubirArchivo) {
                    customStatus = "Documentación incompleta";
                } else if (estaEnRevision) {
                    customStatus = "Revisando..."; // Este es el que querías, amiga
                }
            }
        }

        // --- FASE 3 Y 4 ---
        if ((idx === 2 || idx === 3) && todoAprobadoReal) {
            done = false;
            current = true;
        }

        let statusClass = done ? 'completed' : (current ? 'active' : '');

        return `
            <div class="step ${statusClass}">
                <div class="dot">${done ? '✓' : idx + 1}</div>
                <div class="step-info">
                    <span class="label">${name}</span>
                    <div class="date-container">
                        <span class="date-badge">
                            ${done ? 'Terminó: ' + fmt(displayDate) :
                             (current ? (customStatus || 'En progreso') : '—')}
                        </span>
                    </div>
                </div>
            </div>
        `;
    }).join('');

    actualizarTarjetas(todoAprobadoReal);
}

// Función auxiliar para no amontonar código
function actualizarTarjetas(habilitar) {
    const configuracion = [
        { id: 'card-cartas', link: 'registroCartas.html', tag: 'lock-tag-cartas' },
        { id: 'card-seguimiento', link: 'registroseguimiento.html', tag: 'lock-tag-seguimiento' }
    ];

    configuracion.forEach(item => {
        const card = document.getElementById(item.id);
        const lock = document.getElementById(item.tag);
        if (!card) return;

        if (habilitar) {
            card.classList.remove('locked');
            if (lock) lock.style.display = 'none';
            card.onclick = () => window.location.href = item.link;
            card.style.cursor = "pointer";
        } else {
            card.classList.add('locked');
            card.onclick = (e) => {
                e.preventDefault();
                showModal("Aviso", "Aún faltan documentos por aprobar.", "info");
            };
        }
    });
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