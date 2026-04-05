package mx.edu.uacm.is.slt.ms.cosmicos.controlador;
import java.io.InputStream;

import io.javalin.http.Context;
import mx.edu.uacm.is.slt.ms.cosmicos.modelo.*;

public class DesviacionControlador {
	
	public static void obtenerAnalisisDesviacion(Context ctx) {
	    try {
	        InputStream is = DesviacionControlador.class.getResourceAsStream("/data.json");
	        
	        if (is == null) {
	            ctx.status(404).result("Error: El archivo de datos JSON no existe.");
	            return;
	        }

	        DesviacionEsfuerzo modelo = new DesviacionEsfuerzo();
	        modelo.procesarDatos(is);

	        ctx.contentType("application/json");
	        ctx.result(modelo.prepararDatosApex());

	    } catch (Exception e) {
	        ctx.status(500).result("Error al procesar el JSON: " + e.getMessage());
	    }
	}
	
	public static void subirYAnalizar(Context ctx) {
	    try {
	        // Obtenemos el archivo desde la petición multipart
	        var file = ctx.uploadedFile("archivo_json");
	        
	        if (file == null) {
	            ctx.status(400).result("No se seleccionó ningún archivo.");
	            return;
	        }

	        // Usamos el InputStream del archivo subido directamente en el modelo
	        DesviacionEsfuerzo modelo = new DesviacionEsfuerzo();
	        modelo.procesarDatos(file.content());

	        ctx.contentType("application/json");
	        ctx.result(modelo.prepararDatosApex());

	    } catch (Exception e) {
	        ctx.status(500).result("Error al procesar el archivo: " + e.getMessage());
	    }
	}

}
