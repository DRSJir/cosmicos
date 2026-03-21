package mx.edu.uacm.is.slt.ms.cosmicos;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class App {

	public static void main(String[] args) {
		var app = Javalin.create(config -> {
            // Esto le dice a Javalin que busque archivos en src/main/resources/public
            config.staticFiles.add("/public/inicio.html", Location.CLASSPATH);
        }).start("0.0.0.0",8080);

	}

}
