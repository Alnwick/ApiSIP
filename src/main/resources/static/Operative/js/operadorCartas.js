const urlParams = new URLSearchParams(window.location.search);
const enrollment = urlParams.get('enrollment');

const MAPA_CARTAS = {
    'CARTA_PRESENTACION': 'Carta de Presentación',
    'CARTA_ACEPTACION': 'Carta de Aceptación Firmada'
};

document.addEventListener('DOMContentLoaded', () => {
    renderUniversalHeader('operative');
    InicializarSeccionRevision({
        statusSeccion: 'CARTAS',
        mapaNombres: MAPA_CARTAS,
        endpointPost: '/documents/review',
        contenedorListaId: 'docs-list'
    });
    renderUniversalFooter();
});