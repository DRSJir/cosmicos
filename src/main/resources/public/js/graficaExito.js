
let chart; //Variable global, guarda la grafica para usarla despues

document.addEventListener("DOMContentLoaded", function (){
	
	//Aqui se le el JSON externo
	document.getElementById("inputJson").addEventListener("change", function(event) {
		
		//Input JSON
	    const archivo = event.target.files[0];
	    if (!archivo) return;

	    const reader = new FileReader();

	    reader.onload = function(e) {
	        const contenido = e.target.result;
			const data = JSON.parse(contenido);

			// Transformar tu JSON
			const categorias = data.map(item => item.etiqueta);
			const valores = data.map(item => (item.exitosas / item.totales) * 100);

			// Calcular promedio
			const porcentaje = Math.round(
			    valores.reduce((a, b) => a + b, 0) / valores.length
			);

			// Actualizar indicador
			let texto = "";
			let color = "";

			if (porcentaje >= 70) {
			    texto = "Bueno";
			    color = "#28a745";
			} else if (porcentaje >= 40) {
			    texto = "Riesgo";
			    color = "#ffc107";
			} else {
			    texto = "Bajo";
			    color = "#dc3545";
			}

			const indicador = document.getElementById("indicador");
			indicador.innerHTML = `Tasa de exito general: ${porcentaje}% - ${texto}`;
			indicador.style.color = color;

			// Actualizar gráfica
			chart.updateOptions({
			    xaxis: {
			        categories: categorias
			    },
			    series: [{
			        data: valores
			    }],
			    colors: valores.map(valor => {
			        if (valor >= 70) return '#28a745'; // Verde
			        if (valor >= 40) return '#ffc107'; // Amarillo
			        return '#dc3545'; // Rojo
			    })
			});
	    };

	    reader.readAsText(archivo);
	});
	
	//Permite decargar la grafica en formato PNG
	document.getElementById("btnDescargar").addEventListener("click", function() {
	    chart.dataURI().then(({ imgURI }) => {
	        const a = document.createElement("a");
	        a.href = imgURI;
	        a.download = "grafica_exito.png";
	        a.click();
	    });
	});
	
	fetch("/api/exito-pruebas")
	.then(response => response.json())
	.then(data => {
		
		//Datos del JSON
		const categorias = data.categories;
		const valores = data.series[0].data;
		
		//indicador de tasa de exito general
		const porcentaje = data.porcentaje;

		let texto = "";
		let color = "";

		if (porcentaje >= 70) {
		    texto = "Bueno";
		    color = "#28a745";
		} else if (porcentaje >= 40) {
		    texto = "Riesgo";
		    color = "#ffc107";
		} else {
		    texto = "Bajo";
		    color = "#dc3545";
		}

		const indicador = document.getElementById("indicador");
		indicador.innerHTML = `Tasa de exito general: ${porcentaje}% - ${texto}`;
		indicador.style.color = color;
		
		//Configuracion de la grafica
		var options = {
			chart: {
				type: 'bar',
				height: 450,
				foreColor: '#fff',
				toolbar: {
					show: false,
				}
			},
			theme:{
				mode: 'dark'
			},
			tooltip: {
				enabled: true,
				theme:'dark',
				y:{
					formatter: function(val){
						return val + "% de exito";
					}
				}
			},
			series: [{
				name: 'Tasa de exito',
				data: valores
			}],
			xaxis:{
				categories: categorias,
				labels:{
					show: true,
					rotate: -45,
					hideOverlappingLabels: false,
					trim: false,
					style: {
						colors: '#fff',
						fontSize: '14px'
					}
				}
			},
			yaxis: {
			    labels: {
			        style: {
			            colors: '#fff',
			            fontSize: '14px'
			        }
			    }
			},
			plotOptions:{
				bar: {
					distributed: true
				}
			},
			colors: valores.map(valor => {
				if (valor >= 70) return '#28a745'; //Verde = Bueno
				if (valor >= 40) return '#ffc107'; //Amarillo = Riesgo
				return '#dc3545'; //Rojo = Bajo
			}),
			legend: {
				show: false
			},
			title:{			
				text:'Tasa de exito por categoria',
				style:{
					color: '#fff'
				}
			}
		};
		
		chart = new ApexCharts(document.querySelector("#main-chart"), options);
		chart.render();
	})
	.catch(error => {
		console.error("Error:", error);
	});
});