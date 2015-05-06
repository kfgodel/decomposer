/**
 * 22/12/2013 18:44:53 Copyright (C) 2013 Darío L. García
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
import ar.com.kfgodel.decomposer.api.TareaParticionable;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;


/**
 * Esta clase define comportamiento base para que sea facil implementar tareas específicas
 * 
 * @author D. García
 */
public class TareaParticionableSupport<R> implements TareaParticionable<R> {

	private ResultadoIterativo<R> resultado = ResultadoPendiente.instancia();
	public static final String resultado_FIELD = "resultado";

	/**
	 * @see TareaParticionable#procesar()
	 */
	@Override
	public void ejecutarCon(final ProcesadorDeTareasParticionables procesador) {
		// La subclase debería definir este método si corresponde
	}

	public void setResultado(final ResultadoIterativo<R> resultado) {
		this.resultado = resultado;
	}

	/**
	 * @see TareaParticionable#getResultado()
	 */
	@Override
	public ResultadoIterativo<R> getResultado() {
		return resultado;
	}

	protected void setValorFinal(final R valor) {
		this.resultado = ResultadoProducido.create(valor);
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
	 * @see TareaParticionable#liberarRecursos()
	 */
	@Override
	public void liberarRecursos() {
		// La subclase debe liberar los recursos que use
	}

	/**
	 * @see TareaParticionable#prepararRecursos()
	 */
	@Override
	public void prepararRecursos() {
		// La subclase debe preparar los recursos que necesite
	}
}
