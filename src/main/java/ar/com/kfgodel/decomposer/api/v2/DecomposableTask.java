package ar.com.kfgodel.decomposer.api.v2;

/**
 * This type represents a task that can be decomposed in sub tasks to be processed to process it
 * Created by kfgodel on 06/05/2015.
 */
@FunctionalInterface
public interface DecomposableTask {

    /**
     * Executes this task using the given context to share state with parent and child tasks.<br>
     *     The method result may be a DelayedResult which has special semantics or any other object.<br>
     * A DelayedResult will contain subtasks that need to be processed first before accessing its value.
     * In that way this task can be decomposed in subtasks to be processed in simpler parts before getting
     * the actual result value
     *
     * @param taskContext The context used to access previous subtask results, parent shared object, or to share
     *                    an object to sub tasks
     * @return The direct result value, or a delayed result with prerequisite subtasks
     */
    Object executeUnder(DecomposedContext taskContext);
}
