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