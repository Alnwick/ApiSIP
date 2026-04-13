
async function InicializarSeccionRevision(config) {
    const {
        statusSeccion,      // 'DOC_INICIAL', 'CARTAS', 'DOC_FINAL'
        mapaNombres,        // El diccionario de nombres
        endpointPost,       // URL para guardar
        proximaEtapa,       // { idBoton: 'irCartas', functionVerificar: verificarAccesoACartas }
        contenedorListaId = 'docs-list'
    } = config;

    // 1. Validación de boleta (Común a todos)
    if (!enrollment) {
        alert("No se especificó la boleta del alumno.");
        window.location.href = 'home.html';
        return;
    }

    renderUniversalHeader('operative');

    try {
        // 2. Carga de Datos Única
        const data = await SeccionInfoEstudiante(enrollment, statusSeccion);

        if (data && data.documents) {
            currentDocuments = data.documents;
            
            // 3. Renderizado Dinámico
            renderDocumentsGenerico(currentDocuments, mapaNombres, contenedorListaId);
            
            // 4. Verificación de flujo (Si existe botón para la siguiente etapa)
            if (proximaEtapa) {
                proximaEtapa.functionVerificar(currentDocuments, proximaEtapa.idBoton);
            }
        }

        // 5. Configurar botones de guardado (POST)
        setupActionButtonsGenerico(endpointPost);
        renderUniversalFooter();

    } catch (error) {
        console.error("Error al inicializar la sección:", error);
    }
}

// Función de renderizado que usa el mapa de nombres inyectado
function renderDocumentsGenerico(docs, mapa, containerId) {
    const container = document.getElementById(containerId);

    console.log("Contenedor encontrado:", container); // Si sale null, el ID está mal
    console.log("Documentos a renderizar:", docs);    // Si sale [] o undefined, el API no trajo nada
    if (!container) return;
    container.innerHTML = '';

    const fragment = document.createDocumentFragment();
    docs.forEach((doc, index) => {
        const tarjeta = TarjetaOperador(doc, {
            index: index,
            docPath: '/view-documents/',
            mapaNombres: mapa,
            tipoDocumento: doc.typeName || doc.typeCode,
            onView: (url, title) => viewPdf(url, title)
        });
        fragment.appendChild(tarjeta);
    });
    container.appendChild(fragment);
}

function setupActionButtonsGenerico(endpoint) {
    const btnFinalize = document.getElementById('btn-finalize-review');
    if (!btnFinalize) return;

    btnFinalize.onclick = async () => {
        const reviews = [];
        // ... toda tu lógica de recolección de radios y comentarios ...

        try {
            const res = await fetch(`${endpoint}?enrollment=${enrollment}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(reviews)
            });
            // ... lógica de éxito/error con showModal ...
        } catch (e) {
            // ...
        }
    };
}
