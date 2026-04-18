
const ETAPAS_ORDEN = ['DOC_INICIAL', 'CARTAS', 'DOC_FINAL', 'LIBERACION'];

function gestionarNavegacionRevision(currentStatus, todasLasEtapas) {
    
    const { inicial, cartas, termino } = todasLasEtapas;
    const estaAprobado = (docs) => docs && docs.length > 0 && docs.every(d => d.status === 'CORRECTO' || d.status === 'ACEPTADO');

    const inicialOK = estaAprobado(inicial);
    const cartasOK = estaAprobado(cartas);
    const terminoOK = estaAprobado(termino);

    ETAPAS_ORDEN.forEach(etapa => {
        const btn = document.getElementById(`nav-${etapa}`);
        if (!btn) return;

        let bloqueado = false;
        let razon = "";

    
        if (etapa === 'CARTAS' && !inicialOK) {
            bloqueado = true;
            razon = "Primero aprueba todos los Documentos Iniciales.";
        }
        if (etapa === 'DOC_FINAL' && !cartasOK) {
            bloqueado = true;
            razon = "Primero aprueba todas las Cartas.";
        }
        if (etapa === 'LIBERACION' && !terminoOK) {
            bloqueado = true;
            razon = "Primero aprueba los Documentos de Término.";
        }

        const rutas = {
            'DOC_INICIAL': 'documentosInicio.html',
            'CARTAS': 'operadorCartas.html',
            'DOC_FINAL': 'documentosTermino.html',
            'LIBERACION': 'operadorLiberacion.html'
        };

        btn.classList.remove('active', 'locked');

        if (bloqueado) {
            btn.classList.add('locked');
            btn.onclick = (e) => {
                e.preventDefault();
                showModal("Sección Bloqueada", razon, "info");
            };
        } else {
            btn.classList.remove('locked');
            btn.onclick = () => window.location.href = `${rutas[etapa]}?enrollment=${enrollment}`;
        }

        if (etapa === currentStatus) btn.classList.add('active');
    });
}