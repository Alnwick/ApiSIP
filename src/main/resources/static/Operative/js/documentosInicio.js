const API_BASE = '/view-documents/';

/**
 * Carga el PDF en el visor derecho (Idéntico a generarCedula)
 */
function viewPdf(fileName, title) {
    const container = document.getElementById('pdfContainer');
    document.getElementById('pdf-title').textContent = title;

    // Aquí se usaría el endpoint que ya configuramos en Java
    // Simulado:
    container.innerHTML = `<iframe src="${API_BASE}${fileName}"></iframe>`;
}

function approveActa() {
    if(confirm("¿Confirmas la aprobación del Acta de Aceptación para este alumno?")) {
        alert("Acta aprobada con éxito. El alumno será notificado.");
    }
}

function saveReview() {
    alert("La revisión ha sido guardada. Los comentarios han sido enviados al portal del alumno.");
    window.location.href = 'home.html';
}