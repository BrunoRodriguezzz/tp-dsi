mapboxgl.accessToken = 'pk.eyJ1IjoiZmVybmFuZG8xN2EiLCJhIjoiY21nbnk3MDg2MXpteTJucHJsdDllNzZuZCJ9.KuFr7I-l2wBE6ONQk3GpGw'

let map = new mapboxgl.Map({
    container:'map',
    style: 'mapbox://styles/mapbox/streets-v10',
    center:[-58.3818,-34.6038],
    zoom:15
})

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
    const coords = e.result.geometry.coordinates; // [long, lat]
    const lat = coords[1];
    const lng = coords[0];

    // Guardar en los inputs ocultos
    document.getElementById('latInput').value = lat;
    document.getElementById('lngInput').value = lng;

    // (Opcional) También podés autocompletar el municipio si querés
    const municipio = e.result.context?.find(c => c.id.includes('place'))?.text || '';
    document.getElementById('municipioInput').value = municipio;
});