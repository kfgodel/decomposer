package ar.com.kfgodel.decomposer;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This test verifies that the processor handles sub task branching
 * Created by kfgodel on 06/05/2015.
 */
@RunWith(JavaSpecRunner.class)
public class TaskBranchingTest extends JavaSpec<DecomposerTestContext> {

    @Override
    public void define() {
        describe("a decomposer processor", ()->{

            it("can execute a task", () -> {
                DecomposableTask task = (taskContext) -> "Hola";

                String taskResult = context().decomposer().process(task);

                assertThat(taskResult).isEqualTo("Hola");
            });

            describe("a task with sub-tasks", () -> {

                it("can use its sub-task result as its own result", () -> {
                    DecomposableTask subTask = (subTaskContext) -> "Subtask";

                    DecomposableTask task = (taskContext) -> {
                        return DelayedResult.until(subTask);
                    };

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Subtask");
                });

                it("can use method references as sub-tasks", () -> {
                    DecomposableTask task = (taskContext) ->
                            DelayedResult.until(this::calculateSalutation);

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Hola Mundo!");
                });

                it("can nest any number of sub-task levels", () -> {
                    DecomposableTask subSubTask = (subSubTaskContext) -> "Sub-Sub-Task";

                    DecomposableTask subTask = (subTaskContext) -> DelayedResult.until(subSubTask);

                    DecomposableTask task = (taskContext) -> DelayedResult.until(subTask);

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Sub-Sub-Task");

                });

                it("can return its own result regardless of the subtasks", () -> {
                    DecomposableTask subTask = (subTaskContext) -> "Subtask";

                    DecomposableTask task = (taskContext) ->
                            DelayedResult.until(subTask)
                                    .returning("Task");

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Task");
                });

                it("can combine its result with its subtask result", () -> {
                    DecomposableTask subTask = (subTaskContext) -> "Subtask";

                    DecomposableTask task = (taskContext) ->
                            DelayedResult.until(subTask)
                                    .andThen((endTaskContext) -> endTaskContext.getSubTaskResult() + " and Task");

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Subtask and Task");
                });

                it("can combine multiple sub-task results", () -> {
                    DecomposableTask firstSubTask = (subTaskContext) -> "1";
                    DecomposableTask secondSubTask = (subTaskContext) -> "2";
                    DecomposableTask thirdSubTask = (subTaskContext) -> "3";

                    DecomposableTask task = (taskContext) ->
                            DelayedResult.until(firstSubTask, secondSubTask, thirdSubTask)
                                    .andThen((endTaskContext) -> {
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
                            DelayedResult.until(firstSubTask, secondSubTask, thirdSubTask);

                    List<String> taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo(Lists.newArrayList("1", "2", "3"));
                });

                it("continues with task end if empty sub-task list used", () -> {
                    List<DecomposableTask> subtasks = new ArrayList<>();

                    DecomposableTask task = (taskContext) ->
                            DelayedResult.until(subtasks)
                                    .andThen((endTaskContext) -> "Processed subtasks: " + endTaskContext.getSubTaskResults().size());

                    String taskResult = context().decomposer().process(task);

                    assertThat(taskResult).isEqualTo("Processed subtasks: 0");
                });

                it("produces an error if no explicit result and an empty subtask list", () -> {
                    List<DecomposableTask> subtasks = new ArrayList<>();

                    DecomposableTask task = (taskContext) ->
                            DelayedResult.until(subtasks);
                    try {
                        context().decomposer().process(task);
                        failBecauseExceptionWasNotThrown(Exception.class);
                    } catch (Exception e) {
                        assertThat(e.getMessage()).isEqualTo("asd");
                    }
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
                        return DelayedResult
                                .until(firstSubTask, secondSubTask)
                                .andThen((endTaskContext) -> {
                                    builder.append("!");
                                    return builder.toString();
                                });
                    };

                    String parentResult = context().decomposer().process(parentTask);

                    assertThat(parentResult).isEqualTo("Hello World!");
                });

                it("a sub-task can share its own object without affecting sibling tasks",()->{
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
                        return DelayedResult.until(subSubTask);
                    };
                    DecomposableTask secondSubTask = (taskContext) -> {
                        StringBuilder parentBuilder = taskContext.getShared();
                        parentBuilder.append(", 2");
                        return null;
                    };

                    DecomposableTask parentTask = (taskContext) -> {
                        StringBuilder builder = new StringBuilder();
                        taskContext.share(builder);
                        return DelayedResult
                                .until(firstSubTask, secondSubTask)
                                .andThen((endTaskContext) -> builder.toString());
                    };

                    String parentResult = context().decomposer().process(parentTask);

                    assertThat(parentResult).isEqualTo("1, 2");
                });

                it("if not overriden the shared object is inherited by sub-tasks", ()->{
                    DecomposableTask subSubTask = (subSubTaskContext) -> {
                        StringBuilder childBuilder = subSubTaskContext.getShared();
                        childBuilder.append(", 3");
                        return null;
                    };
                    DecomposableTask firstSubTask = (taskContext) -> {
                        StringBuilder parentBuilder = taskContext.getShared();
                        parentBuilder.append("1");
                        return DelayedResult.until(subSubTask);
                    };

                    DecomposableTask parentTask = (taskContext) -> {
                        StringBuilder builder = new StringBuilder();
                        taskContext.share(builder);
                        return DelayedResult
                                .until(firstSubTask)
                                .andThen((endTaskContext) -> builder.toString());
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
