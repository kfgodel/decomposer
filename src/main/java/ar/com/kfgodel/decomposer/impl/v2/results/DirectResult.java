package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.Collections;
import java.util.List;

/**
 * This type represents a task result that doesn't require extra task processing.
 * The result is already defined and no wait is needed
 *
 * Created by kfgodel on 07/05/2015.
 */
public class DirectResult implements TaskResult {

    private Object returnedObject;

    @Override
    public Object get() {
        return returnedObject;
    }

    @Override
    public List<TaskExecution> createPrerequisiteExecutions(DecomposedContext parentContext) {
        return Collections.emptyList();
    }

    public static DirectResult create(Object returnedObject) {
        DirectResult directResult = new DirectResult();
        directResult.returnedObject = returnedObject;
        return directResult;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
