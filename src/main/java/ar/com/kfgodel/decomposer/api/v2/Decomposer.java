package ar.com.kfgodel.decomposer.api.v2;

/**
 * This type represents a task processor that allows sub-tasks to be executed as part
 * of their parent task
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface Decomposer {
    <R> R process(DecomposableTask task);
}
