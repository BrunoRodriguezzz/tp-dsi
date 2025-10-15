document.addEventListener("DOMContentLoaded", () => {
    const switchInput = document.getElementById("switchCheckDefault");
    const curadoContainer = document.querySelector(".curado-container");
    const irrestrictoContainer = document.querySelector(".irrestricto-container");

    // Arranca mostrando irrestricto
    curadoContainer.style.display = "none";
    irrestrictoContainer.style.display = "block";

    switchInput.addEventListener("change", () => {
      if (switchInput.checked) {
        // Si está activado → mostrar curado
        curadoContainer.style.display = "block";
        irrestrictoContainer.style.display = "none";
      } else {
        // Si está desactivado → mostrar irrestricto
        curadoContainer.style.display = "none";
        irrestrictoContainer.style.display = "block";
      }
    });
  });

document.addEventListener('DOMContentLoaded', function () {
    const modoToggle = document.getElementById('modoToggle');
    const descripcionModo = document.getElementById('descripcionModo');
    if (modoToggle && descripcionModo) {
        modoToggle.addEventListener('change', function() {
            const modoEstado = document.getElementById('modoEstado');
            if (this.checked) {
                descripcionModo.textContent = 'Solo se muestran hechos verificados y validados por la comunidad';
                if (modoEstado) {
                    modoEstado.classList.remove('irrestricto');
                    modoEstado.classList.add('curado');
                    modoEstado.innerHTML = '<i class="icon">🟩</i><span>Navegando en modo Curado</span>';
                }
            } else {
                descripcionModo.textContent = 'Se muestran todos los hechos disponibles, incluyendo contenido no verificado.';
                if (modoEstado) {
                    modoEstado.classList.remove('curado');
                    modoEstado.classList.add('irrestricto');
                    modoEstado.innerHTML = '<i class="icon">🟦</i><span>Navegando en modo Irrestricto</span>';
                }
            }
        });
    }
});
