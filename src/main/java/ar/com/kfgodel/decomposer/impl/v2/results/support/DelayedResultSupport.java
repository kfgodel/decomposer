package ar.com.kfgodel.decomposer.impl.v2.results.support;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import ar.com.kfgodel.decomposer.impl.v2.results.CombinedResult;
import ar.com.kfgodel.decomposer.impl.v2.results.PredefinedResult;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

/**
 * This type serves as a base class for implementing delayed task with common behavior pre-defined
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

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
