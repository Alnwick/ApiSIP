// Foto de perfil
const profileImage = document.getElementById('profileImage');
const fileInput = document.getElementById('fileInput');
const saveProfileBtn = document.getElementById('saveProfileBtn');

// Al hacer clic en la imagen, se abre el selector de archivos
profileImage.addEventListener('click', function() {
    fileInput.click();
});

// Cuando se selecciona un archivo, se previsualiza
fileInput.addEventListener('change', function(e) {
    if (e.target.files.length) {
        const reader = new FileReader();
        reader.onload = function(event) {
            profileImage.src = event.target.result;
        }
        reader.readAsDataURL(e.target.files[0]);
    }
});

// Botón de guardar (simulado)
saveProfileBtn.addEventListener('click', function() {
    alert('Foto de perfil guardada correctamente');
    // Aquí iría la lógica para guardar la imagen en el servidor (Fetch API hacia tu controlador Spring Boot)
});