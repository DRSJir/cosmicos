package mx.edu.uacm.is.slt.ms.cosmicos;
import mx.edu.uacm.is.slt.ms.cosmicos.controlador.*;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class App {

	
	public static void main(String[] args) {
	    Javalin app = Javalin.create(config -> {
	        config.staticFiles.add("/public", Location.CLASSPATH);
	    }).start(8080);
	    
	    
	    app.get("/", ctx -> ctx.redirect("/desviacion.html"));
	    
	    

	    // Ruta productividad
	    app.post("/productividad", ProductividadController::calcular);

	    // Definición de la ruta usando referencia de método
	    app.get("/api/desviacion", DesviacionControlador::obtenerAnalisisDesviacion);
	    
	    app.post("/api/subir-desviacion", DesviacionControlador::subirYAnalizar);
    
      //Densidad de defectos
      app.get("/api/densidad", DensidadDefectosControlador::obtenerAnalisisDensidadDefectos);
      app.post("/api/densidad/subir", DensidadDefectosControlador::subirYAnalizar);


	    app.get("/api/exito-pruebas", ExitoPruebasControlador::obtenerAnalisisExito);
		
	}

}
