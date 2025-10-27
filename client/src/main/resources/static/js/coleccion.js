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

document.addEventListener('DOMContentLoaded', function() {
    try {
        const main = document.querySelector('main.container');
        const coleccionId = main ? main.dataset.coleccionId : 0;
        if (!coleccionId || coleccionId === '0') return;
        fetch('/coleccion/' + coleccionId + '/hechos')
            .then(response => response.json())
            .then(hechos => {
                const container = document.getElementById('hechos-container');
                if (!hechos || hechos.length === 0) {
                    container.innerHTML = '<p>No hay hechos para esta colección.</p>';
                    return;
                }
                let html = '';
                hechos.forEach(hecho => {
                    html += `
                    <div class="hecho-card">
                        <div class="card-col-izq">
                            <div class="card-header">${hecho.titulo || ''}</div>
                            <div class="card-desc">${hecho.descripcion || ''}</div>
                            <div class="card-info-row">
                                <p><b>Fecha:</b> ${hecho.fechaAcontecimiento || ''}</p>
                                <p><b>Ubicación:</b> ${hecho.ubicacion || ''}</p>
                                <p><b>Fuente:</b> ${hecho.fuente || ''}</p>
                            </div>
                            <div class="card-etiquetas">
                                ${(hecho.etiquetas || []).map(etiqueta => `<span class="card-etiqueta">${etiqueta}</span>`).join('')}
                            </div>
                            <div class="card-footer">
                                <i class="fa-regular fa-calendar"></i>
                                <span>Publicado: ${hecho.fechaPublicacion || ''}</span>
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
            .catch(() => {
                const container = document.getElementById('hechos-container');
                if (container) container.innerHTML = '<p>Error al cargar los hechos.</p>';
            });
    } catch (e) {
        console.error('Error al cargar hechos de la colección:', e);
    }
});
