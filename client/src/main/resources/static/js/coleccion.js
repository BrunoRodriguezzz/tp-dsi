console.log('Script de nuevaColeccion cargado');
document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('fuentesContainer');
    const loading = document.getElementById('fuentesLoading');
    container.style.display = 'none';
    loading.style.display = 'block';
    fetch('/colecciones/fuentes')
        .then(response => {
            if (!response.ok) {
                throw new Error('HTTP status ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            container.innerHTML = '';
            if (!Array.isArray(data)) {
                container.innerHTML = '<p class="text-danger">Respuesta inesperada del servidor.</p>';
            } else if (data.length === 0) {
                container.innerHTML = '<p>No hay fuentes disponibles.</p>';
            } else {
                data.forEach(fuente => {
                    const div = document.createElement('div');
                    div.className = 'form-check fade-in';
                    div.innerHTML = `
                        <input class="form-check-input" type="checkbox" name="fuentes" value="${fuente.nombre}" id="fuente_${fuente.id}">
                        <label class="form-check-label" for="fuente_${fuente.id}">${fuente.nombre}</label>
                    `;
                    container.appendChild(div);
                });
            }
            loading.style.display = 'none';
            container.style.display = 'block';
        })
        .catch((err) => {
            container.innerHTML = `<p class="text-danger">Error al obtener fuentes: ${err.message}</p>`;
            loading.style.display = 'none';
            container.style.display = 'block';
        });

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

