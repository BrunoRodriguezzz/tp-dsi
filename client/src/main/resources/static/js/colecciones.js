document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("modoToggle");
    const estado = document.getElementById("modoEstado");
    const texto = estado.querySelector("span");
    const descripcion = document.getElementById("descripcionModo");
    const tarjetas = document.querySelectorAll(".tarjeta");

    function actualizarCantidadHechos(modoCurado) {
        tarjetas.forEach(tarjeta => {
            const cantidadElement = tarjeta.querySelector(".cantidad-hechos");
            const hechosCurados = tarjeta.getAttribute("data-hechos-curado");
            const hechosIrrestricto = tarjeta.getAttribute("data-hechos-irrestricto");

            if (cantidadElement) {
                if (modoCurado) {
                    cantidadElement.textContent = hechosCurados + " hechos registrados";
                } else {
                    cantidadElement.textContent = hechosIrrestricto + " hechos registrados";
                }
            } else {
                console.error("No se encontró elemento .cantidad-hechos en tarjeta");
            }
        });
    }

    function actualizarEnlacesColecciones() {
        const enlaces = document.querySelectorAll(".ver-link");
        const modoCurado = toggle.checked;

        enlaces.forEach(enlace => {
            const href = enlace.getAttribute("href");
            const baseHref = href.split("?")[0];

            if (modoCurado) {
                enlace.setAttribute("href", baseHref + "?modo=curado");
            } else {
                enlace.setAttribute("href", baseHref + "?modo=irrestricto");
            }
        });
    }

    toggle.addEventListener("change", () => {
        if (toggle.checked) {
            estado.classList.remove("irrestricto");
            estado.classList.add("curado");
            texto.textContent = "Navegando en modo Curado";
            descripcion.textContent = "Se muestran sólo los hechos para los que se tiene consenso.";
            actualizarCantidadHechos(true);
            actualizarEnlacesColecciones();
        } else {
            estado.classList.remove("curado");
            estado.classList.add("irrestricto");
            texto.textContent = "Navegando en modo Irrestricto";
            descripcion.textContent = "Se muestran todos los hechos disponibles, incluyendo contenido no verificado.";
            actualizarCantidadHechos(false);
            actualizarEnlacesColecciones();
        }
    });

    actualizarEnlacesColecciones();
});

