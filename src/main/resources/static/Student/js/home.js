const PROCESS_STATUS_URL = '/student/process-status';

document.addEventListener('DOMContentLoaded', () => {
    loadProcessStatus();
});

async function loadProcessStatus() {
    try {
        const response = await fetch(PROCESS_STATUS_URL);
        if (!response.ok) throw new Error('Error al obtener el estatus del proceso');

        const stages = await response.json();

        const tableBody = document.querySelector('.status-table-row');
        if (tableBody) {
            tableBody.innerHTML = stages.map(s => {
                // Lógica simplificada: Si hay fecha y no es un guion, está completado
                const isDone = s.date && s.date !== "" && s.date !== "-";

                // Formateamos la fecha si existe, si no, dejamos el guion
                const displayValue = isDone ? formatDate(s.date) : "-";

                return `
                    <td class="${s.isCurrent ? 'current-stage' : ''} ${isDone ? 'completed-stage' : ''}">
                        ${displayValue}
                    </td>
                `;
            }).join('');
        }

        // Actualizar el texto descriptivo del estatus actual
        const currentStage = stages.find(s => s.isCurrent);
        const statusLabel = document.querySelector('.status-description b');

        if (currentStage && statusLabel) {
            statusLabel.textContent = currentStage.stageName;
            statusLabel.style.color = "#1A8C14";
        }

    } catch (error) {
        console.error("Error cargando el estatus del proceso:", error);
    }
}

/**
 * Formatea strings de fecha (ISO o Timestamp) a formato DD/MM/YYYY
 */
function formatDate(dateString) {
    try {
        const d = new Date(dateString);
        if (isNaN(d.getTime())) return dateString;

        return d.toLocaleDateString('es-MX', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    } catch (e) {
        return dateString;
    }
}

// ========== RESTO DE FUNCIONES (DROPDOWN Y LOGOUT) ==========

function toggleDropdown() {
    document.getElementById("userDropdown").classList.toggle("show");
}

async function cerrarSesion() {
    if (!confirm('¿Estás seguro de que deseas cerrar sesión?')) return;

    try {
        await fetch('/auth/logout', { method: 'POST' });
    } catch (error) {
        console.error('Error al cerrar sesión:', error);
    } finally {
        window.location.href = '../index.html';
    }
}

window.onclick = function(event) {
    if (!event.target.matches('.icon')) {
        const dropdowns = document.getElementsByClassName("dropdown-content");
        for (const openDropdown of dropdowns) {
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}