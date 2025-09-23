const HOME_PAGE = './index.html';
// ID del contenedor del navbar: debe coincidir con el ID en index.html y en la pantalla donde se quiere usar
const NAVBAR_ID = 'navbar-container';

// Función para cargar el navbar del HOME_PAGE
async function loadNavbar() {
    alert("Hola");
    try {
        // Cargar el contenido "virtualmente" del HOME_PAGE
        const response = await fetch(HOME_PAGE);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const homeHTML = await response.text();
        const container = document.getElementById(NAVBAR_ID);

        // Extraer el navbar
        const parser = new DOMParser();
        const homeDoc = parser.parseFromString(homeHTML, 'text/html');
        const homeNavbar = homeDoc.getElementById(NAVBAR_ID);

        // Copiar el contenido
        if (homeNavbar) container.innerHTML = homeNavbar.innerHTML;

    } catch (error) {
        console.error('Error loading navbar:', error);
    }
}

// Ejecutar cuando cargue la página
document.addEventListener('DOMContentLoaded', loadNavbar);