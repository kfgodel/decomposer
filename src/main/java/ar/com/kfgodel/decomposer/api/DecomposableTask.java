package ar.com.kfgodel.decomposer.api;

import ar.com.kfgodel.decomposer.api.context.DecomposedContext;

/**
 * This type represents a task that can be decomposed into sub tasks to be processed.<br>
 *     In that way a task can be defined into smaller simpler pieces to be solved before the master
 *     part, or integrate their results into a single result<br>
 *  <br>
 *  For sub tasks to be processed, they need to be returned in an instanceof DelayedResult,
 *  that can be constructed with DelayedResult. The Decomposer processor will treat differently
 *  objects of that type processing any subtask before returning the final result
 *
 * Created by kfgodel on 06/05/2015.
 */
@FunctionalInterface
public interface DecomposableTask {

    /**
     * Executes this task using the given context to share state with parent and child tasks.<br>
     *     The method result may be a DelayedResult which has special semantics or any other object.<br>
     * A DelayedResult will contain subtasks that need to be processed first before accessing its value.
     * In that way this task can be decomposed in subtasks to be processed in simpler parts before getting
     * the actual result value.<br>
     * A final subtask can be used as a cominator of the results of previous subtasks
     *
     * @param taskContext The context used to share an object to sub tasks, access parent shared object
     *                    or access previous subtask results
     * @return The direct result value, or a delayed result with prerequisite subtasks
     */
    Object executeUnder(DecomposedContext taskContext);
}
