package ar.com.kfgodel.decomposer;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.decomposer.api.DependenciaCircularException;
import ar.com.kfgodel.decomposer.api.ProcesadorDeTareasParticionables;
import ar.com.kfgodel.decomposer.api.ResultadoIterativo;
import ar.com.kfgodel.decomposer.api.TareaParticionable;
import ar.com.kfgodel.decomposer.impl.*;
import com.google.common.collect.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type serves as a showcase test showing main decomposer functionalities
 * Created by kfgodel on 03/05/2015.
 */
@RunWith(JavaSpecRunner.class)
public class DecomposerShowcaseTest extends JavaSpec<DecomposerTestContext> {

    @Override
    public void define() {
        describe("A decomposer", () -> {

            context().processor(ProcesadorDeParticionables::create);

            it("can execute a task in the current thread", () -> {
                ProcesadorDeParticionables processor = context().processor();

                ResultadoIterativo<String> result = processor.procesar(new TareaParticionableSupport<String>() {
                    @Override
                    public void ejecutarCon(ProcesadorDeTareasParticionables procesador) {
                        this.setResultado(ResultadoProducido.create("Hola"));
                    }
                });

                assertThat(result.getValorFinal()).isEqualTo("Hola");
            });

            it("can execute child tasks with their parent", () -> {
                final List<TareaParticionable> tareasEjecutadas = new ArrayList<>();
                TareaDePrueba tareaHija1 = TareaDePrueba.create(tareasEjecutadas);
                TareaDePrueba tareaHija2 = TareaDePrueba.create(tareasEjecutadas);

                final TareaDePrueba tareaPadre = TareaDePrueba.create(tareasEjecutadas);
                tareaPadre.agregarRequisito(tareaHija1);
                tareaPadre.agregarRequisito(tareaHija2);

                context().processor().procesar(tareaPadre);

                assertThat(tareasEjecutadas).isEqualTo(Lists.newArrayList(tareaHija1, tareaHija2, tareaPadre));
            });

            it("can detect circular task dependencies", () -> {
                final TareaParticionableSupport<Void> tareaQueDependeDeSiMisma = new TareaParticionableSupport<Void>() {
                    @Override
                    public void ejecutarCon(final ProcesadorDeTareasParticionables procesador) {
                        procesador.procesarRequisitoYContinuarConActual(this);
                    }
                };
                try {
                    context().processor().procesar(tareaQueDependeDeSiMisma);
                    failBecauseExceptionWasNotThrown(DependenciaCircularException.class);
                } catch (final DependenciaCircularException e) {
                    assertThat(e.getMessage()).startsWith("Se detecto una dependencia circular. La tarea");
                }
            });

            it("can execute methods of the tasks as child tasks", () -> {
                StringBuilder builder = new StringBuilder();

                final TareaEnPasosTemplate<Void> tareaPadre = new TareaEnPasosTemplate<Void>() {
                    @MetodoComoSubtarea(orden = 0)
                    public void primerPaso(final ProcesadorDeTareasParticionables procesador) {
                        builder.append("Hola");
                    }
                };

                context().processor().procesar(tareaPadre);

                assertThat(builder.toString()).isEqualTo("Hola");
            });

            it("can receive the processor as a method argument to schedule sub tasks", () -> {
                final List<TareaParticionable> tareasEjecutadas = new ArrayList<>();
                final TareaDePrueba tareaHija1 = TareaDePrueba.create(tareasEjecutadas);

                final TareaEnPasosTemplate<Integer> tareaPadre = new TareaEnPasosTemplate<Integer>() {
                    @MetodoComoSubtarea(orden = 0)
                    public void primerPaso(final ProcesadorDeTareasParticionables procesador) {
                        // Como primer paso creamos una subtarea y la procesamos antes de seguir al 2
                        procesador.procesarRequisitoYContinuarConActual(tareaHija1);
                    }
                    @MetodoComoSubtarea(orden = 1)
                    public ResultadoIterativo<List<Integer>> segundoPaso() {
                        // El segundo paso utiliza el resultado de la subtarea
                        return tareaHija1.getResultado();
                    }
                };

                context().processor().procesar(tareaPadre);

                assertThat(tareasEjecutadas).isEqualTo(Lists.newArrayList(tareaHija1));
            });

        });
    }
}
