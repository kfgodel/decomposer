/**
 * 04/01/2014 16:18:09 Copyright (C) 2014 Darío L. García
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

import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.methods.TypeMethod;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Esta clase representa una tarea procesable en pasos, que son implementados en métodos distintos.<br>
 * Cada método representa un paso de esta tarea, y al anotarlos con {@link MetodoComoSubtarea}
 * definen un orden de ejecución.<br>
 * Al ser agregada esta tarea al procesador se toman los métodos anotados y se agregan como pasos
 * para esta tarea
 * 
 * @author D. García
 */
public class TareaEnPasosTemplate<R> extends TareaEnPasosSupport<R> {

	/**
	 * @see TareaEnPasosSupport#crearPasos()
	 */
	@Override
	protected void crearPasos() {
		final Collection<TareaPorReflection<R>> subtareas = crearTareasDesdeMetodos();
		getPasos().addAll(subtareas);
	}

	/**
	 * Crea tareas desde cada método anotado como subtarea
	 * 
	 * @return Las tareas por cada metodo anotado de esta clase
	 */
	private Collection<TareaPorReflection<R>> crearTareasDesdeMetodos() {
        Iterator<TypeMethod> metodosAnotados = Diamond.of(getClass())
                .methods().all()
                .filter(typeMethod -> typeMethod.annotations().anyMatch(MetodoComoSubtarea.class::isInstance))
                .iterator();
        final SortedMap<Integer, TareaPorReflection<R>> tareasPorOrden = new TreeMap<>();
		while (metodosAnotados.hasNext()) {
			final TypeMethod metodo = metodosAnotados.next();
			final TareaPorReflection<R> subtarea = crearTareaDesdeMetodo(metodo);
			final Integer orden = subtarea.getOrden();
			// Chequeo por posible error de config
			if (tareasPorOrden.containsKey(orden)) {
				final TareaPorReflection<R> tareaPrevia = tareasPorOrden.get(orden);
				throw new IllegalStateException("Existen dos tareas [" + tareaPrevia + "," + subtarea
						+ "] con el mismo orden: " + orden);
			}
			tareasPorOrden.put(orden, subtarea);
		}
		return tareasPorOrden.values();
	}

	/**
	 * Crea la tarea por reflection desde el método indicado
	 * 
	 * @param metodo
	 *            El método que será invocado por la tarea
	 * @return La tarea creada para invocarla
	 */
	private TareaPorReflection<R> crearTareaDesdeMetodo(final TypeMethod metodo) {
		final TareaPorReflection<R> tarea = TareaPorReflection.create(metodo, this);

		final MetodoComoSubtarea annotation = (MetodoComoSubtarea) metodo.annotations()
                .filter(MetodoComoSubtarea.class::isInstance)
                .findFirst().get();
		final int orden = annotation.orden();
		tarea.setOrden(orden);
		return tarea;
	}
}
