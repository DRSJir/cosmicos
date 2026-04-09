package mx.edu.uacm.is.slt.ms.cosmicos.controlador;

import java.io.InputStream;
import io.javalin.http.Context;
import mx.edu.uacm.is.slt.ms.cosmicos.modelo.*;

public class ExitoPruebasControlador {//inicia clase
	
	public static void obtenerAnalisisExito(Context ctx) {
		try {
			InputStream is = ExitoPruebasControlador.class.getResourceAsStream("/data_exito.json");
			
			if(is == null) {
				ctx.status(400).result("Error: El archivo de datos JSON no existe.");
				return;
			}
			
			ExitoPruebas modelo = new ExitoPruebas();
			modelo.procesarDatos(is);
			
			ctx.contentType("application/json");
			ctx.result(modelo.prepararDatosApex());
			
		} catch (Exception e ){
			ctx.status(500).result("Error al procesar el JSON: " + e.getMessage());
		}
	}
}//termina  clase
