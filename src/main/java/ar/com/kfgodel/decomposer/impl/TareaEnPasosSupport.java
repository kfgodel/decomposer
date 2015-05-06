/**
 * 31/12/2013 12:48:58 Copyright (C) 2013 Darío L. García
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

import ar.com.kfgodel.decomposer.api.ProcesadorDeTareasParticionables;
import ar.com.kfgodel.decomposer.api.ResultadoIterativo;
import ar.com.kfgodel.decomposer.api.TareaConPadre;
import ar.com.kfgodel.decomposer.api.TareaParticionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa una tarea que es procesable en pasos.<br>
 * Al ejecutarse cada vez avanza un paso
 * 
 * @author D. García
 */
public class TareaEnPasosSupport<R> extends TareaParticionableSupport<R> implements TareaConPadre<R> {
    private final static Logger LOG = LoggerFactory.getLogger(TareaEnPasosSupport.class);

	private int indiceDePasoActual;
	private TareaParticionable<R> tareaPadre;
	private List<TareaParticionable<R>> pasos;

	public List<TareaParticionable<R>> getPasos() {
		if (pasos == null) {
			pasos = new ArrayList<>();
		}
		return pasos;
	}

	/**
	 * El paso actual o null, si no hay
	 * 
	 * @return null si no hay pasos, o ya pasamos todos
	 */
	public TareaParticionable<R> getPasoActual() {
		if (pasos == null || indiceDePasoActual >= pasos.size()) {
			return null;
		}
		final TareaParticionable<R> pasoActual = pasos.get(indiceDePasoActual);
		return pasoActual;
	}

	public int getIndiceDePasoActual() {
		return indiceDePasoActual;
	}

	public void setIndiceDePasoActual(final int indiceDePasoActual) {
		this.indiceDePasoActual = indiceDePasoActual;
	}

	public void setPasos(final List<TareaParticionable<R>> pasos) {
		this.pasos = pasos;
	}

	/**
	 * @see TareaParticionableSupport#ejecutarCon(ProcesadorDeTareasParticionables)
	 */
	@Override
	public void ejecutarCon(final ProcesadorDeTareasParticionables procesador) {
		if (indiceDePasoActual >= pasos.size()) {
			throw new IllegalStateException("No puede ejecutarse esta tarea porque no quedan mas pasos");
		}
		if (indiceDePasoActual == -1) {
			indiceDePasoActual = 0;
		}
		final TareaParticionable<R> pasoActual = getPasoActual();
		LOG.debug("Running step {} of task[{}]: {}", indiceDePasoActual, this, pasoActual);
		pasoActual.ejecutarCon(procesador);
        ResultadoIterativo<R> stepResult = pasoActual.getResultado();
        LOG.trace("Using step {} result[{}] for task[{}]", indiceDePasoActual, stepResult, this);
        setResultado(stepResult);
		indiceDePasoActual++;
	}

	@Override
	public TareaParticionable<R> getTareaPadre() {
		return tareaPadre;
	}

	@Override
	public void setTareaPadre(final TareaParticionable<R> tareaPadre) {
		this.tareaPadre = tareaPadre;
	}

	/**
	 * @see TareaParticionableSupport#prepararRecursos()
	 */
	@Override
	public void prepararRecursos() {
		crearPasos();
		// Nos asguramos que las subtareas conozcan a su padre
		for (final TareaParticionable<R> paso : getPasos()) {
			if (paso instanceof TareaConPadre) {
				@SuppressWarnings("unchecked")
				final TareaConPadre<R> subtareaEnPasos = (TareaConPadre<R>) paso;
				subtareaEnPasos.setTareaPadre(this);
			}
		}
	}

	/**
	 * Crea las tareas que representa los pasos de esta tarea
	 */
	protected void crearPasos() {
		// Responsablidad de la subclase si corresponde
	}

	/**
	 * @see TareaParticionableSupport#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
		builder.append("<");
		builder.append(Integer.toHexString(this.hashCode()));
		builder.append(">");
		builder.append("{ steps: ");
		builder.append(indiceDePasoActual);
		builder.append(" of ");
		builder.append(getPasos().size());
		builder.append(", current: ");
		builder.append(getPasoActual());
		builder.append("}");
		return builder.toString();
	}
}
