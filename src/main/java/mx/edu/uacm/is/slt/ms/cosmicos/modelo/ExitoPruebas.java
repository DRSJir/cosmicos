package mx.edu.uacm.is.slt.ms.cosmicos.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExitoPruebas {//inicia clase
	
	private List<Integer> pruebasTotales = new ArrayList<>();
	private List<Integer> pruebasExitosas = new ArrayList<>();
	private List<String> etiquetas = new ArrayList<>();
	
	private final ObjectMapper mapper = new ObjectMapper();//para leer los datos desde data.json y crea el json p/el dashborn
	
	//Metodo para leer datos desde el JSON y guardarlos en listas p/usarlos en la metrica
	public void procesarDatos(InputStream datosStream) throws IOException{//inicia procesar datos
		
		pruebasTotales.clear();pruebasExitosas.clear(); etiquetas.clear();
		
		JsonNode root = mapper.readTree(datosStream);//lee el JSON(data.json) y lo convierte en un obj que Java entiende
		
		if (root.isArray()) {
			for (JsonNode nodo : root) {//lee c/obj del JSON
				if (nodo.has("totales")) {
					pruebasTotales.add(nodo.get("totales").asInt());
					pruebasExitosas.add(nodo.get("exitosas").asInt());
					etiquetas.add(nodo.get("etiqueta").asText());
				} 
			}
		}
	}// termina procesar datos
	
	public double aplicarMetrica() {//inicia aplicarMetrica	
		if(pruebasTotales.isEmpty()) return 0.0;
			 double total = pruebasTotales.stream().mapToDouble(Integer::doubleValue).sum();
			 double exitosas = pruebasExitosas.stream().mapToDouble(Integer::doubleValue).sum();
			 
			 if(total == 0) return 0.0;
			 
			 return(exitosas / total) * 100;//calcula porcentaje de exito 			 
	}//termina AplicarMetrica
	
	public String obtenerUmbral() {//inicia obtenerUmbral
		double exito = aplicarMetrica();
		
		if(exito >= 90) return "BUENO";
		if(exito >= 70) return "REGULAR";
		return "MALO";
	}//termina obtenerUmbral
	
	//Metodo para convertir mi metrica en JSON y p/que el dashboardpueda graficar
	public String prepararDatosApex() {//inicia prepararDatosApex
		try {
			ObjectNode root = mapper.createObjectNode();//root es el obj principal JSON
			
			root.put("umbral", obtenerUmbral());
			root.put("porcentaje", Math.round(aplicarMetrica() * 100.0) / 100.0);
			
			//Categorias (etiquetas) 
			ArrayNode categoriesArr = root.putArray("categories");
			etiquetas.forEach(categoriesArr::add);
			
			//Series
			ArrayNode seriesArr = root.putArray("series");
			
			ObjectNode serie = mapper.createObjectNode();
			serie.put("name", "Tasa de exito");
			
			ArrayNode data = serie.putArray("data");
			
			//calcular porcentaje por cada etiqueta
			for (int i = 0; i < pruebasTotales.size(); i++) {
				int total = pruebasTotales.get(i);
				double porcentaje = (total == 0) ? 0: (pruebasExitosas.get(i) * 100.0 / total);
				data.add(Math.round(porcentaje * 100.0) / 100.0);	
			}
			seriesArr.add(serie);
			
			return mapper.writeValueAsString(root);
			
			
		}catch(Exception e) {
			return "{\"error\": \"No se pudo generar el JSON\"}";
		}
	}//termina prepararDatosApex

}//termina clase
