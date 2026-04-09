package mx.edu.uacm.is.slt.ms.cosmicos.modelo;

public class ProductividadEquipo {

    private int tareas;
    private int dias;

    public ProductividadEquipo(int tareas, int dias) {
        this.tareas = tareas;
        this.dias = dias;
    }

    public double aplicarMetrica() {
        if (dias == 0) return 0;
        return (double) tareas / dias;
    }

    public String obtenerEstado() {
        double productividad = aplicarMetrica();

        if (productividad < 2) {
            return "BAJA";
        } else if (productividad <= 5) {
            return "NORMAL";
        } else {
            return "ALTA";
        }
    }

    public String obtenerColor() {
        double productividad = aplicarMetrica();

        if (productividad < 2) {
            return "red";
        } else if (productividad <= 5) {
            return "yellow";
        } else {
            return "green";
        }
    }
}