document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("modo-toggle");
    const textoModo = document.getElementById("texto-modo");
    const descripcion = document.getElementById("descripcion-modo");

    toggle.addEventListener("change", () => {
        if (toggle.checked) {
            textoModo.innerHTML = '<i class="icono-escudo">🛡️</i> Navegando en modo Curado';
            descripcion.textContent = "Se muestran solo los hechos verificados por curadores.";
        } else {
            textoModo.innerHTML = '<i class="icono-escudo">🛡️</i> Navegando en modo Irrestricto';
            descripcion.textContent = "Se muestran todos los hechos disponibles, incluyendo contenido no verificado.";
        }
    });
});
