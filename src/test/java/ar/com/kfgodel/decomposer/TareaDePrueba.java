/**
 * 22/12/2013 19:09:57 Copyright (C) 2013 Darío L. García
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
package ar.com.kfgodel.decomposer;

import ar.com.kfgodel.decomposer.old.api.ProcesadorDeTareasParticionables;
import ar.com.kfgodel.decomposer.old.api.TareaParticionable;
import ar.com.kfgodel.decomposer.old.impl.TareaParticionableSupport;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es una tarea de prueba para evaluar el orden de ejecucion, registrando en una lista
 * cuando se ejecuta
 * 
 * @author D. García
 */
public class TareaDePrueba extends TareaParticionableSupport<List<Integer>> {

	private final List<TareaDePrueba> requisitos = new ArrayList<>();

	private List<TareaParticionable> tareasEjecutadas;

	@Override
	public void ejecutarCon(final ProcesadorDeTareasParticionables procesador) {
		// Si tenemos requisitos tenemos que procesarlos antes
		if (!requisitos.isEmpty()) {
			procesador.procesarRequisitosYContinuarConActual(requisitos);
			requisitos.clear();
			return;
		}
		// Agregamos nuestro numero para que quede el orden
        tareasEjecutadas.add(this);
	}

	public static TareaDePrueba create(final List<TareaParticionable> listaDeEjecutadas) {
		final TareaDePrueba tarea = new TareaDePrueba();
        tarea.tareasEjecutadas = listaDeEjecutadas;
		return tarea;
	}

	/**
	 * @see TareaParticionableSupport#toString()
	 */
	@Override
    @ImplementedWithStringer
	public String toString() {
		return Stringer.representationOf(this);
	}

	public void agregarRequisito(TareaDePrueba tareaDePrueba) {
		requisitos.add(tareaDePrueba);
	}
}
