document.addEventListener('DOMContentLoaded', function(){
    const form = document.getElementById('publicarHechoForm');
    if(!form) return;

    const requiredIds = {
        titulo: 'titleInput',
        descripcion: 'descripcionInput',
        categoria: 'categoriaSelect',
        fechaAcontecimiento: 'fechaInput'
    };

    function markInvalid(el){
        if(!el) return;
        el.classList.add('is-invalid');
        el.addEventListener('input', () => el.classList.remove('is-invalid'), { once: true });
    }

    form.addEventListener('submit', function(ev){
        let errores = [];
        // limpiar clases previas
        Object.values(requiredIds).forEach(id => {
            const el = document.getElementById(id);
            if(el) el.classList.remove('is-invalid');
        });

        // Validar campos texto/select
        if(!document.getElementById('titleInput')?.value?.trim()){ errores.push('Título'); markInvalid(document.getElementById('titleInput')); }
        if(!document.getElementById('descripcionInput')?.value?.trim()){ errores.push('Descripción'); markInvalid(document.getElementById('descripcionInput')); }

        const catSel = document.getElementById('categoriaSelect');
        if(!catSel || !catSel.value){ errores.push('Categoría'); markInvalid(catSel); }

        const fecha = document.getElementById('fechaInput');
        if(!fecha || !fecha.value){ errores.push('Fecha Acontecimiento'); markInvalid(fecha); }

        const lat = document.getElementById('latInput')?.value;
        const lng = document.getElementById('lngInput')?.value;
        if(!lat || !lng){ errores.push('Ubicación (selecciona en el mapa)'); }

        // Datos de usuario ocultos
        const nombre = document.getElementById('nombre')?.value;
        const apellido = document.getElementById('apellido')?.value;
        const fechaNac = document.getElementById('fechaNac')?.value;

        if(errores.length){
            ev.preventDefault();
            let alert = document.querySelector('#validationAlert');
            if(!alert){
                alert = document.createElement('div');
                alert.id = 'validationAlert';
                alert.className = 'alert alert-danger';
                form.parentElement.insertBefore(alert, form);
            }
            alert.textContent = 'Revisa los campos obligatorios: ' + errores.join(', ');
            window.scrollTo({ top: alert.offsetTop - 20, behavior: 'smooth' });
        }
    });
});