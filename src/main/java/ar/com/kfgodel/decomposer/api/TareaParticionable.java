/**
 * 22/12/2013 17:18:53 Copyright (C) 2013 Darío L. García
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
package ar.com.kfgodel.decomposer.api;


/**
 * Esta interfaz representa una tarea que puede procesarse en partes.<br>
 * Las partes que deben procesarse antes de esta tarea se llaman 'requisitos' las que deben
 * procesarse después se llaman 'consecuencias'.<br>
 * Un {@link ProcesadorDeTareasParticionables} puede procesar el todo y las partes en el orden que
 * corresponden.<br>
 * <br>
 * Si existe alguna forma de identificación de las tareas, debe implementarse el {@link #hashCode()}
 * y {@link #equals(Object)} acorde para que el procesador pueda identificar tareas duplicadas.
 * 
 * @author D. García
 */
public interface TareaParticionable<R> {

	/**
	 * Ejecuta esta tarea procesando la semántica propia de esta instancia.<br>
	 * El procesador pasado permite diferir la ejecución de esta tarea o agregar requisitos previos
	 * 
	 * @param procesador
	 *            El procesador que esta procesando la tarea actualmente
	 */
	void ejecutarCon(ProcesadorDeTareasParticionables procesador);

	/**
	 * Devuelve el resultado disponible en esta tarea.<br>
	 * Si el resultado indica que quedan iteraciones, esta instancia es puesta al final de las
	 * tareas pendientes para ser procesada nuevamente cuando termine con el resto de las pendientes
	 * 
	 * @return El resultado de esta tarea al momento
	 */
	ResultadoIterativo<R> getResultado();

	/**
	 * Invocado por el procesador cuando esta tarea se agrega a la lista de tareas pendientes.<br>
	 * A partir de este punto la tarea puede ser procesada
	 */
	void prepararRecursos();

	/**
	 * Invocado por el procesador cuando esta tarea indica que ya completó el resultado y no
	 * requiere más iteraciones.<br>
	 * La tarea no será procesada nuevamente, a menos que otra tarea la agregue neuvamente como
	 * pendiente
	 */
	void liberarRecursos();
}
