// ========== FUNCIONES DE USUARIO ==========

function toggleDropdown() {
    document.getElementById("userDropdown").classList.toggle("show");
}

function verPerfil() {
    alert('Funcionalidad de perfil en desarrollo');
    // window.location.href = 'perfil.html';
}

async function cerrarSesion() {
    if (!confirm('¿Estás seguro de que deseas cerrar sesión?')) {
        return;
    }

    // Mostrar indicador visual
    const logoutLink = document.querySelector('.dropdown-content a[onclick="cerrarSesion()"]');
    if(logoutLink) logoutLink.textContent = 'Cerrando sesión...';

    try {
        const response = await fetch('/auth/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            console.log('✅ Sesión cerrada correctamente');
        } else {
            console.warn('⚠️ El servidor no respondió 200 OK, pero redirigimos igual.');
        }
    } catch (error) {
        console.error('Error de red al cerrar sesión:', error);
    } finally {
        // Redirigir al login.
        // Como estamos en /Student/js/home.js, primero salimos de js (../)
        // y luego salimos de Student (../) para llegar a static/index.html
        // Pero como el script se ejecuta en el contexto de home.html (que está en /Student),
        // solo necesitamos subir un nivel.
        window.location.href = '../index.html';
    }
}

// Cerrar el menú desplegable si se hace clic fuera de él
window.onclick = function(event) {
    if (!event.target.matches('.icon')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}