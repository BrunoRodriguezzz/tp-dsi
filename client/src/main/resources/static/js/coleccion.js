console.log('coleccion.js cargado');
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.form-coleccion');
    if (!form) {
        console.error('No se encontró el formulario con clase .form-coleccion');
    } else {
        form.addEventListener('submit', function(e) {
            console.log('Submit interceptado por JS');
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
            console.log('DTO a enviar:', coleccionInput);
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
