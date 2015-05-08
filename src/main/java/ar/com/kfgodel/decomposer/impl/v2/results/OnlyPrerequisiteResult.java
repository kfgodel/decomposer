package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import ar.com.kfgodel.decomposer.impl.v2.context.ChildContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class OnlyPrerequisiteResult implements DelayedResult {

    private DecomposableTask prerequisite;
    private TaskExecution prerequisiteExecution;

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
        this.prerequisiteExecution = TaskExecution.create(prerequisite, ChildContext.create(parentContext));
        return Arrays.asList(this.prerequisiteExecution);
    }

    public static OnlyPrerequisiteResult create(DecomposableTask prerequisite) {
        OnlyPrerequisiteResult result = new OnlyPrerequisiteResult();
        result.prerequisite = prerequisite;
        return result;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

    @Override
    public Object get() {
        return this.prerequisiteExecution.getEndResult();
    }
}
