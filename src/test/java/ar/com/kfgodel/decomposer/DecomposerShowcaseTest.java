package ar.com.kfgodel.decomposer;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.decomposer.api.ProcesadorDeTareasParticionables;
import ar.com.kfgodel.decomposer.api.ResultadoIterativo;
import ar.com.kfgodel.decomposer.impl.ProcesadorDeParticionables;
import ar.com.kfgodel.decomposer.impl.ResultadoProducido;
import ar.com.kfgodel.decomposer.impl.TareaParticionableSupport;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type serves as a showcase test showing main decomposer functionalities
 * Created by kfgodel on 03/05/2015.
 */
@RunWith(JavaSpecRunner.class)
public class DecomposerShowcaseTest extends JavaSpec<DecomposerTestContext> {

    @Override
    public void define() {
        describe("A decomposer", ()->{
            it("can execute tasks",()->{
                ProcesadorDeParticionables procesador = ProcesadorDeParticionables.create();

                ResultadoIterativo<String> result = procesador.procesar(new TareaParticionableSupport<String>() {
                    @Override
                    public void ejecutarCon(ProcesadorDeTareasParticionables procesador) {
                        this.setResultado(ResultadoProducido.create("Hola"));
                    }
                });

                assertThat(result.getValorFinal()).isEqualTo("Hola");
            });
        });
    }
}
