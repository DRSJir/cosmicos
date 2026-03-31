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
}
