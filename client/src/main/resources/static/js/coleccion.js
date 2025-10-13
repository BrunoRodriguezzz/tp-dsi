document.addEventListener("DOMContentLoaded", () => {
const btnFiltro = document.querySelector(".btn-filtro");
const filtrosContainer = document.querySelector(".filtros-container");

btnFiltro.addEventListener("click", () => {
    const visible = filtrosContainer.style.display === "block";

    if (visible) {
    filtrosContainer.style.display = "none";
    btnFiltro.textContent = "Mostrar filtros";
    } else {
    filtrosContainer.style.display = "block";
    btnFiltro.textContent = "Ocultar filtros";
    }
});
});

// --- CÓDIGO UNIFICADO Y ROBUSTO PARA FILTRADO Y MODOS ---

let hechosOriginales = [];
let modoActual = 'curado';
let algoritmoSeleccionado = null;

function getAlgoritmos(hecho) {
    // Soporta tanto 'algoritmos' como 'consensos' como nombre de la propiedad
    return Array.isArray(hecho.algoritmos) ? hecho.algoritmos : (Array.isArray(hecho.consensos) ? hecho.consensos : []);
}

function renderHechos(hechos) {
    const container = document.getElementById('hechos-container');
    if (!hechos || hechos.length === 0) {
        container.innerHTML = '<p>No hay hechos para esta colección.</p>';
        return;
    }
    let html = '<div class="row">';
    hechos.forEach(hecho => {
        html += `
        <div class="col">
            <div class="hecho-card">
                <div class="hecho-card-mas-opciones">
                    <div class="titulo-mas-descripcion">
                        <h2 class="titulo-Hecho">${hecho.titulo || ''}</h2>
                        <p class="descripcion">${hecho.descripcion || ''}</p>
                    </div>
                    <div class="opciones">
                        <p class="etiqueta-categoria config-categoria distancia-opciones">${hecho.categoria || ''}</p>
                        <a href="/hechos/verDetalle/${hecho.id}" class="btn btn-dark"><span>Ver Detalle</span></a>
                        <a href="/reportarHecho" class="btn btn-red config-opciones">Reportar</a>
                    </div>
                </div>
                <div class="informacion-general">
                    <div class="fecha-ubicacion-fuente encolumnar">
                        <p class="info-fecha-ubicacion-fuente"><b>Fecha: </b><span>${hecho.fechaAcontecimiento || ''}</span></p>
                        <p class="info-fecha-ubicacion-fuente"><b>Ubicación: </b><span>${hecho.ubicacion || ''}</span></p>
                        <p class="info-fecha-ubicacion-fuente"><b>Fuente: </b><span>${hecho.fuente || ''}</span></p>
                    </div>
                    <div class="etiquetas">
                        ${(hecho.etiquetas || []).map(etiqueta => `<p class="titulo-etiqueta etiqueta-interes">${etiqueta}</p>`).join('')}
                    </div>
                    <div class="ultima-linea">
                        <div class="fechaPublicacion">
                            <i class="fa-regular fa-calendar"></i>
                            <p class="alineacion">Publicado: <span>${hecho.fechaPublicacion || ''}</span></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>`;
    });
    html += '</div>';
    container.innerHTML = html;
}

function filtrarHechos() {
    let hechosFiltrados = hechosOriginales;
    // Filtrado por modo
    if (modoActual === 'curado') {
        hechosFiltrados = hechosFiltrados.filter(h => getAlgoritmos(h).length > 0);
    } else if (modoActual === 'irrestricto') {
        hechosFiltrados = hechosFiltrados.filter(h => getAlgoritmos(h).length === 0);
    }
    // Filtrado por algoritmo de consenso
    if (algoritmoSeleccionado) {
        hechosFiltrados = hechosFiltrados.filter(h => getAlgoritmos(h).includes(algoritmoSeleccionado));
    }
    renderHechos(hechosFiltrados);
}

function activarBotonModo(modo) {
    document.querySelectorAll('.modo-navegacion-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.dataset.modo === modo) btn.classList.add('active');
    });
}

function activarBotonAlgoritmo(algoritmo) {
    document.querySelectorAll('.filtro-algoritmo').forEach(btn => {
        btn.classList.remove('active');
        if (btn.dataset.algoritmo === algoritmo) btn.classList.add('active');
    });
}

window.addEventListener('DOMContentLoaded', function() {
    // Mostrar/ocultar filtros
    const btnFiltro = document.querySelector(".btn-filtro");
    const filtrosContainer = document.querySelector(".filtros-container");
    if (btnFiltro && filtrosContainer) {
        btnFiltro.addEventListener("click", () => {
            const visible = filtrosContainer.style.display === "block";
            if (visible) {
                filtrosContainer.style.display = "none";
                btnFiltro.textContent = "Mostrar filtros";
            } else {
                filtrosContainer.style.display = "block";
                btnFiltro.textContent = "Ocultar filtros";
            }
        });
    }

    // Cargar hechos y guardar copia original
    var coleccionId = window.coleccionId || (typeof COLECCION_ID !== 'undefined' ? COLECCION_ID : 0);
    if (coleccionId === 0) return;
    fetch('/coleccion/' + coleccionId + '/hechos')
        .then(response => response.json())
        .then(hechos => {
            hechosOriginales = hechos;
            if (hechos.length > 0) {
                console.log('Ejemplo de hecho:', hechos[0]);
            }
            filtrarHechos();
        });

    // Modo de navegación
    document.querySelectorAll('.modo-navegacion-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            modoActual = btn.dataset.modo;
            activarBotonModo(modoActual);
            filtrarHechos();
        });
    });

    // Filtro por algoritmo de consenso
    document.querySelectorAll('.filtro-algoritmo').forEach(btn => {
        btn.addEventListener('click', function() {
            if (algoritmoSeleccionado === btn.dataset.algoritmo) {
                algoritmoSeleccionado = null;
                activarBotonAlgoritmo(null);
            } else {
                algoritmoSeleccionado = btn.dataset.algoritmo;
                activarBotonAlgoritmo(algoritmoSeleccionado);
            }
            filtrarHechos();
        });
    });
});
