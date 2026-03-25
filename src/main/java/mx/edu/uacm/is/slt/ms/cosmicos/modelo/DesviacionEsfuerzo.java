package mx.edu.uacm.is.slt.ms.cosmicos.modelo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DesviacionEsfuerzo {
    private List<String> etiquetas = new ArrayList<>();
    private List<Double> estimados = new ArrayList<>();
    private List<Double> reales = new ArrayList<>();
    
    private final ObjectMapper mapper = new ObjectMapper();

    public void procesarDatos(InputStream datosStream) throws IOException {
        etiquetas.clear(); estimados.clear(); reales.clear();

        JsonNode root = mapper.readTree(datosStream);

        if (root.isArray()) {
            for (JsonNode nodo : root) {
                if (nodo.has("etiqueta")) {
                    etiquetas.add(nodo.get("etiqueta").asText());
                    estimados.add(nodo.get("estimado").asDouble());
                    reales.add(nodo.get("real").asDouble());
                }
            }
        }
    }

    public double aplicarMetrica() {
        if (estimados.isEmpty()) return 0.0;
        double sumaEstimado = estimados.stream().mapToDouble(Double::doubleValue).sum();
        double sumaReal = reales.stream().mapToDouble(Double::doubleValue).sum();
        return ((sumaReal - sumaEstimado) / sumaEstimado) * 100;
    }

    public String obtenerUmbral() {
        double desviacion = aplicarMetrica();
        if (desviacion > 15) return "CRITICO";
        if (desviacion > 5) return "ADVERTENCIA";
        if (desviacion < -5) return "SUBESTIMADO";
        return "OPTIMO";
    }


    public String prepararDatosApex() {
        try {
            // Creamos el objeto principal
            ObjectNode root = mapper.createObjectNode();
            
            root.put("umbral", obtenerUmbral());
            root.put("porcentaje", Math.round(aplicarMetrica() * 100.0) / 100.0);

            // Creamos el arreglo de categorías 
            ArrayNode categoriesArr = root.putArray("categories");
            etiquetas.forEach(categoriesArr::add);

            // Creamos el arreglo de series
            ArrayNode seriesArr = root.putArray("series");

            // Serie Estimado
            ObjectNode serieEstimado = mapper.createObjectNode();
            serieEstimado.put("name", "Estimado");
            ArrayNode dataEstimado = serieEstimado.putArray("data");
            estimados.forEach(dataEstimado::add);
            seriesArr.add(serieEstimado);

            // Serie Real
            ObjectNode serieReal = mapper.createObjectNode();
            serieReal.put("name", "Real");
            ArrayNode dataReal = serieReal.putArray("data");
            reales.forEach(dataReal::add);
            seriesArr.add(serieReal);

            // Convertimos el objeto completo a String JSON
            return mapper.writeValueAsString(root);
            
        } catch (Exception e) {
            return "{\"error\": \"No se pudo generar el JSON\"}";
        }
    }
}