/**
 * 04/01/2014 23:08:01 Copyright (C) 2014 Darío L. García
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
package ar.com.kfgodel.decomposer.old.impl;

import ar.com.kfgodel.decomposer.old.api.DependenciaCircularException;
import ar.com.kfgodel.decomposer.old.api.TareaParticionable;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Esta clase representa la cola de tareas pendientes del procesador
 * 
 * @author D. García
 */
public class ColaDeTareas {
	private final static Logger LOG = LoggerFactory.getLogger(ColaDeTareas.class);

	private Deque<TareaParticionable<?>> tareas;
	public static final String tareas_FIELD = "tareas";

	public void encolarProxima(final TareaParticionable<?> nuevaTarea) {
		verificarQueNoEsteComoPendiente(nuevaTarea);
		agregarAlInicio(nuevaTarea);
	}

	/**
	 * Verifica que la tarea pasada no esté presenta como pendiente ya en la lista de tareas
	 * pendientes
	 * 
	 * @param tareaAProcesar
	 *            La tarea a ejecutar
	 */
	private void verificarQueNoEsteComoPendiente(final TareaParticionable<?> tareaAProcesar)
			throws DependenciaCircularException {
		if (tareas.contains(tareaAProcesar)) {
			throw new DependenciaCircularException("Se detecto una dependencia circular. La tarea[" + tareaAProcesar
					+ "] ya se encuentra pendiente");
		}
	}

	/**
	 * Agrega la tarea pasada como pendiente para ser ejeuctada inmediatamente apenas se libere el
	 * procesador
	 * 
	 * @param tareaAProcesar
	 *            La tarea a encolar
	 */
	private void agregarAlInicio(final TareaParticionable<?> tareaAProcesar) {
		LOG.trace("Enqueuing before {} others, task[{}]", this.getSize(), tareaAProcesar);
		tareas.addFirst(tareaAProcesar);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	@ImplementedWithStringer
	public String toString() {
		return Stringer.representationOf(this);
	}

	public static ColaDeTareas create() {
		final ColaDeTareas cola = new ColaDeTareas();
		cola.tareas = new LinkedList<>();
		return cola;
	}

	/**
	 * Indica si quedan tareas pendientes en esta cola
	 * 
	 * @return true si aun quedan tareas por procesar
	 */
	public boolean tieneTareas() {
		return !tareas.isEmpty();
	}

	/**
	 * Quita la proxima tarea pendiente de esta cola
	 * 
	 * @return La tarea quitada del inicio
	 */
	public TareaParticionable<?> sacarProxima() {
        TareaParticionable<?> tarea = tareas.pop();
        LOG.trace("Dequeued before {} others, task[{}]", this.getSize(), tarea);
        return tarea;
	}

	/**
	 * Indica si esta cola contiene a la tarea pasada como pendiente
	 * 
	 * @param tareaActual
	 *            La tarea a controlar
	 * @return true si la tarea indicada está presente en esta cola
	 */
	public boolean contieneA(final TareaParticionable<?> tareaActual) {
		return tareas.contains(tareaActual);
	}

	/**
	 * La cantidad de tareas encoladas
	 */
	public int getSize() {
		return tareas.size();
	}
}
