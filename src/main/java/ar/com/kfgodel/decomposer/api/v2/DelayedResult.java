package ar.com.kfgodel.decomposer.api.v2;

import ar.com.kfgodel.decomposer.impl.v2.results.TaskResult;

/**
 * This type represents a task result that is delayed until other tasks are resolved first
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface DelayedResult extends TaskResult {

    /**
     * Defines a final operation to be executed as requirement to access this result value.<br>
     *     The given task can also spawn its own sub tasks
     * @param endTask The task to execute after the previous subtasks
     * @return The delayed result that will wait for this final task
     */
    DelayedResult andFinally(DecomposableTask endTask);

    /**
     * Defines as result a final value that is to be returned only after subtasks are executed first
     * @param returnedResult The value to return as result
     * @return The delayed result that waits for subtasks and returns the given value
     */
    DelayedResult returning(Object returnedResult);

}
