package ar.com.kfgodel.decomposer.api;

/**
 * This type represents a task processor that allows decomposition of tasks into sub-tasks.<br>
 * This processor allows a task to delay their result until other subtasks are finished first.
 * By returning a DelayedResult instance a task can indicate that there's still more work to be
 * done before the final result is available.
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface Decomposer {
    /**
     * Executes the given task in this thread and returns its final value. <br>
     *     As the task can spawn sub tasks using DelayedResult as returned object, this
     *     processor will execute every spawned task before accessing the end result and
     *     returning it
     * @param task The task to execute
     * @param <R> The expected result type
     * @return The result obtained from the task execution
     * @throws DecomposerException
     *  If a result is not produced (due to invalid delayed result),
     *  an incorrect access to subtask results is performed,
     *  wrong access to shared objects
     */
    <R> R process(DecomposableTask task) throws DecomposerException;
}
