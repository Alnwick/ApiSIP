document.addEventListener('DOMContentLoaded', () => {
    loadUserProfile();
    setupLogout();
    setupPasswordUpdate();
});

// Cargar datos desde la API
async function loadUserProfile() {
    try {
        const resp = await fetch('/student/profile');
        if (resp.ok) {
            const data = await resp.json();

            // Actualizar Tarjeta de Identidad
            document.getElementById('display-initial').textContent = data.name.charAt(0).toUpperCase();
            document.getElementById('info-full-name').textContent = `${data.name} ${data.fLastName} ${data.mLastName}`;
            document.getElementById('info-boleta').textContent = data.enrollment;

            // Actualizar Información de Contacto
            document.getElementById('info-email').textContent = data.email;
            document.getElementById('info-phone').textContent = data.phone;

            // Actualizar Información Institucional
            document.getElementById('info-career').textContent = data.career;
            document.getElementById('info-plan').textContent = data.syllabus;
            document.getElementById('info-semester').textContent = data.semester;
            document.getElementById('info-practice-status').textContent = data.processStatus;
        } else {
            console.error("No se pudo cargar el perfil. Estado HTTP:", resp.status);
        }
    } catch (error) {
        console.error("Error al cargar perfil:", error);
    }
}

// Funcionalidad de Cerrar Sesión
function setupLogout() {
    const btnLogout = document.getElementById('logoutBtn');
    if (!btnLogout) return;

    btnLogout.addEventListener('click', async () => {
        try {
            const response = await fetch('/auth/logout', { method: 'POST' });
            if (response.ok) {
                window.location.href = '/index.html';
            }
        } catch (error) {
            console.error("Error al intentar cerrar sesión:", error);
        }
    });
}

// Funcionalidad de Actualizar Contraseña
function setupPasswordUpdate() {
    const btnUpdate = document.getElementById('btn-update-pwd');
    const inputNew = document.getElementById('new-password');
    const inputConfirm = document.getElementById('confirm-password');

    btnUpdate.addEventListener('click', async () => {
        const pwd = inputNew.value;
        const confirm = inputConfirm.value;

        // Validaciones locales
        if (!pwd || !confirm) {
            showModal('Campos Vacíos', 'Por favor, llena ambos campos de contraseña.', 'error');
            return;
        }
        if (pwd.length < 6) {
            showModal('Contraseña Corta', 'La contraseña debe tener al menos 6 caracteres.', 'error');
            return;
        }
        if (pwd !== confirm) {
            showModal('Contraseñas no coinciden', 'La nueva contraseña y la confirmación deben ser iguales.', 'error');
            return;
        }

        // Iniciar Petición a la API
        btnUpdate.disabled = true;
        btnUpdate.textContent = "Actualizando...";

        try {
            // NOTA: Asegúrate de tener implementado el endpoint /student/change-password en tu StudentController
            const resp = await fetch('/student/change-password', {
                method: 'POST', // o PUT
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ newPassword: pwd })
            });

            if (resp.ok) {
                showModal('¡Actualización Exitosa!', 'Tu contraseña ha sido cambiada correctamente.', 'success', () => {
                    inputNew.value = '';
                    inputConfirm.value = '';
                });
            } else {
                const errorTxt = await resp.text();
                showModal('Error al actualizar', errorTxt || 'No se pudo cambiar la contraseña. Inténtalo más tarde.', 'error');
            }
        } catch (error) {
            showModal('Error de Conexión', 'No se pudo contactar con el servidor. Revisa tu conexión a internet.', 'error');
        } finally {
            btnUpdate.disabled = false;
            btnUpdate.textContent = "Actualizar Contraseña";
        }
    });
}

// Controlador del Modal
function showModal(title, message, type, callback) {
    const modal = document.getElementById('custom-modal');
    const iconBox = document.getElementById('modal-icon-box');
    const titleEl = document.getElementById('modal-title');
    const msgEl = document.getElementById('modal-message');
    const btn = document.getElementById('btn-modal-close');

    titleEl.textContent = title;
    msgEl.textContent = message;

    // Configurar icono y color según tipo
    if (type === 'success') {
        iconBox.className = 'modal-icon-box icon-success';
        iconBox.innerHTML = `
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                    </svg>`;
    } else {
        iconBox.className = 'modal-icon-box icon-error';
        iconBox.innerHTML = `
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
                    </svg>`;
    }

    // Mostrar modal
    modal.classList.add('active');

    // Manejar cierre
    btn.onclick = () => {
        modal.classList.remove('active');
        if (callback) callback();
    };
}