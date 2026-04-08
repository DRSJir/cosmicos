package mx.edu.uacm.is.slt.ms.panelcalidadsoftware.controller;

import io.javalin.Javalin;
import mx.edu.uacm.is.slt.ms.panelcalidadsoftware.service.IndicadorService;

public class PruebaController {//INICIA CLASE 
	
	public static void registerRoutes(Javalin app) {//INICIA METODO
		
		app.get("/prueba", ctx -> {
			ctx.result("Ruta funcionando");
		});
		
		app.post("/registrar", ctx -> {
		    try {
		        int totales = Integer.parseInt(ctx.formParam("totales"));
		        int exitosas = Integer.parseInt(ctx.formParam("exitosas"));

		        double tasa = IndicadorService.calcularTasaExito(totales, exitosas);

		        ctx.redirect("/resultado.html?tasa=" + tasa);

		    } catch (Exception e) {
		        ctx.result("Error: verifica que los datos sean correctos");
		    }
		});
		
	}//TERMINA METODO
	

}//TERMINA CLASE
