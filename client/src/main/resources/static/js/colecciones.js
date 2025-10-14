document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("modoToggle");
    const estado = document.getElementById("modoEstado");
    const texto = estado.querySelector("span");

    toggle.addEventListener("change", () => {
        if (toggle.checked) {
            estado.classList.remove("irrestricto");
            estado.classList.add("curado");
            texto.textContent = "Navegando en modo Curado";
        } else {
            estado.classList.remove("curado");
            estado.classList.add("irrestricto");
            texto.textContent = "Navegando en modo Irrestricto";
        }
    });
});
