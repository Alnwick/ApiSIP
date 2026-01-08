/**
 * Maneja la selección de Correcto/Incorrecto (mutuamente excluyentes)
 */
function toggleValidation(docId, option) {
    const correctoCheckbox = document.getElementById(`${docId}-correcto`);
    const incorrectoCheckbox = document.getElementById(`${docId}-incorrecto`);

    if (option === 'correcto') {
        if (correctoCheckbox.checked) {
            incorrectoCheckbox.checked = false;
        }
    } else {
        if (incorrectoCheckbox.checked) {
            correctoCheckbox.checked = false;
        }
    }
}

/**
 * Función para actualizar la fecha y hora en tiempo real
 */
function updateDateTime() {
    const dateTimeElement = document.getElementById('currentDateTime');
    if (!dateTimeElement) return;

    const now = new Date();
    const options = {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    };
    const dateTimeStr = now.toLocaleString('es-MX', options).replace(',', '');
    dateTimeElement.textContent = dateTimeStr;
}

// Inicializar funciones al cargar el DOM
document.addEventListener('DOMContentLoaded', () => {
    updateDateTime();
    setInterval(updateDateTime, 1000);
});