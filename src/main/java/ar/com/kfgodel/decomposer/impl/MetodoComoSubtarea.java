/**
 * 04/01/2014 16:43:03 Copyright (C) 2014 Darío L. García
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
package ar.com.kfgodel.decomposer.impl;

import java.lang.annotation.*;

/**
 * Este annotation es una marca para los métodos que representan pasos de una tarea.<br>
 * Los métodos anotados con instancias de este tipo, que extienden de la clase
 * {@link TareaEnPasosTemplate} son autométicamente convertidos en sub tareas para ser ejecutadas
 * cada vez que se ejecuta la tarea contenedora.<br>
 * Ejecutando un paso a la vez, según el orden indicado en este annotation.<br>
 * Si hay más de un método con el mismo orden de ejecución, se produce un error.<br>
 * <br>
 * El método indica su retorno, por valor, o devolviendo una instancia de ResultadoIterativo
 * 
 * @author D. García
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MetodoComoSubtarea {

	/**
	 * Indica el orden relativo de ejecución del método respecto de las otras tareas.<br>
	 * Debe ser un entero unico, no es necesario que sea consecutivo.<br>
	 * Las tareas se ejecutaran en orden empezando por las que tengan menor valor en este atributo
	 * 
	 * @return El numero que indica el orden de ejecución, o el numero de paso
	 */
	int orden();
}
