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

}
