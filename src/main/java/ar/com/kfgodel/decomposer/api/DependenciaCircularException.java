/**
 * 22/12/2013 18:05:44 Copyright (C) 2013 Darío L. García
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
 * Esta clase representa la excepción producida por el procesador de tareas particionables al
 * detectar una dependencia circular entre tareas (una tarea depende de si misma por transitividad)
 * 
 * @author D. García
 */
public class DependenciaCircularException extends RuntimeException {
	private static final long serialVersionUID = 2435658513523507836L;

	public DependenciaCircularException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DependenciaCircularException(final String message) {
		super(message);
	}

	public DependenciaCircularException(final Throwable cause) {
		super(cause);
	}

}
