document.addEventListener("DOMContentLoaded", function () {
        const filtroSelect = document.getElementById("filtroSolicitudes");
        const solicitudes = document.querySelectorAll(".solicitud-card");
        const contador = document.querySelector(".filtro-contador");

        function aplicarFiltro() {
        const filtro = filtroSelect.value;
        let visibles = 0;

        solicitudes.forEach(card => {
        const estado = card.getAttribute("data-estado");

        if (filtro === "todas" || estado === filtro) {
        card.style.display = "block";
        visibles++;
        } else {
            card.style.display = "none";
        }
        });

            contador.textContent = `${visibles} solicitud${visibles !== 1 ? 'es' : ''} mostrada${visibles !== 1 ? 's' : ''}`;
        }

            filtroSelect.addEventListener("change", aplicarFiltro);
            aplicarFiltro(); // aplicar filtro inicial
        });