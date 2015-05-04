/**
 * 22/12/2013 18:30:38 Copyright (C) 2013 Darío L. García
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

import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;
import ar.com.kfgodel.decomposer.api.ResultadoIterativo;

/**
 * Esta clase representa el resultado que posee un valor final
 * 
 * @author D. García
 */
public class ResultadoProducido<R> implements ResultadoIterativo<R> {

	private R valorFinal;
	public static final String valorFinal_FIELD = "valorFinal";

	/**
	 * @see ResultadoIterativo#quedanIteraciones()
	 */
	@Override
	public boolean quedanIteraciones() {
		return false;
	}

	/**
	 * @see ResultadoIterativo#getValorFinal()
	 */
	@Override
	public R getValorFinal() throws IllegalStateException {
		return valorFinal;
	}

	public void cambiarValorFinal(final R nuevoValor) {
		this.valorFinal = nuevoValor;
	}

	public static <R> ResultadoProducido<R> create(final R valor) {
		final ResultadoProducido<R> resultado = new ResultadoProducido<>();
		resultado.valorFinal = valor;
		return resultado;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	@ImplementedWithStringer
	public String toString() {
		return Stringer.representationOf(this);
	}
}
