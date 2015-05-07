package ar.com.kfgodel.decomposer.api.v2;

import java.util.List;

/**
 * This type represents a task result that is delayed until other tasks are resolved first
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface DelayedResult {
    static DelayedResult until(DecomposableTask... nextTasks) {
        return null;
    }

    DelayedResult andThen(DecomposableTask endTask);

    DelayedResult returning(Object returnedResult);

    static DelayedResult until(List<DecomposableTask> subtasks) {
        return null;
    }
}
