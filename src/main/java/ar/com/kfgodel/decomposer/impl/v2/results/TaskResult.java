package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;

import java.util.List;
import java.util.function.Supplier;

/**
 * This type represents an abstraction over two kinds of results: direct or delayed.<br>
 *     Direct results don't need to wait on other taskstobe accessed.<br>
 *     Delayed results need to wait on subtask resolution to access the final value
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
