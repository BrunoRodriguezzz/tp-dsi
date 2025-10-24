mapboxgl.accessToken = 'pk.eyJ1IjoiZmVybmFuZG8xN2EiLCJhIjoiY21nbnk3MDg2MXpteTJucHJsdDllNzZuZCJ9.KuFr7I-l2wBE6ONQk3GpGw';

let map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v10',
    center: [-58.3818, -34.6038],
    zoom: 15
});

map.addControl(new mapboxgl.NavigationControl());

const geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl,
    placeholder: 'Buscar una ubicación...',
    marker: true,
    language: 'es'
});

document.getElementById('geocoder').appendChild(geocoder.onAdd(map));

geocoder.on('result', function (e) {
    console.log("Ubicación seleccionada:", e.result);

    const coords = e.result.geometry.coordinates; // [long, lat]
    const lat = coords[1];
    const lng = coords[0];

    const latInput = document.getElementById('latInput');
    if (latInput) {
        latInput.value = lat;
    } else {
        console.error("Elemento latInput no encontrado");
    }

    const lngInput = document.getElementById('lngInput');
    if (lngInput) {
        lngInput.value = lng;
    } else {
        console.error("Elemento lngInput no encontrado");
    }

    const municipioInput = document.getElementById('municipioInput');
    if (municipioInput) {
        const municipio = e.result.context?.[1].text || '';
        municipioInput.value = municipio;
    } else {
        console.error("Elemento municipioInput no encontrado");
    }

    const provinciaInput = document.getElementById('provinciaInput');
    if (provinciaInput) {
        const provincia = e.result.context?.[2].text || '';
        provinciaInput.value = provincia;
    } else {
        console.error("Elemento provinciaInput no encontrado");
    }
});

document.getElementById('filtrosCollapse').addEventListener('shown.bs.collapse', function () {
    map.resize();
});
