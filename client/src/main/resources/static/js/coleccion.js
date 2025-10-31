// coleccion.js loaded
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.form-coleccion');
    if (!form) {
        console.error('No se encontró el formulario con clase .form-coleccion');
    } else {
        form.addEventListener('submit', function(e) {
            // submit intercepted
            e.preventDefault();
            const nombre = document.getElementById('titleInput').value;
            const descripcion = document.getElementById('descripcionInput').value;
            const criterio = {
                categoria: document.getElementById('categoriaInput').value,
                fechaCargaInicio: document.getElementById('fechaCargaInicioInput').value,
                fechaCargaFin: document.getElementById('fechaCargaFinInput').value,
                fechaAcontecimientoInicio: document.getElementById('fechaAcontecimientoInicioInput').value,
                fechaAcontecimientoFin: document.getElementById('fechaAcontecimientoFinInput').value,
                titulo: document.getElementById('criterioTituloInput').value,
                latitud: document.getElementById('latitudInput').value,
                longitud: document.getElementById('longitudInput').value
            };
            const fuentes = Array.from(document.querySelectorAll('input[name="fuentes"]:checked')).map(cb => cb.value);
            const consensos = Array.from(document.querySelectorAll('input[name="consensos"]:checked')).map(cb => cb.value);
            const coleccionInput = {
                nombre: nombre,
                descripcion: descripcion,
                criterio: criterio,
                fuentes: fuentes,
                consensos: consensos
            };
            // DTO ready to send
            fetch('/colecciones', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(coleccionInput)
            })
            .then(response => {
                if (response.ok) {
                    alert('Colección creada exitosamente');
                    window.location.href = '/colecciones';
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .catch(err => {
                alert('Error al crear la colección: ' + err.message);
            });
        });
    }
});

// Extraer la lógica de fetch y render en una función reutilizable
function fetchYRenderHechos(query) {
    const main = document.querySelector('main.container');
    const coleccionId = main ? main.dataset.coleccionId : 0;
    if (!coleccionId || coleccionId === '0') return;
    const agregadorUrl = 'http://localhost:8082/colecciones/' + coleccionId + '/hechos' + (query || '');
    // calling agregador: agregadorUrl
    fetch(agregadorUrl)
        .then(response => {
            // agregador response status
            if (!response.ok) throw new Error('Error al obtener hechos: ' + response.status + ' ' + response.statusText);
            return response.json();
        })
        .then(data => {
            // datos recibidos del agregador
            const hechos = Array.isArray(data) ? data : (data.content || []);
            const container = document.getElementById('hechos-container');
            if (!container) return;
            if (!hechos || hechos.length === 0) {
                container.innerHTML = '<p>No hay hechos para esta colección.</p>';
                return;
            }
            let html = '';
            hechos.forEach(hecho => {
                const ubic = hecho.ubicacion || {};
                const ubicText = (ubic.latitud !== undefined && ubic.longitud !== undefined) ? (`lat: ${ubic.latitud}, lon: ${ubic.longitud}`) : (ubic.provincia || ubic.muncipio || '');
                const fechaAcontecimiento = hecho.fechaAcontecimiento ? new Date(hecho.fechaAcontecimiento).toLocaleString() : '';
                const fechaCarga = hecho.fechaCarga ? new Date(hecho.fechaCarga).toLocaleString() : '';
                html += `
                <div class="hecho-card">
                    <div class="card-col-izq">
                        <div class="card-header">${hecho.titulo || ''}</div>
                        <div class="card-desc">${hecho.descripcion || ''}</div>
                        <div class="card-info-row">
                            <p><b>Fecha:</b> ${fechaAcontecimiento}</p>
                            <p><b>Ubicación:</b> ${ubicText}</p>
                            <p><b>Fuente:</b> ${hecho.fuente || ''}</p>
                        </div>
                        <div class="card-etiquetas">
                            <!-- etiquetas no disponibles en DTO por defecto -->
                        </div>
                        <div class="card-footer">
                            <i class="fa-regular fa-calendar"></i>
                            <span>Publicado: ${fechaCarga}</span>
                        </div>
                    </div>
                    <div class="card-col-der">
                        <span class="card-categoria">${hecho.categoria || ''}</span>
                        <div class="card-botones">
                            <a href="/hechos/${hecho.id}" class="btn btn-dark">Ver Detalle</a>
                            <a href="/reportarHecho" class="btn btn-red">Reportar</a>
                        </div>
                    </div>
                </div>`;
            });
            container.innerHTML = html;
        })
        .catch(err => {
            const container = document.getElementById('hechos-container');
            if (container) container.innerHTML = '<p>Error al cargar los hechos: ' + err.message + '</p>';
            console.error('Error al cargar hechos de la colección:', err);
        });
}

document.addEventListener('DOMContentLoaded', function() {
    try {
        const main = document.querySelector('main.container');
        const coleccionId = main ? main.dataset.coleccionId : 0;
        if (!coleccionId || coleccionId === '0') return;
        const rawQuery = window.location.search || '';
        // querystring detected

        // Mapear parámetros que produce el fragmento de filtros a los que espera el agregador
        // Ej: lat -> latitud, lng -> longitud
        const params = new URLSearchParams(rawQuery);
        if (params.has('lat')) {
            params.set('latitud', params.get('lat'));
            params.delete('lat');
        }
        if (params.has('lng')) {
            params.set('longitud', params.get('lng'));
            params.delete('lng');
        }

        // Transformar fechas (YYYY-MM-DD) a LocalDateTime ISO que espera el agregador
        const dateFieldsStart = ['fechaAcontecimientoInicio','fechaCargaInicio'];
        const dateFieldsEnd = ['fechaAcontecimientoFin','fechaCargaFin'];

        dateFieldsStart.forEach(name => {
            if (params.has(name)) {
                const v = params.get(name);
                if (/^\d{4}-\d{2}-\d{2}$/.test(v)) {
                    params.set(name, v + 'T00:00:00');
                }
            }
        });
        dateFieldsEnd.forEach(name => {
            if (params.has(name)) {
                const v = params.get(name);
                if (/^\d{4}-\d{2}-\d{2}$/.test(v)) {
                    params.set(name, v + 'T23:59:59');
                }
            }
        });

        const mappedQuery = params.toString();
        const query = mappedQuery ? ('?' + mappedQuery) : '';

        // Cargar hechos inicialmente (si hay querystring en la URL)
        fetchYRenderHechos(query);

        // Interceptar submit del formulario de filtros y hacer fetch sin recargar
        let filtrosForm = document.querySelector('#filtrosCollapse form');
        if (!filtrosForm) {
            // intentos alternativos por si Thymeleaf/inserción cambia el DOM
            filtrosForm = document.querySelector('#filtrosCollapse');
            if (filtrosForm) filtrosForm = filtrosForm.querySelector('form');
        }
        if (!filtrosForm) {
            // buscar cualquier formulario dentro del fragmento si el id cambia
            const collapseEl = document.getElementById('filtrosCollapse');
            if (collapseEl) filtrosForm = collapseEl.querySelector('form');
        }

        if (filtrosForm) {
            // filtros form found
            filtrosForm.addEventListener('submit', async function(e) {
                e.preventDefault();
                // filtros submit intercepted
                const formData = new FormData(filtrosForm);
                const p = new URLSearchParams();
                for (const [key, value] of formData.entries()) {
                    // form entry processed
                    if (value == null || value === '') continue;
                    p.append(key, value);
                }
                // mapear lat/lng
                if (p.has('lat')) { p.set('latitud', p.get('lat')); p.delete('lat'); }
                if (p.has('lng')) { p.set('longitud', p.get('lng')); p.delete('lng'); }

                // Si no hay latitud/longitud, intentar extraer del input visible del geocoder
                try {
                    const hasLat = p.has('latitud') && p.get('latitud') !== '';
                    const hasLng = p.has('longitud') && p.get('longitud') !== '';
                    if (!hasLat || !hasLng) {
                        // primero intentar usar resultado global del geocoder si existe
                        if (window.__lastGeocoderResult && window.__lastGeocoderResult.geometry && window.__lastGeocoderResult.geometry.coordinates) {
                            try {
                                const coordsG = window.__lastGeocoderResult.geometry.coordinates; // [lng, lat]
                                const latG = coordsG[1];
                                const lngG = coordsG[0];
                                // using last geocoder result for coords
                                p.set('latitud', String(latG));
                                p.set('longitud', String(lngG));
                                const latInputEl = document.getElementById('latitudInput'); if (latInputEl) latInputEl.value = latG;
                                const lngInputEl = document.getElementById('longitudInput'); if (lngInputEl) lngInputEl.value = lngG;
                            } catch (err) {
                                console.warn('coleccion.js: error leyendo window.__lastGeocoderResult', err);
                            }
                        } else {
                            // buscar input del geocoder en el DOM
                            let geocoderValue = '';
                            try {
                                const selectors = [
                                    '#geocoder input[type="text"]',
                                    '#geocoder .mapboxgl-ctrl-geocoder--input',
                                    '.mapboxgl-ctrl-geocoder--input',
                                    '#geocoder input',
                                ];
                                for (const sel of selectors) {
                                    const el = document.querySelector(sel);
                                    if (el && el.value && el.value.trim()) { geocoderValue = el.value.trim(); break; }
                                }
                                // si no lo encontramos, intentar propiedades internas del geocoder expuesto
                                if (!geocoderValue && window.__mapboxGeocoder) {
                                    const g = window.__mapboxGeocoder;
                                    const candidates = [
                                        g._inputEl,
                                        g._mapboxGeocoder && g._mapboxGeocoder._inputEl,
                                        g.inputEl,
                                        g._input,
                                        g._container && g._container.querySelector && g._container.querySelector('input')
                                    ];
                                    for (const c of candidates) {
                                        try {
                                            if (c && c.value && c.value.trim()) { geocoderValue = c.value.trim(); break; }
                                        } catch (e) { /* skip */ }
                                    }
                                }
                            } catch (err) {
                                console.warn('coleccion.js: error buscando input del geocoder', err);
                                geocoderValue = '';
                            }

                            if (geocoderValue) {
                                // geocoder text detected
                                // intentar forward geocoding con Mapbox
                                const token = window.__mapboxAccessToken || ((window.mapboxgl && window.mapboxgl.accessToken) ? window.mapboxgl.accessToken : null);
                                if (token) {
                                    const url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + encodeURIComponent(geocoderValue) + '.json?access_token=' + encodeURIComponent(token) + '&limit=1&language=es';
                                    // calling Mapbox geocoding
                                    try {
                                        const resp = await fetch(url);
                                        if (resp.ok) {
                                            const geo = await resp.json();
                                            if (geo.features && geo.features.length > 0) {
                                                const coords = geo.features[0].geometry.coordinates; // [lng, lat]
                                                const lat = coords[1];
                                                const lng = coords[0];
                                                p.set('latitud', String(lat));
                                                p.set('longitud', String(lng));
                                                const latInputEl = document.getElementById('latitudInput'); if (latInputEl) latInputEl.value = lat;
                                                const lngInputEl = document.getElementById('longitudInput'); if (lngInputEl) lngInputEl.value = lng;
                                            } else {
                                                console.warn('coleccion.js: no se encontraron resultados en la respuesta de geocoding');
                                            }
                                        } else {
                                            console.warn('coleccion.js: respuesta no OK de Mapbox geocoding', resp.status, resp.statusText);
                                        }
                                    } catch (err) {
                                        console.error('coleccion.js: error en fetch a Mapbox geocoding', err);
                                    }
                                } else {
                                    console.warn('coleccion.js: token de Mapbox no disponible');
                                }
                            }
                        }
                    }
                } catch (err) {
                    console.warn('coleccion.js: error procesando latitud/longitud', err);
                }

                const mappedQuery = p.toString();
                const query = mappedQuery ? ('?' + mappedQuery) : '';
                fetchYRenderHechos(query);
            });
        }
    } catch (err) {
        console.error('Error en coleccion.js:', err);
    }
});
