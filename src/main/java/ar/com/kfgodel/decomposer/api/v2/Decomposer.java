package ar.com.kfgodel.decomposer.api.v2;

/**
 * This type represents a task processor that allows decomposition of tasks into sub-tasks. to be executed as part
 * They are executed as part of the process to
 * of their parent task
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface Decomposer {
    <R> R process(DecomposableTask task);
}
