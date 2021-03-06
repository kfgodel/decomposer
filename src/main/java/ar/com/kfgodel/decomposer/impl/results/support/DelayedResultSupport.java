package ar.com.kfgodel.decomposer.impl.results.support;

import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.api.results.DelayedResult;
import ar.com.kfgodel.decomposer.impl.results.CombinedResult;
import ar.com.kfgodel.decomposer.impl.results.PredefinedResult;

/**
 * This type serves as a base class for implementing delayed tasks
 * Created by kfgodel on 08/05/2015.
 */
public abstract class DelayedResultSupport implements DelayedResult {

    @Override
    public DelayedResult andFinally(DecomposableTask endTask) {
        return CombinedResult.create(this, endTask);
    }

    @Override
    public DelayedResult returning(Object returnedResult) {
        return PredefinedResult.create(this, returnedResult);
    }

}
