package ar.com.kfgodel.decomposer;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.api.v2.DelayResult;
import ar.com.kfgodel.decomposer.impl.v2.DecomposerProcessor;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This test verifies that the processor handles sub task branching
 * Created by kfgodel on 06/05/2015.
 */
@RunWith(JavaSpecRunner.class)
public class DecomposerV2Test extends JavaSpec<DecomposerTestContext> {

    @Override
    public void define() {
        describe("a decomposer processor", ()->{

            context().decomposer(DecomposerProcessor::create);

            it("can execute a task", () -> {
                DecomposableTask task = (taskContext) -> "Hola";

                String taskResult = context().decomposer().process(task);

                assertThat(taskResult).isEqualTo("Hola");
            });

            describe("a task with sub-tasks", () -> {

                it("can use its sub-task result as its own result", () -> {
                    DecomposableTask subTask = (subTaskContext) -> "Subtask";

                    DecomposableTask task = (taskContext) -> {
                        return DelayResult.waitingFor(subTask);
                    };

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Subtask");
                });

                it("can use method references as sub-tasks", () -> {
                    DecomposableTask task = (taskContext) ->
                            DelayResult.waitingFor(this::calculateSalutation);

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Hola Mundo!");
                });

                it("can nest any number of sub-task levels", () -> {
                    DecomposableTask subSubTask = (subSubTaskContext) -> "Sub-Sub-Task";

                    DecomposableTask subTask = (subTaskContext) -> DelayResult.waitingFor(subSubTask);

                    DecomposableTask task = (taskContext) -> DelayResult.waitingFor(subTask);

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Sub-Sub-Task");

                });

                it("can return its own result regardless of the subtasks", () -> {
                    DecomposableTask subTask = (subTaskContext) -> "Subtask";

                    DecomposableTask task = (taskContext) ->
                            DelayResult.waitingFor(subTask)
                                    .returning("Task");

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Task");
                });

                it("can combine its result with its subtask result", () -> {
                    DecomposableTask subTask = (subTaskContext) -> "Subtask";

                    DecomposableTask task = (taskContext) ->
                            DelayResult.waitingFor(subTask)
                                    .andFinally((endTaskContext) -> endTaskContext.getSubTaskResult() + " and Task");

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Subtask and Task");
                });

                it("can nest also in the combinator task", () -> {
                    DecomposableTask nestedFinalTask = (nestedTaskContext) -> {
                        StringBuilder outerBuilder = nestedTaskContext.getShared();
                        outerBuilder.append("Hello");
                        return null;
                    };

                    DecomposableTask task = taskContext -> {
                        return DelayResult.waitingFor((subTaskContext) -> new StringBuilder())
                                .andFinally((nestingTaskContext) -> {
                                    nestingTaskContext.share(nestingTaskContext.getSubTaskResult());
                                    return DelayResult.waitingFor(nestedFinalTask)
                                            .andFinally((endTaskContext) -> endTaskContext.getShared().toString());
                                });
                    };

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Hello");
                });

                it("can combine multiple sub-task results", () -> {
                    DecomposableTask firstSubTask = (subTaskContext) -> "1";
                    DecomposableTask secondSubTask = (subTaskContext) -> "2";
                    DecomposableTask thirdSubTask = (subTaskContext) -> "3";

                    DecomposableTask task = (taskContext) ->
                            DelayResult.waitingFor(firstSubTask, secondSubTask, thirdSubTask)
                                    .andFinally((endTaskContext) -> {
                                        List<String> subTaskResults = endTaskContext.getSubTaskResults();
                                        return subTaskResults.stream().collect(Collectors.joining(", "));
                                    });

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("1, 2, 3");

                });

                it("has an implicit subtask result list as result if no explicit result given", () -> {
                    DecomposableTask firstSubTask = (subTaskContext) -> "1";
                    DecomposableTask secondSubTask = (subTaskContext) -> "2";
                    DecomposableTask thirdSubTask = (subTaskContext) -> "3";

                    DecomposableTask task = (taskContext) ->
                            DelayResult.waitingFor(firstSubTask, secondSubTask, thirdSubTask);

                    List<String> taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo(Lists.newArrayList("1", "2", "3"));
                });

                it("continues with task end if empty sub-task list used", () -> {
                    List<DecomposableTask> subtasks = new ArrayList<>();

                    DecomposableTask task = (taskContext) ->
                            DelayResult.waitingFor(subtasks)
                                    .andFinally((endTaskContext) -> "Processed subtasks: " + endTaskContext.getSubTaskResults().size());

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Processed subtasks: 0");
                });

                it("produces an error if no explicit result and an empty subtask list", () -> {
                    List<DecomposableTask> subtasks = new ArrayList<>();

                    DecomposableTask task = (taskContext) ->
                            DelayResult.waitingFor(subtasks);
                    try {
                        context().decomposer().process(task);
                        failBecauseExceptionWasNotThrown(DecomposerException.class);
                    } catch (DecomposerException e) {
                        assertThat(e.getMessage()).isEqualTo("There's no result available because it was delayed to an empty sub-task list");
                    }
                });

                it("can execute the same task in a loop like", () -> {
                    AtomicInteger counter = new AtomicInteger(10);

                    // Needed for the lambdaish task to reference itself
                    AtomicReference<DecomposableTask> taskAutoReference = new AtomicReference<DecomposableTask>();
                    taskAutoReference.set((taskContext) -> {
                        if (counter.decrementAndGet() == 0) {
                            return "Finished";
                        }
                        return DelayResult.waitingFor(taskAutoReference.get());
                    });

                    String taskResult = context().decomposer().process(taskAutoReference.get());

                    assertThat(taskResult).isEqualTo("Finished");

                });

            });

            describe("task context", () -> {

                it("allows sharing an object between a task and its sub-tasks", () -> {

                    DecomposableTask firstSubTask = (taskContext) -> {
                        StringBuilder builder = taskContext.getShared();
                        builder.append("Hello");
                        return null;
                    };
                    DecomposableTask secondSubTask = (taskContext) -> {
                        StringBuilder builder = taskContext.getShared();
                        builder.append(" World");
                        return null;
                    };

                    DecomposableTask parentTask = (taskContext) -> {
                        StringBuilder builder = new StringBuilder();
                        taskContext.share(builder);
                        return DelayResult
                                .waitingFor(firstSubTask, secondSubTask)
                                .andFinally((endTaskContext) -> {
                                    builder.append("!");
                                    return builder.toString();
                                });
                    };

                    String parentResult = context().decomposer().process(parentTask);

                    assertThat(parentResult).isEqualTo("Hello World!");
                });

                it("a sub-task can share its own object without affecting sibling tasks", () -> {
                    DecomposableTask subSubTask = (subSubTaskContext) -> {
                        StringBuilder childBuilder = subSubTaskContext.getShared();
                        childBuilder.append(", 3");
                        return null;
                    };
                    DecomposableTask firstSubTask = (taskContext) -> {
                        StringBuilder parentBuilder = taskContext.getShared();
                        parentBuilder.append("1");

                        StringBuilder ownBuilder = new StringBuilder();
                        taskContext.share(ownBuilder);
                        return DelayResult.waitingFor(subSubTask);
                    };
                    DecomposableTask secondSubTask = (taskContext) -> {
                        StringBuilder parentBuilder = taskContext.getShared();
                        parentBuilder.append(", 2");
                        return null;
                    };

                    DecomposableTask parentTask = (taskContext) -> {
                        StringBuilder builder = new StringBuilder();
                        taskContext.share(builder);
                        return DelayResult
                                .waitingFor(firstSubTask, secondSubTask)
                                .andFinally((endTaskContext) -> builder.toString());
                    };

                    String parentResult = context().decomposer().process(parentTask);

                    assertThat(parentResult).isEqualTo("1, 2");
                });

                it("if not overriden the shared object is inherited by sub-tasks", () -> {
                    DecomposableTask subSubTask = (subSubTaskContext) -> {
                        StringBuilder childBuilder = subSubTaskContext.getShared();
                        childBuilder.append(", 3");
                        return null;
                    };
                    DecomposableTask firstSubTask = (taskContext) -> {
                        StringBuilder parentBuilder = taskContext.getShared();
                        parentBuilder.append("1");
                        return DelayResult.waitingFor(subSubTask);
                    };

                    DecomposableTask parentTask = (taskContext) -> {
                        StringBuilder builder = new StringBuilder();
                        taskContext.share(builder);
                        return DelayResult
                                .waitingFor(firstSubTask)
                                .andFinally((endTaskContext) -> builder.toString());
                    };

                    String parentResult = context().decomposer().process(parentTask);

                    assertThat(parentResult).isEqualTo("1, 3");
                });

            });

        });
    }

    public Object calculateSalutation(DecomposedContext taskContext){
        return "Hola Mundo!";
    }
}
