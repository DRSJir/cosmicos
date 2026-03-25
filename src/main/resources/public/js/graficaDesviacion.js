// 1. Cargar la barra lateral dinámicamente
fetch('/barraLateral.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('sidebar-container').innerHTML = data;
    });

// 2. Cargar datos de la API y renderizar ApexCharts
fetch('/api/desviacion')
    .then(res => res.json())
    .then(data => {
        const badge = document.getElementById('status-badge');
        badge.innerText = `${data.umbral} (${data.porcentaje}%)`;
        badge.className = `status-badge status-${data.umbral}`;

        const options = {
            series: data.series,
            chart: { type: 'area', height: 360, toolbar: { show: false }, foreColor: '#94a3b8' },
            colors: ['#6366f1', '#10b981'],
            stroke: { curve: 'smooth' },
            xaxis: { categories: data.categories },
            tooltip: { theme: 'dark' }
        };

        new ApexCharts(document.querySelector("#main-chart"), options).render();
    });