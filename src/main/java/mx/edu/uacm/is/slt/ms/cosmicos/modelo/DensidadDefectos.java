package mx.edu.uacm.is.slt.ms.cosmicos.modelo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DensidadDefectos {
    private List<String> modulos = new ArrayList<>();
    private List<Integer> defectos = new ArrayList<>();
    private List<Double> tamanioKloc = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();

    public void procesarDatos(InputStream datos) throws IOException {
        modulos.clear();
        defectos.clear();
        tamanioKloc.clear();

        JsonNode raiz = mapper.readTree(datos);

        if (raiz.isArray()) {
            for(JsonNode nodo: raiz) {
                if (nodo.has("modulo")) {
                    modulos.add(nodo.get("modulo").asText());
                    defectos.add(nodo.get("defectos").asInt());
                    tamanioKloc.add(nodo.get("kloc").asDouble());
                }
            }
        }
    }

    /**
     * Calcula la densidad global: (Total de defectos / Total KLOC)
     * @return Densidad de defectos
     */
    public double aplicarMetrica() {
        double totalKloc = tamanioKloc.stream().mapToDouble(Double::intValue).sum();
        if (totalKloc == 0) return 0.0;

        int totalDefectos = defectos.stream().mapToInt(Integer::intValue).sum();
        return totalDefectos / totalKloc;
    }

    /**
     *
     * @return Estado de la calidad
     */
    public String obtenerEstadoCalidad() {
        double densidad = aplicarMetrica();
        // Umbrales de defectos
        if (densidad > 5.0) return "BAJA CALIDAD";
        if (densidad > 2.0) return "ACEPTABLE CON RIESGO";
        return "ALTA CALIDAD";
    }

    public String prepararDatosApex() {
        try {
            // 1. Siempre crear un nodo raíz limpio
            ObjectNode root = mapper.createObjectNode();

            root.put("estado", obtenerEstadoCalidad());
            root.put("densidadGlobal", Math.round(aplicarMetrica() * 100.0) / 100.0);

            // 2. Categorías (Modulos)
            ArrayNode categoriesArr = mapper.createArrayNode(); // Crear por separado
            for (String m : modulos) {
                categoriesArr.add(m);
            }
            root.set("categories", categoriesArr); // Usar set en lugar de putArray

            // 3. Series
            ArrayNode seriesArr = mapper.createArrayNode();

            // Serie Defectos
            ObjectNode sDefectos = mapper.createObjectNode();
            sDefectos.put("name", "Defectos");
            ArrayNode dDefectos = mapper.createArrayNode();
            defectos.forEach(dDefectos::add);
            sDefectos.set("data", dDefectos);
            seriesArr.add(sDefectos);

            // Serie KLOC
            ObjectNode sKloc = mapper.createObjectNode();
            sKloc.put("name", "KLOC");
            ArrayNode dKloc = mapper.createArrayNode();
            tamanioKloc.forEach(dKloc::add);
            sKloc.set("data", dKloc);
            seriesArr.add(sKloc);

            root.set("series", seriesArr);

            return mapper.writeValueAsString(root);

        } catch (Exception e) {
            e.printStackTrace(); // Importante para ver qué falló antes del StackOverflow
            return "{\"error\": \"Error de recursión en JSON\"}";
        }
    }
}
