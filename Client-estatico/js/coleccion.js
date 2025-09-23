document.addEventListener("DOMContentLoaded", () => {
const btnFiltro = document.querySelector(".btn-filtro");
const filtrosContainer = document.querySelector(".filtros-container");

btnFiltro.addEventListener("click", () => {
    const visible = filtrosContainer.style.display === "block";

    if (visible) {
    filtrosContainer.style.display = "none";
    btnFiltro.textContent = "Mostrar filtros";
    } else {
    filtrosContainer.style.display = "block";
    btnFiltro.textContent = "Ocultar filtros";
    }
});
});