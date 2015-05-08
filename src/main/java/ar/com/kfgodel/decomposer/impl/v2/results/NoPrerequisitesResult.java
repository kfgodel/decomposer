package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.Collections;
import java.util.List;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class NoPrerequisitesResult implements DelayedResult {
    @Override
    public DelayedResult andFinally(DecomposableTask endTask) {
        return CombinedResult.create(this, endTask);
    }

    @Override
    public DelayedResult returning(Object returnedResult) {
        return PredefinedResult.create(this, returnedResult);
    }

    @Override
    public List<TaskExecution> getPrerequisites(DecomposedContext parentContext) {
        return Collections.emptyList();
    }

    public static NoPrerequisitesResult create() {
        NoPrerequisitesResult result = new NoPrerequisitesResult();
        return result;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

    @Override
    public Object get() {
        throw new DecomposerException("There's no result available because it was delayed to an empty sub-task list");
    }
}
