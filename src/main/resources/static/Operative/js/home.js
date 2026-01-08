// Datos de ejemplo para las carreras
const careerData = {
    transporte: {
        nombre: "Transporte",
        stats: { total: 756, sinRevision: 398, revisado: 63, finalizados: 265, seguimiento: 30, hombres: 378, mujeres: 378 },
        estudiantes: [
            { plan: "Plan 2020", genero: "Masculino", nombre: "Santiago Hernandez Martínez", boleta: "2021402087", estado: "Documentos de Inicio", status: "new" },
            { plan: "Plan 2020", genero: "Femenino", nombre: "Ana López García", boleta: "2021402156", estado: "Documentos de Seguimiento", status: "visited" },
            { plan: "Plan 2018", genero: "Masculino", nombre: "Carlos Méndez Ruiz", boleta: "2021402189", estado: "Documentos de Inicio", status: "review" },
            { plan: "Plan 2020", genero: "Femenino", nombre: "María Fernández Soto", boleta: "2021402210", estado: "Finalizado", status: "correct" }
            // ... más datos
        ]
    },
    informatica: {
        nombre: "Ingeniería en Informática",
        stats: { total: 650, sinRevision: 320, revisado: 85, finalizados: 245, seguimiento: 42, hombres: 325, mujeres: 325 },
        estudiantes: [
            { plan: "Plan 2020", genero: "Femenino", nombre: "Laura Torres Sánchez", boleta: "2021402256", estado: "Documentos de Seguimiento", status: "visited" }
        ]
    }
    // ... añadir el resto de carreras según tu código original
};

let currentCareer = 'transporte';
let currentFilter = 'total';
let currentPlan = 'general';

function toggleDropdown() {
    document.getElementById("userDropdown").classList.toggle("show");
}

window.onclick = function(event) {
    if (!event.target.matches('.icon')) {
        const dropdowns = document.getElementsByClassName("dropdown-content");
        for (let i = 0; i < dropdowns.length; i++) {
            if (dropdowns[i].classList.contains('show')) {
                dropdowns[i].classList.remove('show');
            }
        }
    }
}

function updateStats() {
    const statsContainer = document.getElementById('careerStats');
    const stats = careerData[currentCareer].stats;

    statsContainer.innerHTML = `
    <div class="stat-box ${currentFilter === 'total' ? 'active' : ''}" onclick="setFilter('total')">
      <div class="stat-title">Total Alumnos</div>
      <div class="stat-value">${stats.total}</div>
    </div>
    <div class="stat-box ${currentFilter === 'sinRevision' ? 'active' : ''}" onclick="setFilter('sinRevision')">
      <div class="stat-title">Sin Revisión</div>
      <div class="stat-value">${stats.sinRevision}</div>
    </div>
    <div class="stat-box ${currentFilter === 'revisado' ? 'active' : ''}" onclick="setFilter('revisado')">
      <div class="stat-title">Revisados</div>
      <div class="stat-value">${stats.revisado}</div>
    </div>
    <div class="stat-box ${currentFilter === 'finalizados' ? 'active' : ''}" onclick="setFilter('finalizados')">
      <div class="stat-title">Finalizados</div>
      <div class="stat-value">${stats.finalizados}</div>
    </div>
    <div class="stat-box ${currentFilter === 'seguimiento' ? 'active' : ''}" onclick="setFilter('seguimiento')">
      <div class="stat-title">Seguimiento</div>
      <div class="stat-value">${stats.seguimiento}</div>
    </div>
  `;
}

function renderTable() {
    const tableBody = document.getElementById('tableBody');
    tableBody.innerHTML = '';

    let students = careerData[currentCareer].estudiantes;
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    // Filtros de búsqueda, plan y estado
    let filtered = students.filter(s => {
        const matchesSearch = s.nombre.toLowerCase().includes(searchTerm) || s.boleta.includes(searchTerm);
        const matchesPlan = currentPlan === 'general' || s.plan === currentPlan;

        let matchesStatus = true;
        if (currentFilter === 'sinRevision') matchesStatus = (s.status === 'new' || s.status === 'visited');
        if (currentFilter === 'revisado') matchesStatus = (s.status === 'review');
        if (currentFilter === 'finalizados') matchesStatus = (s.status === 'correct');

        return matchesSearch && matchesPlan && matchesStatus;
    });

    filtered.forEach(est => {
        const row = document.createElement('tr');
        row.classList.add(`status-${est.status}`);
        row.innerHTML = `
      <td>${est.plan}</td>
      <td>${est.genero}</td>
      <td>${est.nombre}</td>
      <td>${est.boleta}</td>
      <td>${est.estado}</td>
    `;
        tableBody.appendChild(row);
    });
}

function setFilter(filter) {
    currentFilter = filter;
    updateStats();
    renderTable();
}

// Event Listeners
document.getElementById('planSelect').addEventListener('change', (e) => {
    currentPlan = e.target.value;
    renderTable();
});

document.getElementById('searchInput').addEventListener('input', renderTable);

document.querySelectorAll('.career-item').forEach(item => {
    item.addEventListener('click', function() {
        document.querySelector('.career-item.active').classList.remove('active');
        this.classList.add('active');
        currentCareer = this.dataset.career;
        document.getElementById('selectedCareer').textContent = `Información de ${careerData[currentCareer].nombre}`;
        updateStats();
        renderTable();
    });
});

// Inicialización
document.addEventListener('DOMContentLoaded', () => {
    updateStats();
    renderTable();
});