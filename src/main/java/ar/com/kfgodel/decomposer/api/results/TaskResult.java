package ar.com.kfgodel.decomposer.api.results;

import ar.com.kfgodel.decomposer.api.context.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.execution.TaskExecution;

import java.util.List;
import java.util.function.Supplier;

/**
 * This type represents an abstraction over two kinds of results: direct or delayed.
 * Both types supply a result value at the end of the task execution. Delayed tasks
 * require subtasks to be fulfilled before that value is available<br>
 * <br>
 * Direct results don't need to wait on other tasks for their result to be accessed.<br>
 * Delayed results need to wait on subtask executions to access the final value
 *
 * Created by kfgodel on 07/05/2015.
 */
public interface TaskResult extends Supplier<Object> {

    /**
     * Creates and returns the list of task executions that need to be done before
     * accessing the value of this result
     * @param parentContext The context from which sub tasks execution can inherit
     * @return The list of needed sub tasks, or an empty list
     */
    List<TaskExecution> createPrerequisiteExecutions(DecomposedContext parentContext);

}
