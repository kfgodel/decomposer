package ar.com.kfgodel.decomposer.impl.results;

import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.api.DecomposerException;
import ar.com.kfgodel.decomposer.api.context.DecomposedContext;
import ar.com.kfgodel.decomposer.api.results.DelayedResult;
import ar.com.kfgodel.decomposer.impl.execution.TaskExecution;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * This type represents a delayed result whose value is already defined,
 * but it must wait for subtasks to finish
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
    public List<TaskExecution> createPrerequisiteExecutions(DecomposedContext parentContext) {
        return prerequisite.createPrerequisiteExecutions(parentContext);
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
          .add("predefinedValue", predefinedValue)
          .add("prerequisite", prerequisite)
          .toString();
    }
}
