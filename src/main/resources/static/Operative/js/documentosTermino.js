

const MAPA_DOCS_INICIALES = {
    
};

document.addEventListener('DOMContentLoaded', () => {
    renderUniversalHeader('users');
    volverAtras();
    InicializarSeccionRevision({
        statusSeccion: 'DOC_FINAL',
        mapaNombres: MAPA_DOCS_INICIALES,
        endpointPost: '/documents/review',
    });
    renderUniversalFooter();
});
