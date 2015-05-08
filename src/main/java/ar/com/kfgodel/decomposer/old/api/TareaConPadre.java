/**
 * 04/01/2014 16:49:25 Copyright (C) 2014 Darío L. García
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
 * Esta interfaz es una marca para aquellas tareas que son parte de una tarea padre
 * 
 * @author D. García
 */
public interface TareaConPadre<R> {

	/**
	 * Define el padre de esta tarea
	 */
	public abstract void setTareaPadre(final TareaParticionable<R> tareaPadre);

	/**
	 * Devuelve la tarea padre de esta instancia
	 */
	public abstract TareaParticionable<R> getTareaPadre();

}
