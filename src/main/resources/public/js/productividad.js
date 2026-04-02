async function calcular() {
    // Obtener valores del formulario
    let tareas = parseFloat(document.getElementById("tareas").value);
    let dias = parseFloat(document.getElementById("dias").value);

    if (isNaN(tareas) || tareas < 0) {
        alert("Ingresa un número válido de tareas");
        return;
    }
    if (isNaN(dias) || dias <= 0) {
        alert("Ingresa un número válido de días mayor que 0");
        return;
    }

    // Preparar datos para el backend
    let formData = new FormData();
    formData.append("tareas", tareas);
    formData.append("dias", dias);

    try {
        // Llamada al backend
        let respuesta = await fetch("/productividad", {
            method: "POST",
            body: formData
        });

        // Si hay error del backend
        if (!respuesta.ok) {
            let msg = await respuesta.text();
            alert("Error: " + msg);
            return;
        }

        let datos = await respuesta.json();

        let productividad = Math.round(datos.productividad * 10) / 10;
        document.getElementById("resultado").innerText = productividad + " tareas/día";

        let estado = document.getElementById("estado");
        estado.innerText = datos.estado;
        // Convertir estado a minúsculas para clases CSS: baja, normal, alta
        estado.className = "status-badge status-" + datos.estado.toLowerCase();

        document.getElementById("gaugeValue").innerText = productividad;

        let max = 10; // máximo del velocímetro
        let porcentaje = productividad / max;
        if (porcentaje > 1) porcentaje = 1;

        let grados = -90 + (porcentaje * 180);
        document.getElementById("needle").style.transform = "rotate(" + grados + "deg)";

    } catch (error) {
        console.error(error);
        alert("Ocurrió un error al calcular la productividad. Intenta de nuevo.");
    }
}