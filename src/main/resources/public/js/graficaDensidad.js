// 2. Simulación de carga de sidebar (Asegúrate de llamar a la función aquí)
fetch('/barraLateral.html') // O como sea que estés cargando tu sidebar
    .then(response => response.text())
    .then(data => {
        document.getElementById('sidebar-container').innerHTML = data;
        
        resaltarEnlaceActivo();
    });

// 1. Cargar componentes compartidos
fetch('/barraLateral.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('sidebar-container').innerHTML = data;
    });

// 2. Consumir API y Renderizar
fetch('/api/densidad')
    .then(res => res.json())
    .then(data => {
        if (data.error) throw new Error(data.error);

        renderizarGrafica(data);
        actualizarInterfaz(data);
    })
    .catch(error => {
        console.error('Error en la carga de métricas:', error);
        document.getElementById('status-badge').innerText = "Error de Datos";
    });

function renderizarGrafica(apiData) {
    const options = {
        // Configuramos series mixtas: Barras para Defectos, Línea para KLOC
        series: [
            {
                name: 'Defectos Detectados',
                type: 'column',
                data: apiData.series[0].data // Datos de defectos
            },
            {
                name: 'Tamaño del Módulo (KLOC)',
                type: 'line',
                data: apiData.series[1].data // Datos de KLOC
            }
        ],
        chart: {
            height: 380,
            type: 'line', // El contenedor debe ser 'line' para soportar tipos mixtos
            stacked: false,
            toolbar: { show: false },
            fontFamily: 'inherit',
            foreColor: '#94a3b8' // Slate 400
        },
        stroke: {
            width: [0, 4], // 0 para la columna (sin borde), 4px para la línea
            curve: 'smooth'
        },
        plotOptions: {
            bar: {
                columnWidth: '45%',
                borderRadius: 4
            }
        },
        colors: ['#6366f1', '#10b981'], // Indigo y Emerald
        fill: {
            opacity: [0.85, 1], // Un poco de transparencia en las barras
            gradient: {
                inverseColors: false,
                shade: 'light',
                type: "vertical",
                opacityFrom: 0.85,
                opacityTo: 0.55,
                stops: [0, 100, 100, 100]
            }
        },
        labels: apiData.categories,
        markers: {
            size: 5,
            colors: ['#10b981'],
            strokeColors: '#fff',
            strokeWidth: 2
        },
        // --- DOBLE EJE Y ---
        yaxis: [
            {
                title: {
                    text: 'Conteo de Defectos',
                    style: { color: '#6366f1', fontWeight: 500 }
                },
                labels: {
                    style: { colors: '#6366f1' }
                }
            },
            {
                opposite: true, // Eje derecho
                title: {
                    text: 'Miles de Líneas (KLOC)',
                    style: { color: '#10b981', fontWeight: 500 }
                },
                labels: {
                    style: { colors: '#10b981' },
                    formatter: (val) => val.toFixed(2)
                }
            }
        ],
        xaxis: {
            type: 'category',
            axisBorder: { show: false },
            axisTicks: { show: false }
        },
        grid: {
            borderColor: '#334155', // Slate 700
            strokeDashArray: 4,
            padding: { bottom: 10 }
        },
        tooltip: {
            theme: 'dark',
            shared: true,
            intersect: false,
            y: {
                formatter: function (y) {
                    if (typeof y !== "undefined") {
                        return y.toFixed(1) + (y > 10 ? " unidades" : " kloc");
                    }
                    return y;
                }
            }
        },
        legend: {
            position: 'top',
            horizontalAlign: 'right',
            offsetY: -10
        }
    };

    const chart = new ApexCharts(document.querySelector("#density-chart"), options);
    chart.render();
}

function actualizarInterfaz(data) {
    const sBadge = document.getElementById('status-badge');
    const pBadge = document.getElementById('promedio-badge');

    // Actualizar texto del promedio
    pBadge.innerHTML = `<i class="bi bi-calculator me-1"></i> Promedio: ${data.densidadGlobal}`;

    // Colores
    let colorClass = "text-bg-secondary"; // Color por defecto
    let icono = '<i class="bi bi-info-circle-fill me-1"></i> ';

    const estado = data.estado.trim();

    if (estado === "ALTA CALIDAD") {
        colorClass = "text-bg-success";
        icono = '<i class="bi bi-check-circle-fill me-1"></i> ';
    } else if (estado === "ACEPTABLE CON RIESGO") {
        colorClass = "text-bg-warning";
        icono = '<i class="bi bi-exclamation-triangle-fill me-1"></i> ';
    } else if (estado === "BAJA CALIDAD") {
        colorClass = "text-bg-danger";
        icono = '<i class="bi bi-x-octagon-fill me-1"></i> ';
    }

    sBadge.className = `badge rounded-pill ${colorClass} p-2 px-3 shadow-sm`;
    sBadge.innerHTML = icono + estado;
}
