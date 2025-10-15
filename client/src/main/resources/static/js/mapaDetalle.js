mapboxgl.accessToken = 'pk.eyJ1IjoiZmVybmFuZG8xN2EiLCJhIjoiY21nbnk3MDg2MXpteTJucHJsdDllNzZuZCJ9.KuFr7I-l2wBE6ONQk3GpGw'

function mostrarMapaDelHecho({lat, lng}) {
    let map = new mapboxgl.Map({
        container:'map',
        style: 'mapbox://styles/mapbox/streets-v10',
        center:[lng, lat],  // Nota el orden: lng, lat
        zoom:15
    });

    map.addControl(new mapboxgl.NavigationControl());

    // Opcionalmente agregar marcador en esa ubicación:
    new mapboxgl.Marker().setLngLat([lng, lat]).addTo(map);
}