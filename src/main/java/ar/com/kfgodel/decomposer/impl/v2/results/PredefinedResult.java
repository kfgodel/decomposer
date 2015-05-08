package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.List;

/**
 * This type represents a delayed result whose end value is already defined
 *
 * Created by kfgodel on 07/05/2015.
 */
public class PredefinedResult implements DelayedResult {

    private Object predefinedValue;

    private DelayedResult prerequisite;

    @Override
    public DelayedResult andFinally(DecomposableTask endTask) {
        throw new DecomposerException("The result is already defined as["+predefinedValue+"] cannot use a task to define it");
    }

    @Override
    public DelayedResult returning(Object returnedResult) {
        throw new DecomposerException("The result is already defined as["+predefinedValue+"] cannot accept new value["+returnedResult+"]");
    }

    @Override
    public List<TaskExecution> getPrerequisites(DecomposedContext parentContext) {
        return prerequisite.getPrerequisites(parentContext);
    }

    public static PredefinedResult create(DelayedResult prerequisite, Object predefinedValue) {
        PredefinedResult predefinedResult = new PredefinedResult();
        predefinedResult.predefinedValue = predefinedValue;
        predefinedResult.prerequisite = prerequisite;
        return predefinedResult;
    }

    @Override
    public Object get() {
        return predefinedValue;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
