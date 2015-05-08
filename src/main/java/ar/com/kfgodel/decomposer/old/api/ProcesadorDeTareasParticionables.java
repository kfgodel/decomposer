/**
 * 22/12/2013 17:17:18 Copyright (C) 2013 Darío L. García
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

import java.util.List;

/**
 * Esta interfaz representa un procesador de tareas que son particionables en sub tareas<br>
 * Instancias de este tipo pueden procesar las tareas en partes
 * 
 * @author D. García
 */
public interface ProcesadorDeTareasParticionables {

	/**
	 * Procesa la tarea pasada y sus subtareas en forma ordenada (según la spec de
	 * {@link TareaParticionable}) hasta que no queden tareas pendientes por procesar, y devuelve el
	 * resultado de la tarea pasada.<br>
	 * Este método no es utilizable por una tarea en proceso.<br>
	 * Para procesar subtareas desde una tarea en proceso deben utilizarse los otros métodos para
	 * agregar subtareas de esta instancia.<br>
	 * El resultado devuelto podría estar incompleto si se ejecutan todas las tareas pendientes y la
	 * tarea no genera un valor final
	 * 
	 * @param tarea
	 *            La tarea a procesar
	 * @return El resultado final de la tarea indicada
	 * @throws DependenciaCircularException
	 *             si una tarea dependen de si misma indirectamente
	 */
	<R> ResultadoIterativo<R> procesar(TareaParticionable<R> tarea) throws DependenciaCircularException;

	/**
	 * Encola la tarea actual para ser ejecutada nuevamente, luego de procesar las tareas pasadas.<br>
	 * Este procesador encolará las tareas de manera que ejecuta las tareas pasadas como requisitos
	 * antes y luego rejecuta la actual.<br>
	 * Normalmente este es el ultimo método invocado por la tarea actual, que sabe como continuar
	 * desde donde suspendió al ejecutarse la próxima vez
	 * 
	 * @param requisitos
	 *            Tareas que deben procesarse antes de continuar con la actual
	 */
	void procesarRequisitosYContinuarConActual(List<? extends TareaParticionable<?>> requisitos);

	/**
	 * Encola la tarea actual para ser ejecutada nuevamente, luego de procesar la tarea pasada.<br>
	 * Este procesador encolará las tareas de manera que ejecuta la tarea pasada como requisito
	 * antes y luego rejecuta la actual.<br>
	 * Normalmente este es el ultimo método invocado por la tarea actual, que sabe como continuar
	 * desde donde suspendió al ejecutarse la próxima vez
	 * 
	 * @param requisito
	 *            La tarea que debe procesarse antes de continuar con la actual
	 */
	void procesarRequisitoYContinuarConActual(TareaParticionable<?> requisito);

	/**
	 * Agrega las dos tareas pasadas como pendientes, poniendo primero la subtarea para procesar y
	 * luego la tarea.<br>
	 * Este método permite a una tarea en proceso suspender su ejecución hasta terminar con una
	 * subtarea
	 * 
	 * @param subtarea
	 *            La tarea que será ejecutada inmediatamente al terminar la tarea en proceso
	 * @param tarea
	 *            La tarea que será procesada después de la subtarea pero antes que otras tareas
	 *            pendietes
	 */
	// void encolarRequisitoDeActual(TareaParticionable<?> subtarea, TareaParticionable<?> tarea);

	/**
	 * Agrega todas las tareas pasadas para ser ejecutadas apenas termine de procesa la tarea
	 * actual, poniendo primero las subtareas y luego la tarea pasada
	 * 
	 * @param subtareas
	 *            Las tareas que serán ejecutadas antes que otras tareas
	 * @param tarea
	 *            La tarea que será ejecutada al final, pero antes que el resto de las tareas
	 *            pendientes
	 */
	// void encolarRequisitosDeActual(List<? extends TareaParticionable<?>> subtareas,
	// TareaParticionable<?> tarea);

	/**
	 * Agrega la tarea pasada como pendiente para ser procesada apenas termine la tarea actual
	 * 
	 * @param subtarea
	 *            La tarea a procesar
	 */
	// void encolarPrimera(TareaParticionable<?> subtarea);

	/**
	 * Agrega las tareas en orden para ser procesadas inmediatamente después de terminar la tarea
	 * actual y antes del resto de las tareas pendientes
	 * 
	 * @param subtareas
	 *            Las tareas a agregar como pendientes
	 */
	// void encolarPrimeras(List<? extends TareaParticionable<?>> subtareas);

	/**
	 * Agrega la tarea indicada como pendiente para ser procesada después que las tareas pendientes
	 * actuales
	 * 
	 * @param tarea
	 *            La tarea a procesar despues que el resto
	 */
	// void encolarUltima(TareaParticionable<?> tarea);

	/**
	 * Agrega las tareas indicadas para ser procesadas en orden al final del resto de las tareas
	 * pendientes
	 * 
	 * @param tareas
	 *            Las tareas a procesar ultimas
	 */
	// void encolarUltimas(List<? extends TareaParticionable<?>> tareas);

}
