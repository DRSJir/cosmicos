package mx.edu.uacm.is.slt.ms.panelcalidadsoftware;

import io.javalin.Javalin;
import mx.edu.uacm.is.slt.ms.panelcalidadsoftware.controller.PruebaController;

public class Main {

    public static void main(String[] args) {
    	
    	 Javalin app = Javalin.create(confing -> {
    		 confing.staticFiles.add("/static");
    	 }).start(7000);

         app.get("/", ctx -> {//INICIAN  RUTAS
         ctx.redirect("/index.html");
         });//TERMINAN RUTAS
         
         PruebaController.registerRoutes(app);//Para que Javalin conozca la ruta
    }

}