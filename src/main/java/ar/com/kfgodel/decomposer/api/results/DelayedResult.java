package ar.com.kfgodel.decomposer.api.results;

import ar.com.kfgodel.decomposer.api.DecomposableTask;

/**
 * This type represents a task result that is delayed until other tasks are resolved first
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface DelayedResult extends TaskResult {

    /**
     * Defines a final combinator task to be executed as the final value producer.<br>
     * The given task can also spawn its own sub tasks (nesting any number of levels)
     * @param endTask The task to execute after the previous subtasks
     * @return The delayed result that will wait for this final task
     */
    DelayedResult andFinally(DecomposableTask endTask);

    /**
     * Defines an explicit value to be used as result only after subtasks are executed first
     * @param returnedResult The value to return as result
     * @return The delayed result that waits for subtasks and returns the given value
     */
    DelayedResult returning(Object returnedResult);

}
