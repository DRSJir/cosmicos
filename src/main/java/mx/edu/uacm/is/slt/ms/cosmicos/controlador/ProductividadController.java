package mx.edu.uacm.is.slt.ms.cosmicos.controlador;

import io.javalin.http.Context;
import mx.edu.uacm.is.slt.ms.cosmicos.modelo.ProductividadEquipo;

public class ProductividadController {

    public static void calcular(Context ctx) {
        try {
            String tareasStr = ctx.formParam("tareas");
            String diasStr = ctx.formParam("dias");

            if (tareasStr == null || diasStr == null) {
                ctx.status(400).result("Faltan parámetros: tareas o días");
                return;
            }

            int tareas = Integer.parseInt(tareasStr);
            int dias = Integer.parseInt(diasStr);

            if (dias <= 0) {
                ctx.status(400).result("Los días deben ser mayores que 0");
                return;
            }

            ProductividadEquipo productividad = new ProductividadEquipo(tareas, dias);

            ctx.json(new Resultado(
                productividad.aplicarMetrica(),
                productividad.obtenerEstado(),
                productividad.obtenerColor()
            ));

        } catch (NumberFormatException e) {
            ctx.status(400).result("Los parámetros deben ser números enteros válidos");
        } catch (Exception e) {
            ctx.status(500).result("Hubo un error. Por favor verifica los datos e intenta otra vez");
        }
    }

    public static class Resultado {
        public double productividad;
        public String estado;
        public String color;

        public Resultado(double productividad, String estado, String color) {
            this.productividad = productividad;
            this.estado = estado;
            this.color = color;
        }
    }
}