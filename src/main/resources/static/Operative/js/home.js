const ORIGIN = window.location.origin;
const API_CATALOGS = ORIGIN + '/catalogs';
const API_OPERATIVES = ORIGIN + '/operatives';

// Configuración Fija
const SCHOOL_NAME = "UPIICSA";
const SCHOOL_ACRONYM = "UPIICSA";

let selectedCareerAcronym = 'all';
let selectedPlanCode = 'all';
let selectedStatFilter = 'total';
let onlyMyReviews = false;
let statsDataCache = { total: 0, registered: 0, docInitial: 0, letterAccep: 0, docFinal: 0 };

function toggleDropdown() {
    document.getElementById("userDropdown").classList.toggle("show");
}

window.onclick = function(event) {
    if (!event.target.matches('.icon')) {
        const dropdowns = document.getElementsByClassName("dropdown-content");
        for (let d of dropdowns) {
            if (d.classList.contains('show')) d.classList.remove('show');
        }
    }
}

async function apiRequest(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error(`Error: ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error("API Error:", error);
        return null;
    }
}

async function init() {
    setupListeners();
    // Carga inicial de carreras de UPIICSA
    await fetchCareers();
    await fetchStats();
    await renderTable();
}

function setupListeners() {
    document.getElementById('searchInput').addEventListener('input', debounce(() => renderTable(), 300));

    const toggle = document.getElementById('myReviewsToggle');
    toggle.addEventListener('change', function() {
        onlyMyReviews = this.checked;
        document.getElementById('switchStatusText').textContent = onlyMyReviews ? "Activado" : "Desactivado";
        document.getElementById('switchStatusText').style.color = onlyMyReviews ? "var(--primary-green)" : "#64748b";
        renderTable();
    });

    // Evento para el botón "Todas las carreras" que ya existe en el HTML
    document.querySelector('.selectable-item[data-acronym="all"]').onclick = function() {
        selectCareer(this);
    };
}

async function fetchCareers() {
    // Nueva ruta: /catalogs/careers?SchoolName=UPIICSA
    const careers = await apiRequest(`${API_CATALOGS}/careers?SchoolName=${SCHOOL_NAME}`);
    const container = document.getElementById('careerContainer');
    if (!careers) return;

    const allOption = '<div class="selectable-item active" data-acronym="all" data-name="Todas las carreras">Todas las carreras</div>';
    container.innerHTML = allOption + careers.map((c) => `
            <div class="selectable-item" 
                 data-acronym="${c.acronym}" 
                 data-name="${c.name}">
                 ${c.acronym}
            </div>
        `).join('');

    container.querySelectorAll('.selectable-item').forEach(item => {
        item.onclick = () => selectCareer(item);
    });
}

async function selectCareer(element) {
    document.querySelectorAll('#careerContainer .selectable-item').forEach(i => i.classList.remove('active'));
    element.classList.add('active');

    selectedCareerAcronym = element.dataset.acronym;
    document.getElementById('currentTitle').textContent = element.dataset.name;

    selectedPlanCode = 'all';
    selectedStatFilter = 'total';

    if (selectedCareerAcronym === 'all') {
        document.getElementById('planContainer').innerHTML = '<div class="selectable-item active" data-code="all">Todos los planes</div>';
        await Promise.all([fetchStats(), renderTable()]);
    } else {
        await Promise.all([
            fetchSyllabus(),
            fetchStats(),
            renderTable()
        ]);
    }
}

async function fetchSyllabus() {
    if (selectedCareerAcronym === 'all') return;

    // Nueva ruta: /catalogs/syllabus?schoolAcronym=UPIICSA&careerAcronym=...
    const syllabus = await apiRequest(`${API_CATALOGS}/syllabus?schoolAcronym=${SCHOOL_ACRONYM}&careerAcronym=${selectedCareerAcronym}`);
    const container = document.getElementById('planContainer');

    container.innerHTML = '<div class="selectable-item active" data-code="all">Todos los planes</div>';

    if (syllabus && syllabus.length > 0) {
        container.innerHTML += syllabus.map(s => `
                <div class="selectable-item" data-code="${s.code}">${s.code}</div>
            `).join('');
    }

    container.querySelectorAll('.selectable-item').forEach(item => {
        item.onclick = () => {
            container.querySelectorAll('.selectable-item').forEach(i => i.classList.remove('active'));
            item.classList.add('active');
            selectedPlanCode = item.dataset.code;
            renderTable();
        };
    });
}

async function fetchStats() {
    // Ruta: /operatives/stats?careerAcronym=... (O el parámetro que use tu API para carrera)
    const stats = await apiRequest(`${API_OPERATIVES}/stats?careerAcronym=${selectedCareerAcronym}`);
    if (stats) {
        statsDataCache = stats;
    } else {
        statsDataCache = { total: 0, registered: 0, docInitial: 0, letterAccep: 0, docFinal: 0 };
    }
    drawStats();
}

function drawStats() {
    const grid = document.getElementById('statsGrid');
    // Mapeo dinámico a las nuevas llaves del DTO OperativeDashboardStatsDto
    const labelsMap = [
        { key: "total", label: "Total de Alumnos" },
        { key: "registered", label: "Registrados" },
        { key: "docInitial", label: "Documentación Inicial" },
        { key: "letterAccep", label: "Carta Aceptación" },
        { key: "docFinal", label: "Documentación Final" }
    ];

    grid.innerHTML = labelsMap.map(item => `
            <div class="stat-card ${selectedStatFilter === item.key ? 'active' : ''}" 
                 onclick="handleStatFilterClick('${item.key}')">
                <div class="stat-val">${statsDataCache[item.key] || 0}</div>
                <div class="stat-lab">${item.label}</div>
            </div>
        `).join('');
}

window.handleStatFilterClick = function(filter) {
    selectedStatFilter = filter;
    drawStats();
    renderTable();
};

async function renderTable() {
    const loading = document.getElementById('loadingTable');
    const container = document.getElementById('studentTableBody');
    const search = document.getElementById('searchInput').value;

    loading.style.display = 'block';

    const params = new URLSearchParams({
        careerAcronym: selectedCareerAcronym,
        syllabusCode: selectedPlanCode,
        stat: selectedStatFilter,
        search: search,
        onlyMine: onlyMyReviews
    });

    const alumnos = await apiRequest(`${API_OPERATIVES}/students?${params.toString()}`);
    loading.style.display = 'none';

    if (!alumnos || alumnos.length === 0) {
        container.innerHTML = `<tr><td colspan="4" style="text-align:center; padding: 30px; color: #64748b;">No se encontraron alumnos para los criterios seleccionados.</td></tr>`;
        return;
    }

    container.innerHTML = alumnos.map(a => `
            <tr class="row-${a.visualStatus || 'new'}" onclick="window.location.href='detalles.html?id=${a.enrollment}'" style="cursor:pointer">
                <td>${a.syllabusCode}</td>
                <td><strong>${a.fullName}</strong></td>
                <td>${a.enrollment}</td>
                <td>${a.processStateName}</td>
            </tr>
        `).join('');
}

function debounce(func, wait) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

document.addEventListener('DOMContentLoaded', init);