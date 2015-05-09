package ar.com.kfgodel.decomposer;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.api.results.DelayResult;
import ar.com.kfgodel.decomposer.impl.DecomposerProcessor;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class defines a simple but complete example test used to show the decomposer processor use case
 * Created by kfgodel on 09/05/15.
 */
@RunWith(JavaSpecRunner.class)
public class DecomposerExampleTest extends JavaSpec<DecomposerTestContext> {

    @Override
    public void define() {

        describe("a decomposer processor", () -> {

            context().decomposer(DecomposerProcessor::create);
            
            it("can process a task with subtasks combining their result",()->{

                AtomicInteger globalCounter = new AtomicInteger(0);

                DecomposableTask nextNumberProducer = (producerTaskContex)-> String.valueOf(globalCounter.getAndIncrement());

                DecomposableTask aTask = (taskContext)->
                    DelayResult.waitingFor(nextNumberProducer, nextNumberProducer, nextNumberProducer, nextNumberProducer)
                        .andFinally((combinatorContext) -> {
                            List<String> subTaskResults = combinatorContext.getSubTaskResults();
                            return subTaskResults.stream().collect(Collectors.joining(", "));
                        });

                String result = context().decomposer().process(aTask);

                assertThat(result).isEqualTo("0, 1, 2, 3");

            });   

        });


    }
}
