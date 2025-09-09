// Datos de ejemplo para las colecciones
const datosColecciones = {
    'Infraestructura Urbana': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [245, 156, 134, 89, 67, 45, 34, 28]
    },
    'Seguridad Ciudadana': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [198, 145, 123, 98, 76, 54, 43, 32]
    },
    'Medio Ambiente': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [167, 134, 112, 87, 65, 48, 39, 28]
    },
    'Salud Pública': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [134, 98, 87, 65, 54, 43, 32, 21]
    },
    'Educación': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [98, 76, 65, 54, 43, 32, 27, 18]
    }
};

// Variable para almacenar la instancia del gráfico
let provinciaChart;

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    inicializarGrafico();
});

function inicializarGrafico() {
    const canvas = document.getElementById('provinciaChart');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    const [coleccionInicial] = Object.keys(datosColecciones);
    const datos = datosColecciones[coleccionInicial];
    document.getElementById('selectedColeccion').textContent = coleccionInicial;
    
    provinciaChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: datos.provincias,
            datasets: [{
                label: 'Hechos',
                data: datos.valores,
                backgroundColor: '#3B82F6',
                borderColor: '#2563EB',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0,0,0,0.1)'
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
    actualizarGrafico(coleccionInicial);
}

// Función para actualizar el gráfico y resultado
function actualizarGrafico(coleccion) {
    const datos = datosColecciones[coleccion];
    if (!datos || !provinciaChart) return;
    
    // Actualizar gráfico
    provinciaChart.data.datasets[0].data = datos.valores;
    provinciaChart.update();
    
    // Calcular provincia con más hechos
    const maxValor = Math.max(...datos.valores);
    const indiceMax = datos.valores.indexOf(maxValor);
    const provinciaMax = datos.provincias[indiceMax];
    
    // Actualizar elementos del DOM
    document.getElementById('selectedColeccion').textContent = coleccion;
    document.getElementById('resultColeccion').textContent = coleccion;
    document.getElementById('resultProvincia').textContent = provinciaMax;
    document.getElementById('resultCantidad').textContent = maxValor;
}

// Event listener para manejo de dropdowns usando event delegation
document.addEventListener('click', function(e) {
    if (e.target.matches('[data-coleccion]')) {
        const coleccion = e.target.getAttribute('data-coleccion');
        actualizarGrafico(coleccion);
    }
});

// Redimensionar gráficos cuando cambia el tamaño de ventana
window.addEventListener('resize', function() {
    if (provinciaChart) {
        provinciaChart.resize();
    }
});

// Redimensionar gráficos cuando se cambian las tabs
document.addEventListener('shown.bs.tab', function() {
    setTimeout(function() {
        if (provinciaChart) {
            provinciaChart.resize();
        }
    }, 100);
});