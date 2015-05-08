package ar.com.kfgodel.decomposer.api.context;

import ar.com.kfgodel.decomposer.api.DecomposerException;

import java.util.List;

/**
 * This type represents the context of a task execution that allows sharing of state between tasks
 * and their sub tasks
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface DecomposedContext {
    /**
     * Makes an object available to sub tasks through their own context.<br>
     *     This method overrides the object shared by the parent context
     * @param sharedObject The object to use from this context and below
     */
    void share(Object sharedObject);

    /**
     * Returns the object shared by parent task in the parent execution context.<br>
     *     An error is produced if no object was shared by parent task
     * @param <R> The type of expected object
     * @return The object shared by parent task
     * @throws DecomposerException If no objects was shared
     */
    <R> R getShared() throws DecomposerException;

    /**
     * Returns the result of the only subtask that a parent task executed.<br>
     *     This method is a facility method that delegates on getSubTaskResults()
     *     and extracts the only task result.<br>
     *  An error is produced if less or more than one sub task was executed.<br>
     * @param <R> The type of expected subtask result value
     * @return The task result value
     * @throws DecomposerException If no task, or more than one task was executed
     * as requirement prior to this context
     */
    <R> R getSubTaskResult() throws  DecomposerException;

    /**
     * Returns the results of the subtasks executed by parent task, prior to this final task.<br>
     *     This method is assumed to be only accessed from tasks after subtasks execution.
     *     I.E: Only in the andFinally() method of a delayed task.<br>
     *  An error is produced if accessed from outside that scope
     * @param <R> The type of expected task results
     * @return The list of task results after executing the subtasks of a delayed task
     * @throws DecomposerException If accessed from outside a combinator task context
     */
    <R> List<R> getSubTaskResults() throws DecomposerException;
}
