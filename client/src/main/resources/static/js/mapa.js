mapboxgl.accessToken = 'pk.eyJ1IjoiZmVybmFuZG8xN2EiLCJhIjoiY21nbnk3MDg2MXpteTJucHJsdDllNzZuZCJ9.KuFr7I-l2wBE6ONQk3GpGw';

let map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v10',
    center: [-58.3818, -34.6038],
    zoom: 15
});

const btnGPS = document.getElementById('btnGPS');
if (btnGPS) {
    btnGPS.addEventListener('click', miUbiacion);
}

map.addControl(new mapboxgl.NavigationControl());

const geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl,
    placeholder: 'Buscar una ubicación...',
    marker: false,
    language: 'es'
});

// Exponer el geocoder globalmente para que otros scripts puedan leer su input
try {
    window.__mapboxGeocoder = geocoder;
} catch (err) {
    console.warn('mapa.js: no se pudo exponer window.__mapboxGeocoder', err);
}

// Exponer token para que otros scripts puedan usarlo si es necesario
try {
    window.__mapboxAccessToken = mapboxgl.accessToken;
} catch (err) {
    console.warn('mapa.js: no se pudo exponer window.__mapboxAccessToken', err);
}

document.getElementById('geocoder').appendChild(geocoder.onAdd(map));

// función helper para setear el input oculto de ubicacion
function setUbicacionText(val) {
    try {
        const el = document.getElementById('ubicacionInput');
        if (el) {
            el.value = val || '';
        }
    } catch (err) {
        console.warn('mapa.js: error seteando ubicacionInput', err);
    }
}

// intentar encontrar el input del control geocoder y escuchar eventos
function bindGeocoderInputEvents() {
    try {
        const container = document.getElementById('geocoder');
        const selectors = ['input[type="text"]', '.mapboxgl-ctrl-geocoder--input', 'input'];
        let inputEl = null;
        const tryFind = () => {
            if (!container) return null;
            for (const sel of selectors) {
                const el = container.querySelector(sel);
                if (el && el.value !== undefined) return el;
            }
            // última oportunidad: any input inside container
            const any = container.querySelector('input');
            if (any && any.value !== undefined) return any;
            return null;
        };

        inputEl = tryFind();
        if (inputEl) {
            inputEl.addEventListener('input', function(e) { setUbicacionText(inputEl.value); });
            inputEl.addEventListener('blur', function(e) { setUbicacionText(inputEl.value); });
            return;
        }

        // Si no encontramos input, usar MutationObserver en el container para detectarlo cuando se agregue
        if (container) {
            const observer = new MutationObserver((mutations, obs) => {
                const found = tryFind();
                if (found) {
                    found.addEventListener('input', function(e) { setUbicacionText(found.value); });
                    found.addEventListener('blur', function(e) { setUbicacionText(found.value); });
                    obs.disconnect();
                }
            });
            observer.observe(container, { childList: true, subtree: true });
            // fallback: desconectar observer luego de 6s
            setTimeout(() => {
                try { observer.disconnect(); } catch (e) {}
            }, 6000);
            return;
        }

        console.warn('mapa.js: no se encontró el elemento contenedor #geocoder para observar');
    } catch (err) {
        console.warn('mapa.js: error intentando vincular eventos al input del geocoder', err);
    }
}

bindGeocoderInputEvents();

geocoder.on('result', function (e) {
    try {
        window.__lastGeocoderResult = e.result;
    } catch (err) {
        console.warn('mapa.js: no se pudo asignar window.__lastGeocoderResult', err);
    }

    // setear el texto de ubicacion en el input oculto
    try { setUbicacionText(e.result.place_name || ''); } catch (err) { /* ignore */ }

    const coords = e.result.geometry.coordinates; // [long, lat]
    const lat = coords[1];
    const lng = coords[0];

    clickMarker.setLngLat(coords).addTo(map);

    const latInput = document.getElementById('latInput');
    if (latInput) {
        latInput.value = lat;
    } else {
        console.error("mapa.js: Elemento latInput no encontrado");
    }

    const lngInput = document.getElementById('lngInput');
    if (lngInput) {
        lngInput.value = lng;
    } else {
        console.error("mapa.js: Elemento lngInput no encontrado");
    }

    // Rellenar también latitud/longitud para compatibilidad con coleccion
    const latitudInput = document.getElementById('latitudInput');
    if (latitudInput) {
        latitudInput.value = lat;
    } else {
        console.warn('mapa.js: latitudInput no encontrado');
    }
    const longitudInput = document.getElementById('longitudInput');
    if (longitudInput) {
        longitudInput.value = lng;
    } else {
        console.warn('mapa.js: longitudInput no encontrado');
    }

    const municipioInput = document.getElementById('municipioInput');
    if (municipioInput) {
        const municipio = e.result.context?.[1].text || '';
        municipioInput.value = municipio;
    } else {
        console.error("mapa.js: Elemento municipioInput no encontrado");
    }

    const provinciaInput = document.getElementById('provinciaInput');
    if (provinciaInput) {
        const provincia = e.result.context?.[2].text || '';
        provinciaInput.value = provincia;
    } else {
        console.error("mapa.js: Elemento provinciaInput no encontrado");
    }
});

// Handler para el botón 'Usar ubicación' que forza geocoding del texto escrito
function bindUsarUbicButton() {
    try {
        const btn = document.getElementById('usarUbicBtn');
        if (!btn) { return; }
        btn.addEventListener('click', async function() {
            try {
                // Obtener texto del geocoder (varios métodos)
                let val = '';
                const getText = () => {
                    const selectors = ['#geocoder input[type="text"]', '#geocoder .mapboxgl-ctrl-geocoder--input', '.mapboxgl-ctrl-geocoder--input', '#geocoder input'];
                    for (const sel of selectors) {
                        const el = document.querySelector(sel);
                        if (el && el.value && el.value.trim()) return el.value.trim();
                    }
                    // intentar input oculto
                    const hidden = document.getElementById('ubicacionInput');
                    if (hidden && hidden.value && hidden.value.trim()) return hidden.value.trim();
                    // intentar el control expuesto
                    const g = window.__mapboxGeocoder;
                    if (g) {
                        try { if (g._inputEl && g._inputEl.value) return g._inputEl.value.trim(); } catch(e){}
                        try { if (g.inputEl && g.inputEl.value) return g.inputEl.value.trim(); } catch(e){}
                    }
                    return '';
                };
                val = getText();
                if (!val) { console.warn('mapa.js: no se detectó texto en el geocoder para usar'); return; }
                const token = window.__mapboxAccessToken || (window.mapboxgl && window.mapboxgl.accessToken) || '';
                if (!token) { console.warn('mapa.js: token no disponible para geocoding'); return; }
                const url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + encodeURIComponent(val) + '.json?access_token=' + encodeURIComponent(token) + '&limit=1&language=es';
                const resp = await fetch(url);
                if (!resp.ok) { console.warn('mapa.js: usarUbicBtn geocoding fallo status=', resp.status); return; }
                const geo = await resp.json();
                if (geo.features && geo.features.length > 0) {
                    const feat = geo.features[0];
                    window.__lastGeocoderResult = feat;
                    setUbicacionText(feat.place_name || val);
                    const coords = feat.geometry.coordinates; // [lng, lat]
                    const lat = coords[1]; const lng = coords[0];
                    const latInput = document.getElementById('latitudInput'); if (latInput) { latInput.value = lat; }
                    const lngInput = document.getElementById('longitudInput'); if (lngInput) { lngInput.value = lng; }
                    const latLegacy = document.getElementById('latInput'); if (latLegacy) latLegacy.value = lat;
                    const lngLegacy = document.getElementById('lngInput'); if (lngLegacy) lngLegacy.value = lng;
                } else {
                    console.warn('mapa.js: usarUbicBtn geocoding no devolvió features');
                }
            } catch (err) {
                console.error('mapa.js: error en usarUbicBtn', err);
            }
        });
    } catch (err) {
        console.warn('mapa.js: error vinculando usarUbicBtn', err);
    }
}

bindUsarUbicButton();

// Solo agregar listener si el elemento existe (no todas las páginas tienen filtrosCollapse)
const filtrosCollapse = document.getElementById('filtrosCollapse');
if (filtrosCollapse) {
    filtrosCollapse.addEventListener('shown.bs.collapse', function () {
        map.resize();
    });
}

let clickMarker = new mapboxgl.Marker({ color: 'red' });

// --- CLICK EN EL MAPA PARA SELECCIONAR UN PUNTO ---
map.on('click', async function (e) {
    const lat = e.lngLat.lat;
    const lng = e.lngLat.lng;

    // Mover marcador o crearlo
    clickMarker.setLngLat([lng, lat]).addTo(map);

    // Guardar en inputs
    document.getElementById('latInput').value = lat;
    document.getElementById('lngInput').value = lng;

    // También los "legacy"
    const latitudInput = document.getElementById('latitudInput');
    if (latitudInput) latitudInput.value = lat;
    const longitudInput = document.getElementById('longitudInput');
    if (longitudInput) longitudInput.value = lng;

    // Reverse geocoding para obtener municipio / provincia
    const token = mapboxgl.accessToken;
    const url = `https://api.mapbox.com/geocoding/v5/mapbox.places/${lng},${lat}.json?types=place,region&language=es&access_token=${token}`;

    try {
        const resp = await fetch(url);
        const data = await resp.json();

        let municipio = "";
        let provincia = "";

        if (data.features && data.features.length > 0) {
            data.features.forEach(f => {
                if (f.place_type.includes("place")) municipio = f.text;
                if (f.place_type.includes("region")) provincia = f.text;
            });
        }

        document.getElementById('municipioInput').value = municipio;
        document.getElementById('provinciaInput').value = provincia;

        // (Opcional) llenar el texto del geocoder con la dirección encontrada
        if (data.features[0] && data.features[0].place_name) {
            setUbicacionText(data.features[0].place_name);
        }

    } catch (err) {
        console.error("Error en reverse geocoding: ", err);
    }
});

function miUbiacion() {
    // Verificar si el navegador soporta geolocalización
    if (!navigator.geolocation) {
        alert("Tu navegador no soporta la geolocalización.");
        return;
    }

    // Mostrar estado de carga (opcional, visual)
    const originalText = btnGPS.innerHTML;
    btnGPS.innerHTML = 'Buscando...';
    btnGPS.disabled = true;

    navigator.geolocation.getCurrentPosition(
        async (position) => {
            // 1. OBTENER COORDENADAS
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;

            // 2. MOVER EL MAPA Y EL MARCADOR
            map.flyTo({
                center: [lng, lat],
                zoom: 14,
                essential: true
            });

            clickMarker.setLngLat([lng, lat]).addTo(map);

            // 3. LLENAR INPUTS NUMÉRICOS (Lat/Lng)
            // Inputs principales
            const latInput = document.getElementById('latInput');
            if (latInput) latInput.value = lat;
            const lngInput = document.getElementById('lngInput');
            if (lngInput) lngInput.value = lng;

            // Inputs legacy/compatibilidad
            const latitudInput = document.getElementById('latitudInput');
            if (latitudInput) latitudInput.value = lat;
            const longitudInput = document.getElementById('longitudInput');
            if (longitudInput) longitudInput.value = lng;

            // 4. REVERSE GEOCODING (Coordenadas -> Texto/Provincia/Municipio)
            const token = mapboxgl.accessToken;
            // Pedimos tipos place, region y address
            const url = `https://api.mapbox.com/geocoding/v5/mapbox.places/${lng},${lat}.json?types=place,region,address,poi&language=es&access_token=${token}`;

            try {
                const resp = await fetch(url);
                const data = await resp.json();

                let municipio = "";
                let provincia = "";
                let direccionCompleta = "";

                if (data.features && data.features.length > 0) {
                    // La mejor coincidencia suele ser la primera (address o poi)
                    direccionCompleta = data.features[0].place_name;

                    // Buscar contexto para provincia y municipio
                    data.features.forEach(f => {
                        if (f.place_type.includes("place")) municipio = f.text;
                        if (f.place_type.includes("region")) provincia = f.text;
                    });

                    // Si no encontró municipio en los features principales, buscar en el contexto del primer resultado
                    if (!municipio && data.features[0].context) {
                        const munCtx = data.features[0].context.find(c => c.id.startsWith('place'));
                        if (munCtx) municipio = munCtx.text;
                    }
                    if (!provincia && data.features[0].context) {
                        const provCtx = data.features[0].context.find(c => c.id.startsWith('region'));
                        if (provCtx) provincia = provCtx.text;
                    }
                }

                // Llenar inputs de texto
                const municipioInput = document.getElementById('municipioInput');
                if (municipioInput) municipioInput.value = municipio;

                const provinciaInput = document.getElementById('provinciaInput');
                if (provinciaInput) provinciaInput.value = provincia;

                // Llenar el input visible del geocoder o el hidden de ubicación
                setUbicacionText(direccionCompleta);

                // Actualizar visualmente el input del geocoder de Mapbox si existe
                // (Esto es un truco visual para que el usuario vea la dirección en la barra de búsqueda)
                const geocoderInput = document.querySelector('.mapboxgl-ctrl-geocoder--input');
                if (geocoderInput) geocoderInput.value = direccionCompleta;

            } catch (err) {
                console.error("Error en reverse geocoding GPS: ", err);
            } finally {
                // Restaurar botón
                btnGPS.innerHTML = originalText;
                btnGPS.disabled = false;
            }

        },
        (error) => {
            console.error("Error obteniendo ubicación:", error);
            let msg = "No se pudo obtener la ubicación.";
            if (error.code === 1) msg = "Permiso de ubicación denegado.";
            if (error.code === 2) msg = "Ubicación no disponible.";
            if (error.code === 3) msg = "Se agotó el tiempo de espera.";
            alert(msg);

            // Restaurar botón
            btnGPS.innerHTML = originalText;
            btnGPS.disabled = false;
        },
        {
            enableHighAccuracy: true, // Intentar usar GPS real
            timeout: 10000,
            maximumAge: 0
        }
    )
}