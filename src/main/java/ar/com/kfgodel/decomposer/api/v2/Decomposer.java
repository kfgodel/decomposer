package ar.com.kfgodel.decomposer.api.v2;

/**
 * This type represents a task processor that allows decomposition of tasks into sub-tasks. to be executed as part
 * They are executed as part of the process to
 * of their parent task
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface Decomposer {
    /**
     * Executes the given task in this thread and returns its final value. <br>
     *     As the task can spawn sub tasks using DelayedResult as returned object, this
     *     processor will execute every spawned task before accesing the end result and
     *     returning it
     * @param task The task to execute
     * @param <R> The expected result type
     * @return The result obtained from the task execution
     * @throws DecomposerException If a result is not produced (due to invalid delayed result),
     * an incorrect access to subtask results is performed, wrong access to shared objects
     * or cyclic dependency between task is detected
     */
    <R> R process(DecomposableTask task) throws DecomposerException;
}
