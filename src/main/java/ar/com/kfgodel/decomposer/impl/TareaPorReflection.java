/**
 * 04/01/2014 16:37:43 Copyright (C) 2014 Darío L. García
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
import ar.com.kfgodel.diamond.api.exceptions.DiamondException;
import ar.com.kfgodel.diamond.api.methods.TypeMethod;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;
import ar.com.kfgodel.decomposer.api.ProcesadorDeTareasParticionables;
import ar.com.kfgodel.decomposer.api.ResultadoIterativo;
import ar.com.kfgodel.decomposer.api.TareaConPadre;
import ar.com.kfgodel.decomposer.api.TareaParticionable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Esta clase representa una tarea que se resuelve ejecutando un método indicado por nombre.<br>
 * El método debe aceptar un {@link ProcesadorDeParticionables} como argumento
 * 
 * @author D. García
 */
public class TareaPorReflection<R> extends TareaParticionableSupport<R> implements TareaConPadre<R> {

	private Integer orden;
	public static final String orden_FIELD = "orden";

	private TypeMethod metodo;
	public static final String metodo_FIELD = "metodo";

	private Object instancia;
	private TareaParticionable<R> tareaPadre;

	/**
	 * Devuelve el número de orden de ejecución de esta tarea si es que existe uno
	 */
	public Integer getOrden() {
		return orden;
	}

	public void setOrden(final Integer orden) {
		this.orden = orden;
	}

	/**
	 * @see TareaParticionable#ejecutarCon(ProcesadorDeTareasParticionables)
	 */
	@Override
	public void ejecutarCon(final ProcesadorDeTareasParticionables procesador) {
		final Object retornadoPorMetodo = invocarMetodo(procesador);
		if (retornadoPorMetodo instanceof ResultadoIterativo) {
			// Nos indican el resultado directamente
			@SuppressWarnings("unchecked")
			final ResultadoIterativo<R> resultadoDirecto = (ResultadoIterativo<R>) retornadoPorMetodo;
			setResultado(resultadoDirecto);
		} else {
			// Nos indican resultado por valor
			@SuppressWarnings("unchecked")
			final R valorRetornado = (R) retornadoPorMetodo;
			setValorFinal(valorRetornado);
		}
	}

	/**
	 * @param procesador
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private R invocarMetodo(final ProcesadorDeTareasParticionables procesador) {
		final Object[] argumentos = adaptarArgumentosSegunFirmaDeMetodo(procesador);
		try{
            final Object retorno = metodo.invokeOn(instancia, argumentos);
            return (R) retorno;
        }catch (DiamondException e){
            throw new RuntimeException("Failed to execute method as task", e);
        }
	}

	/**
	 * Crea los argumentos necesarios segun la firma
	 * 
	 * @param procesador
	 *            El procesador para incluir en argumentos
	 * 
	 * @return El array de argumentos
	 */
	private Object[] adaptarArgumentosSegunFirmaDeMetodo(final ProcesadorDeTareasParticionables procesador) {
		final List<Object> createdArguments = new ArrayList<>();

		final Iterator<TypeInstance> expectedArgumentTypes = metodo.parameterTypes().iterator();
        while(expectedArgumentTypes.hasNext()){
            TypeInstance expectedArgumentType = expectedArgumentTypes.next();
            if (expectedArgumentType.isAssignableTo(Diamond.of(ProcesadorDeTareasParticionables.class))) {
                // Si quieren un procesador usamos el que nos llama
                createdArguments.add(procesador);
            } else if (expectedArgumentType.isAssignableTo(Diamond.of(TareaPorReflection.class))) {
                // Si quieren la referencia a la tarea real nos pasamos
                createdArguments.add(this);
            } else {
                // Por cualquier otra cosa que quieran, no tenemos nada
                createdArguments.add(null);
            }
        }
		return createdArguments.toArray();
	}

	/**
	 * @see TareaConPadre#getTareaPadre()
	 */
	@Override
	public TareaParticionable<R> getTareaPadre() {
		return tareaPadre;
	}

	/**
	 * @see TareaConPadre#setTareaPadre(TareaParticionable)
	 */
	@Override
	public void setTareaPadre(final TareaParticionable<R> tareaPadre) {
		this.tareaPadre = tareaPadre;
	}

	public static <R> TareaPorReflection<R> create(final TypeMethod metodo, final Object instancia) {
		final TareaPorReflection<R> tarea = new TareaPorReflection<>();
		tarea.metodo = metodo;
		tarea.instancia = instancia;
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
}
