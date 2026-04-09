async function calcular() {

    const boton = document.querySelector("button");
    boton.disabled = true;
    boton.innerText = "Calculando...";

    let tareas = parseFloat(document.getElementById("tareas").value);
    let dias = parseFloat(document.getElementById("dias").value);

    if (isNaN(tareas) || tareas < 0) {
        alert("Ingresa un número válido de tareas");
        boton.disabled = false;
        boton.innerText = "Calcular productividad";
        return;
    }

    if (isNaN(dias) || dias <= 0) {
        alert("Ingresa un número válido de días mayor que 0");
        boton.disabled = false;
        boton.innerText = "Calcular productividad";
        return;
    }

    let formData = new FormData();
    formData.append("tareas", tareas);
    formData.append("dias", dias);

    try {

        let respuesta = await fetch("/productividad", {
            method: "POST",
            body: formData
        });

        if (!respuesta.ok) {
            let msg = await respuesta.text();
            alert("Error: " + msg);
            throw new Error(msg);
        }

        let datos = await respuesta.json();

        let productividad = Math.round(datos.productividad * 10) / 10;

        let resultado = document.getElementById("resultado");
        resultado.innerText = productividad + " tareas por día";

        let estado = document.getElementById("estado");
        estado.innerText = datos.estado;

        // aplicar clase de estado
        estado.className = "status-badge status-" + datos.estado.toLowerCase();

        // valor del velocímetro
        document.getElementById("gaugeValue").innerText = productividad;

        let max = 10;
        let porcentaje = productividad / max;

        if (porcentaje > 1) porcentaje = 1;

        let grados = -90 + (porcentaje * 180);
        document.getElementById("needle").style.transform = `rotate(${grados}deg)`;

        /* color dinámico del resultado */

        if (datos.estado === "BAJA") {
            resultado.style.color = "#ef4444";
        } 
        else if (datos.estado === "NORMAL") {
            resultado.style.color = "#facc15";
        } 
        else {
            resultado.style.color = "#22c55e";
        }

    } catch (error) {

        console.error(error);
        alert("Ocurrió un error al calcular la productividad.");

    } finally {

        boton.disabled = false;
        boton.innerText = "Calcular productividad";

    }
}