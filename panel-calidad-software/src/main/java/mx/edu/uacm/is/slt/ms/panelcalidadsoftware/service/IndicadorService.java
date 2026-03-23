package mx.edu.uacm.is.slt.ms.panelcalidadsoftware.service;

public class IndicadorService {

	public static double calcularTasaExito(int totales, int exitosas) {//INICIA METODO calcularTasaExito 
		
		if (totales == 0) {
			return 0;
		}
		return (double) exitosas / totales * 100; 
		
	}//TERMINA METODO calcularTasaExito
}
