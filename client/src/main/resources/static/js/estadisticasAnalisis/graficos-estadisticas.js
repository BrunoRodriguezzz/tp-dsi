// ===============================================
// FUNCIONES REUTILIZABLES GLOBALES
// ===============================================

function crearGraficoBarras(canvas, datos, opciones = {}) {
    const ctx = canvas.getContext('2d');
    return new Chart(ctx, {
        type: 'bar',
        data: {
            labels: datos.labels,
            datasets: [{
                label: opciones.label || 'Hechos',
                data: datos.values,
                backgroundColor: opciones.backgroundColor || '#3B82F6',
                borderColor: opciones.borderColor || '#2563EB',
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
            },
            ...opciones.chartOptions
        }
    });
}

function crearGraficoPie(canvas, datos, opciones = {}) {
    const ctx = canvas.getContext('2d');
    return new Chart(ctx, {
        type: 'pie',
        data: {
            labels: datos.labels,
            datasets: [{
                data: datos.values,
                backgroundColor: datos.colors,
                borderColor: '#fff',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = Math.round((context.parsed / total) * 100);
                            return `${context.label}: ${context.parsed} hechos (${percentage}%)`;
                        }
                    }
                }
            },
            ...opciones.chartOptions
        }
    });
}

function crearGraficoLinea(canvas, datos, opciones = {}) {
    const ctx = canvas.getContext('2d');
    return new Chart(ctx, {
        type: 'line',
        data: {
            labels: datos.labels,
            datasets: [{
                label: opciones.label || 'Hechos',
                data: datos.values,
                backgroundColor: opciones.backgroundColor || 'rgba(59, 130, 246, 0.1)',
                borderColor: opciones.borderColor || '#3B82F6',
                borderWidth: 2,
                fill: true,
                tension: 0.4
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
            },
            ...opciones.chartOptions
        }
    });
}

function crearGraficoDonut(canvas, datos, opciones = {}) {
    const ctx = canvas.getContext('2d');
    return new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: datos.labels,
            datasets: [{
                data: datos.values,
                backgroundColor: datos.colors,
                borderColor: '#fff',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = Math.round((context.parsed / total) * 100);
                            return `${context.label}: ${context.parsed} solicitudes (${percentage}%)`;
                        }
                    }
                }
            },
            ...opciones.chartOptions
        }
    });
}

function encontrarEnPosicion(datos, posicion = 0) {
    if (Array.isArray(datos.values) && Array.isArray(datos.labels)) {
        if (posicion >= 0 && posicion < datos.values.length) {
            return {
                valor: datos.values[posicion],
                indice: posicion,
                label: datos.labels[posicion]
            };
        }
    }
    return null;
}

function encontrarMaximo(datos) {
    if (Array.isArray(datos.values)) {
        const maxValor = Math.max(...datos.values);
        const indiceMax = datos.values.indexOf(maxValor);
        return {
            valor: maxValor,
            indice: indiceMax,
            label: datos.labels[indiceMax]
        };
    }
    return null;
}

function encontrarSegundoMaximo(datos) {
    if (Array.isArray(datos.values)) {
        const valoresOrdenados = [...datos.values].sort((a, b) => b - a);
        const segundoValor = valoresOrdenados[1];
        const indiceSegundo = datos.values.indexOf(segundoValor);
        return {
            valor: segundoValor,
            indice: indiceSegundo,
            label: datos.labels[indiceSegundo]
        };
    }
    return null;
}

function actualizarElementoTexto(elementId, texto) {
    const elemento = document.getElementById(elementId);
    if (elemento) {
        elemento.textContent = texto;
    }
}

function calcularPorcentajes(valores) {
    const total = valores.reduce((a, b) => a + b, 0);
    return valores.map(valor => Math.round((valor / total) * 100));
}

// ===============================================
// DATOS DE EJEMPLO
// ===============================================

// Datos para Tab 1 (Colección por Provincia)
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

// Datos para Tab 2 (Categorías General)
const datosCategorias = {
    labels: ['Infraestructura', 'Seguridad', 'Medio Ambiente', 'Salud', 'Transporte', 'Servicios Públicos', 'Educación', 'Cultura'],
    values: [342, 298, 234, 189, 156, 140, 89, 67],
    colors: ['#3B82F6', '#EF4444', '#10B981', '#F59E0B', '#8B5CF6', '#06B6D4', '#84CC16', '#F97316']
};

// Datos para Tab 3 (Categoría por Provincia)
const datosCategoriaPorProvincia = {
    'Infraestructura': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [89, 67, 54, 43, 32, 28, 21, 18]
    },
    'Seguridad': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [76, 65, 54, 43, 34, 29, 23, 19]
    },
    'Medio Ambiente': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [67, 56, 45, 38, 31, 25, 20, 16]
    },
    'Salud': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [54, 43, 38, 32, 27, 22, 18, 15]
    },
    'Transporte': {
        provincias: ['Buenos Aires', 'Córdoba', 'Santa Fe', 'Mendoza', 'Tucumán', 'Entre Ríos', 'Salta', 'Chaco'],
        valores: [43, 36, 31, 26, 22, 19, 15, 12]
    }
};

// Datos para Tab 4 (Categoría por Hora)
const datosCategoriaHora = {
    'Infraestructura': {
        horas: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
        valores: [45, 67, 78, 89, 98, 92, 134, 76, 54]
    },
    'Seguridad': {
        horas: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
        valores: [32, 45, 56, 78, 89, 102, 123, 156, 98]
    },
    'Medio Ambiente': {
        horas: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
        valores: [23, 34, 45, 67, 78, 89, 98, 76, 45]
    },
    'Salud': {
        horas: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
        valores: [18, 29, 38, 54, 67, 78, 89, 65, 43]
    },
    'Transporte': {
        horas: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
        valores: [67, 89, 76, 54, 43, 65, 78, 56, 34]
    }
};

// Datos para Tab 5 (Solicitudes Spam)
const datosSpam = {
    labels: ['Spam', 'No Spam'],
    values: [window.spam, window.noSpam],
    colors: ['#EF4444', '#10B981']
};

// ===============================================
// NAMESPACE ORGANIZADO
// ===============================================

const EstadisticasApp = {
    tab1: {
        chart: null,
        datos: window.datosColecciones || datosColecciones,
        
        inicializar() {
            const canvas = document.getElementById('provinciaChart');
            if (!canvas) return;

            if (!this.datos || Object.keys(this.datos).length === 0) return;
            const [coleccionInicial] = Object.keys(this.datos);
            const datosIniciales = this.datos[coleccionInicial];

            console.log("Colección: ", coleccionInicial);
            console.log("Labels:", Object.keys(datosIniciales));
            console.log("Values:", Object.values(datosIniciales));

            this.chart = crearGraficoBarras(canvas, {
                labels: Object.keys(datosIniciales),
                values: Object.values(datosIniciales)
            });

            /*
            this.chart = crearGraficoBarras(canvas, {
                labels: datosIniciales.provincias,
                values: datosIniciales.valores
            });
            */
            actualizarElementoTexto('selectedColeccion', coleccionInicial);
            this.actualizarResultado(coleccionInicial);
        },
        
        actualizar(coleccion) {
            const datos = this.datos[coleccion];
            console.log("Datos: ", datos);
            console.log("Valores: ", datos.valores)
            if (!datos || !this.chart) return;
            
            // Actualizar gráfico
            //this.chart.data.datasets[0].data = datos.valores;
            this.chart.data.datasets[0].data = Object.values(datos);
            this.chart.data.labels = Object.keys(datos);
            this.chart.update();
            
            // Actualizar resultado
            this.actualizarResultado(coleccion);
        },

        actualizarResultado(coleccion) {
            const datos = this.datos[coleccion];
            const resultado = encontrarEnPosicion({
                labels: Object.keys(datos),
                values: Object.values(datos)
            });
            /*
            const resultado = encontrarMaximo({
                labels: datos.provincias,
                values: datos.valores
            });
             */
            actualizarElementoTexto('selectedColeccion', coleccion);
            actualizarElementoTexto('resultColeccion', coleccion);
            actualizarElementoTexto('resultProvincia', resultado.label);
            actualizarElementoTexto('resultCantidad', resultado.valor);
        }
    },
    
    tab2: {
        chart: null,
        datos: window.rankingCategorias || datosCategorias,
        
        inicializar() {
            const canvas = document.getElementById('categoriaChart');
            if (!canvas) return;
            
            this.chart = crearGraficoPie(canvas,
                {
                    labels: this.datos.map(item => item.categoria),
                    values: this.datos.map(item => item.total),
                    colors: this.datos.map(item => item.color)
                });
            this.actualizarRanking();
            this.actualizarResultado();
        },
        
        actualizarRanking() {
            console.log('Ranking actualizado para Tab 2');
        },
        
        actualizarResultado() {
            // const resultado = encontrarMaximo(this.datos);
            const resultado = encontrarEnPosicion({
                labels: this.datos.map(item => item.categoria),
                values: this.datos.map(item => item.total)
            });
            const segundoMayor = encontrarEnPosicion({
                    labels: this.datos.map(item => item.categoria),
                    values: this.datos.map(item => item.total)
            }, 1);

            actualizarElementoTexto('categoriaTop', resultado.label);
            actualizarElementoTexto('cantidadTop', resultado.valor);
            actualizarElementoTexto('categoriaSegunda', segundoMayor.label);
            actualizarElementoTexto('cantidadSegunda', segundoMayor.valor);

            console.log(`Mayor: ${resultado.label} con ${resultado.valor} hechos`);
            console.log(`Segundo: ${segundoMayor.label} con ${segundoMayor.valor} hechos`);
        }
    },

    tab3: {
        chart: null,
        datos: window.categoriasPorProvincia || datosCategoriaPorProvincia,
        
        inicializar() {
            const canvas = document.getElementById('categoriaProvinciaChart');
            if (!canvas) return;
            console.log(this.datos);

            // Tomar el primer objeto del array
            const categoriaInicial = this.datos[0];

            // Extraer nombre de la categoría
            const nombreCategoria = categoriaInicial.categoria;

            // Extraer provincias y valores
            const provincias = Object.keys(categoriaInicial.provinciasConHechos);
            const valores = Object.values(categoriaInicial.provinciasConHechos);

            /*
            const [categoriaInicial] = Object.keys(this.datos);
            const datosIniciales = this.datos[categoriaInicial];

            console.log("Categoria Inicial: ", categoriaInicial);
            console.log("datosIniciales: ", datosIniciales);

             */
            this.chart = crearGraficoBarras(canvas, {
                labels: provincias,
                values: valores
            }, {
                backgroundColor: '#10B981',
                borderColor: '#059669'
            });

            actualizarElementoTexto('selectedCategoriaP', nombreCategoria);
            this.actualizarResultado(nombreCategoria);
        },
        
        actualizar(categoria) {
            const datos = this.datos.find(d => d.categoria === categoria);
            if (!datos || !this.chart) return;
            console.log("Datos:", datos);
            console.log("Data: ", Object.values(datos.provinciasConHechos));
            console.log("Labels: ", Object.keys(datos.provinciasConHechos));
            // Actualizar gráfico
            this.chart.data.datasets[0].data = Object.values(datos.provinciasConHechos);
            this.chart.data.labels = Object.keys(datos.provinciasConHechos);
            this.chart.update();
            
            // Actualizar resultado
            this.actualizarResultado(categoria);
        },
        
        actualizarResultado(categoria) {
            const datos = this.datos.find(d => d.categoria === categoria);
            const resultado = encontrarEnPosicion({
                labels: Object.keys(datos.provinciasConHechos),
                values: Object.values(datos.provinciasConHechos)
            });
            
            actualizarElementoTexto('selectedCategoriaP', categoria);
            actualizarElementoTexto('resultCategoriaP', categoria);
            actualizarElementoTexto('resultProvinciaP', resultado.label);
            actualizarElementoTexto('resultCantidadP', resultado.valor);
        }
    },

    tab4: {
        chart: null,
        datos: datosCategoriaHora,
        
        inicializar() {
            const canvas = document.getElementById('horaChart');
            if (!canvas) return;
            
            const [categoriaInicial] = Object.keys(this.datos);
            const datosIniciales = this.datos[categoriaInicial];
            
            this.chart = crearGraficoLinea(canvas, {
                labels: datosIniciales.horas,
                values: datosIniciales.valores
            }, {
                backgroundColor: 'rgba(245, 158, 11, 0.1)',
                borderColor: '#F59E0B'
            });
            
            actualizarElementoTexto('selectedCategoriaH', categoriaInicial);
            this.actualizarResultado(categoriaInicial);
        },
        
        actualizar(categoria) {
            const datos = this.datos[categoria];
            if (!datos || !this.chart) return;
            
            // Actualizar gráfico
            this.chart.data.datasets[0].data = datos.valores;
            this.chart.update();
            
            // Actualizar resultado
            this.actualizarResultado(categoria);
        },
        
        actualizarResultado(categoria) {
            const datos = this.datos[categoria];
            const resultado = encontrarMaximo({
                labels: datos.horas,
                values: datos.valores
            });
            const segundoMaximo = encontrarSegundoMaximo({
                labels: datos.horas,
                values: datos.valores
            });
            
            actualizarElementoTexto('selectedCategoriaH', categoria);
            actualizarElementoTexto('resultCategoriaH', categoria);
            actualizarElementoTexto('resultHoraMaxima', resultado.label);
            actualizarElementoTexto('resultCantidadH', resultado.valor);
            actualizarElementoTexto('resultHoraSegunda', segundoMaximo.label);
            actualizarElementoTexto('resultCantidadSegundaH', segundoMaximo.valor);
        }
    },

    tab5: {
        chart: null,
        datos: datosSpam,
        
        inicializar() {
            const canvas = document.getElementById('spamChart');
            if (!canvas) return;
            
            this.chart = crearGraficoDonut(canvas, this.datos);
            this.actualizarResultado();
        },
        
        actualizarResultado() {
            const total = this.datos.values.reduce((a, b) => a + b, 0);
            const spam = this.datos.values[0];
            const porcentajeSpam = Math.round((spam / total) * 100);

            actualizarElementoTexto('porcentajeSpam', porcentajeSpam + '%');
            actualizarElementoTexto('porcentajeNoSpam', 100-porcentajeSpam + '%');
            console.log(`Total solicitudes: ${total}`);
            console.log(`Spam: ${spam} (${porcentajeSpam}%)`);
        }
    }
};

// ===============================================
// INICIALIZACIÓN Y EVENT LISTENERS
// ===============================================

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar todos los tabs
    EstadisticasApp.tab1.inicializar();
    EstadisticasApp.tab2.inicializar();
    EstadisticasApp.tab3.inicializar();
    // EstadisticasApp.tab4.inicializar();
    EstadisticasApp.tab5.inicializar();
});

// Event listeners para todos los dropdowns estandarizados
document.addEventListener('click', function(e) {
    // Tab 1: Colección por Provincia
    if (e.target.matches('[data-coleccion]')) {
        const coleccion = e.target.getAttribute('data-coleccion');
        console.log("NUEVA: ", coleccion)
        EstadisticasApp.tab1.actualizar(coleccion);
    }
    
    // Tab 3: Categoría por Provincia
    if (e.target.matches('[data-categoria-p]')) {
        const categoria = e.target.getAttribute('data-categoria-p');
        EstadisticasApp.tab3.actualizar(categoria);
    }
    
    // Tab 4: Categoría por Hora
    if (e.target.matches('[data-categoria-h]')) {
        const categoria = e.target.getAttribute('data-categoria-h');
        EstadisticasApp.tab4.actualizar(categoria);
    }
});

// Event listener para cambio de tabs
document.addEventListener('shown.bs.tab', function(e) {
    setTimeout(function() {
        // Redimensionar gráficos cuando se cambian las tabs
        Object.values(EstadisticasApp).forEach(tab => {
            if (tab.chart) {
                tab.chart.resize();
            }
        });
    }, 100);
});

// Redimensionar gráficos cuando cambia el tamaño de ventana
window.addEventListener('resize', function() {
    Object.values(EstadisticasApp).forEach(tab => {
        if (tab.chart) {
            tab.chart.resize();
        }
    });
});