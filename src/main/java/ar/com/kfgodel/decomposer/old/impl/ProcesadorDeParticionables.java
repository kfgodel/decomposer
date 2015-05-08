/**
 * 22/12/2013 17:52:46 Copyright (C) 2013 Darío L. García
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
import ar.com.kfgodel.decomposer.old.api.ProcesadorDeTareasParticionables;
import ar.com.kfgodel.decomposer.old.api.ResultadoIterativo;
import ar.com.kfgodel.decomposer.old.api.TareaParticionable;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Esta clase es la implementación del procesador de tareas particionables.<br>
 * Esta instancia no es thread-safe. Cada thread debe crear su procesador
 * 
 * @author D. García
 */
public class ProcesadorDeParticionables implements ProcesadorDeTareasParticionables {
	private final static Logger LOG = LoggerFactory.getLogger(ProcesadorDeParticionables.class);

	private TareaParticionable<?> tareaActual;
	public static final String tareaActual_FIELD = "tareaActual";

	private ColaDeTareas pendientes;
	public static final String pendientes_FIELD = "pendientes";

	/**
	 * @see ProcesadorDeTareasParticionables#procesar(TareaParticionable)
	 */
	@Override
	public <R> ResultadoIterativo<R> procesar(final TareaParticionable<R> tarea) throws DependenciaCircularException {
		LOG.debug("Starting to process task[{}] in processor[{}}", tarea, this);
		prepararComoProxima(tarea);

		this.procesarPendientes();

		final ResultadoIterativo<R> resultadoProcesado = tarea.getResultado();

		LOG.debug("Finished with result[{}] task[{}]", resultadoProcesado, tarea);
		return resultadoProcesado;
	}

	/**
	 * Agrega la tarea a la cola de pendientes, preparándola para ser procesada en cualquier momento
	 * 
	 * @param tarea
	 *            La tarea a procesar
	 */
	private <R> void prepararComoProxima(final TareaParticionable<R> tarea) {
		LOG.trace("Preparing resources for task[{}]", tarea);
        tarea.prepararRecursos();
        agregarProxima(tarea);
	}

    private <R> void agregarProxima(TareaParticionable<R> tarea) {
        pendientes.encolarProxima(tarea);
    }

    /**
	 * Procesa todas las tareas pendientes en el orden que están, agregando nuevas a medida que van
	 * surgiendo
	 */
	private void procesarPendientes() throws DependenciaCircularException {
		while (pendientes.tieneTareas()) {
			final TareaParticionable<?> tareaAprocesar = pendientes.sacarProxima();
			procesarPendiente(tareaAprocesar);
		}
	}

	/**
	 * Procesa la proxima tarea pendiente quitandola de la lista, y procesando sus consecuencias.<br>
	 * Si la tarea requiere varias iteraciones se vuelve a agregar como pendiente
	 */
	private void procesarPendiente(final TareaParticionable<?> tareaProcesada) {
		// La registramos como actual para los metodos que usan actual como implicito
		tareaActual = tareaProcesada;

        LOG.debug("Running task[{}]", tareaActual);
		// La ejecutamos completamente (puede auto encolarse)
		tareaActual.ejecutarCon(this);

		// Si ya no está como pendiente, entonces terminamos con la tarea
		if (!pendientes.contieneA(tareaActual)) {
            LOG.debug("Completed task[{}]. Releasing resources", tareaActual);
			tareaProcesada.liberarRecursos();
		}else{
            LOG.debug("Unfinished task[{}]. Waiting for sub-tasks", tareaProcesada);
        }
		// Si esta como pendiente se liberará en otra vuelta
		tareaActual = null;
	}

    private void posponerActual() {
        agregarProxima(tareaActual);
    }

    public static ProcesadorDeParticionables create() {
		final ProcesadorDeParticionables procesador = new ProcesadorDeParticionables();
		procesador.pendientes = ColaDeTareas.create();
		return procesador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	@ImplementedWithStringer
	public String toString() {
		return Stringer.representationOf(this);
	}

	/**
	 * @see ProcesadorDeTareasParticionables#procesarRequisitosYContinuarConActual(java.util.List)
	 */
	@Override
	public void procesarRequisitosYContinuarConActual(final List<? extends TareaParticionable<?>> requisitos) {
		LOG.trace("Holding task[{}] until {} more done", tareaActual, requisitos.size());
		// Primero ponemos la actual para cuando termine el resto
        posponerActual();

        // Recorremos desde atrás para mantener el orden
		for (int i = requisitos.size() - 1; i >= 0; i--) {
			final TareaParticionable<?> requisito = requisitos.get(i);
			prepararComoProxima(requisito);
		}
	}

	/**
	 * @see ProcesadorDeTareasParticionables#procesarRequisitoYContinuarConActual(TareaParticionable)
	 */
	@Override
	public void procesarRequisitoYContinuarConActual(final TareaParticionable<?> requisito) {
		// Invocamos como lista que sería el caso general de este método
		@SuppressWarnings("unchecked")
		final List<? extends TareaParticionable<?>> comoLista = Lists.newArrayList(requisito);
		procesarRequisitosYContinuarConActual(comoLista);
	}

}
