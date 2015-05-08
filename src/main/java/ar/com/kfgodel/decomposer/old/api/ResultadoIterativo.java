/**
 * 22/12/2013 17:36:52 Copyright (C) 2013 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.kfgodel.decomposer.old.api;

/**
 * Esta interfaz representa el resultado de un procesamiento que puede completarse en varias
 * ejecuciones.<br>
 * Al finalizar todas las iteraciones necesarias, este resultado estará completo y podrá conocerse
 * su valor final
 * 
 * @author D. García
 */
public interface ResultadoIterativo<R> {

	/**
	 * El objeto que representa el valor final de este resultado.<br>
	 * Se produce un error si quedan iteraciones por procesar
	 * 
	 * @return El valor final
	 */
	R getValorFinal() throws IllegalStateException;
}
