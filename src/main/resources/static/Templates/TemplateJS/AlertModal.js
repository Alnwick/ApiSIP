document.addEventListener('DOMContentLoaded', () => {
    if (!document.getElementById('global-modal')) {
        const modalHtml = `
        <div id="global-modal">
            <div class="modal-content">
                <div id="modal-icon"></div>
                <h2 id="modal-title"></h2>
                <p id="modal-msg"></p>
                <button id="modal-btn" class="modal-btn">Aceptar</button>
            </div>
        </div>`;
        document.body.insertAdjacentHTML('beforeend', modalHtml);
    }
});
function showModal(title, message, type = 'info', callback = null) {

    const modal = document.getElementById('global-modal') || document.getElementById('custom-modal');
    const iconBox = document.getElementById('modal-icon') || document.getElementById('modal-icon-box');
    const titleEl = document.getElementById('modal-title');
    const msgEl = document.getElementById('modal-msg') || document.getElementById('modal-message');
    const btn = document.getElementById('modal-btn') || document.getElementById('btn-modal-close');

    if (!modal) {
        console.error("No encontré el contenedor del modal en esta página.");
        return;
    }

    // Asignar textos
    titleEl.textContent = title;
    msgEl.textContent = message;

    // Tus nuevos iconos estandarizados
    const icons = {
        success: `
        <svg class="modal-svg icon-success" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8.5 11.5 11 14l4-4m6 2a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
        </svg>`,
        error: `
        <svg class="modal-svg icon-error" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m15 9-6 6m0-6 6 6m6-3a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
        </svg>`,
        info: `<svg class="modal-svg icon-info" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M12 13V8m0 8h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
        </svg>`
};

    // Configurar icono
    iconBox.innerHTML = icons[type] || icons.error;

    // Ajustar clases para el estilo
    iconBox.className = `modal-icon-box icon-${type}`;

    // Mostrar modal (soportando ambos métodos: display o class .active)
    modal.style.display = 'flex';
    modal.classList.add('active');

    // Manejar cierre
    btn.onclick = () => {
        modal.style.display = 'none';
        modal.classList.remove('active');
        if (callback) callback();
    };
}