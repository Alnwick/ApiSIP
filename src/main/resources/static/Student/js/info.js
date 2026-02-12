document.addEventListener('DOMContentLoaded', () => {
    loadUserProfile();
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