package mx.edu.uacm.is.slt.ms.cosmicos.controlador;

import io.javalin.http.Context;
import mx.edu.uacm.is.slt.ms.cosmicos.modelo.DensidadDefectos;

import java.io.InputStream;

public class DensidadDefectosControlador {
    public static void obtenerAnalisisDensidadDefectos(Context ctx) {
        try {
            InputStream is = DensidadDefectosControlador.class.getResourceAsStream("/calidad_datos.json");

            if (is == null) {
                ctx.status(404).result("Error: el archivo de datos no existe");
            }

            DensidadDefectos defectos = new DensidadDefectos();
            defectos.procesarDatos(is);

            // Configurar respuesta
            ctx.contentType("aplication/json");
            ctx.result(defectos.prepararDatosApex());

        } catch (Exception e) {
            System.out.printf("Error en métrica de densidad de defectos");
            ctx.status(500).result("Error al procesar la métrica de densidad: \"" + e.getMessage());
        }
    }

    public static void subirYAnalizar(Context ctx) {
        try {
            var archivoSubido = ctx.uploadedFile("file");

            if (archivoSubido == null) {
                ctx.status(400).result("{\"error\": \"No se subió ningún archivo\"}");
                return;
            }

            DensidadDefectos modelo = new DensidadDefectos();

            // Porcesar el contenido del archivo
            modelo.procesarDatos(archivoSubido.content());

            // Devolvemos el JSON procesado
            ctx.contentType("application/json");
            ctx.result(modelo.prepararDatosApex());

        } catch (Exception e) {
            ctx.status(500).result("{\"error\": \"Error al procesar archivo: " + e.getMessage() + "\"}");
        }
    }
}
