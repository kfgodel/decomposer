package ar.com.kfgodel.decomposer.api.v2;

import ar.com.kfgodel.decomposer.impl.v2.results.TaskResult;

/**
 * This type represents a task result that is delayed until other tasks are resolved first
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface DelayedResult extends TaskResult {

    DelayedResult andFinally(DecomposableTask endTask);

    DelayedResult returning(Object returnedResult);

}
