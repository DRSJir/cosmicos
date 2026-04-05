// 1. Cargar la barra lateral dinámicamente
fetch('/barraLateral.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('sidebar-container').innerHTML = data;
    });
	
let chartInstance = null;

function renderizarGrafica(data) {
    const badge = document.getElementById('status-badge');
    badge.innerText = `${data.umbral} (${data.porcentaje}%)`;
    badge.className = `status-badge status-${data.umbral}`;

    const interpretationContainer = document.getElementById('interpretation-container');
    const interpretationText = document.getElementById('interpretation-text');
    
    const descripciones = {
        'critico': 'Alerta Crítica: La desviación impacta la viabilidad.',
        'advertencia': 'Se observa una desviación moderada. Revisa el alcance.',
        'optimo': 'La eficiencia es excelente. El esfuerzo real está alineado.',
        'sobreestimado': 'El equipo terminó antes de lo previsto. Ajustar futuras estimaciones.'
    };

    const claveUmbral = data.umbral.toLowerCase();
    if (descripciones[claveUmbral]) {
        interpretationText.innerText = descripciones[claveUmbral];
        interpretationContainer.style.display = 'block';
    }

    if (chartInstance) {
        chartInstance.destroy();
    }

    const options = {
        series: data.series,
        chart: { type: 'area', height: 360, toolbar: { show: false }, foreColor: '#94a3b8' },
        colors: ['#6366f1', '#10b981'],
        stroke: { curve: 'smooth' },
        xaxis: { categories: data.categories },
        tooltip: { theme: 'dark' }
    };

    chartInstance = new ApexCharts(document.querySelector("#main-chart"), options);
    chartInstance.render();
}

document.getElementById('upload-json').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("archivo_json", file);

    fetch('/api/subir-desviacion', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        renderizarGrafica(data);
        alert("¡Datos actualizados desde el archivo!");
    })
    .catch(err => alert("Error al subir el archivo: " + err));
});


fetch('/api/desviacion')
    .then(res => res.json())
    .then(data => renderizarGrafica(data));
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	