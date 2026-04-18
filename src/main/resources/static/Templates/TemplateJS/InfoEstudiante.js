async function SeccionInfoEstudiante(enrollment, processStatus, containerId = 'info-estudiante-container') {
    const container = document.getElementById(containerId);
    if (!container) return;

    const API_URL = `/students/toReview?enrollment=${enrollment}&processStatus=${processStatus}`;

    try {
        const resp = await fetch(API_URL);
        if (!resp.ok) throw new Error("No se pudo obtener la información del alumno");
        
        const data = await resp.json();

        container.innerHTML = `
            <section class="info-card">
                <div class="student-grid">
                    <div class="data-item">
                        <label>Alumno</label>
                        <span>${data.name || '--'}</span>
                    </div>
                    <div class="data-item">
                        <label>Boleta</label>
                        <span>${data.enrollment || '--'}</span>
                    </div>
                    <div class="data-item">
                        <label>Carrera</label>
                        <span>${data.career || '--'}</span>
                    </div>
                    <div class="data-item">
                        <label>Plan de Estudios</label>
                        <span>${data.syllabus || '--'}</span>
                    </div>
                    <div class="data-item" style="grid-column: span 2;">
                        <label>Semestre</label>
                        <span>${data.semester || '--'}</span>
                    </div>
                    </div>
            </section>
        `;

        return data;

    } catch (e) {
        console.error("Error en SeccionInfoEstudiante:", e);
        container.innerHTML = `<div style="color:red; padding:10px;">Error al cargar perfil del alumno.</div>`;
        return null;
    }
}