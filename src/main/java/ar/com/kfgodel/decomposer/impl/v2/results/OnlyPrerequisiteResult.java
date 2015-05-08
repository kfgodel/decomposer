package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.v2.context.ChildContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.decomposer.impl.v2.results.support.DelayedResultSupport;

import java.util.Arrays;
import java.util.List;

/**
 * This type represents a delayed result that depends on one subtask
 *
 * Created by kfgodel on 07/05/2015.
 */
public class OnlyPrerequisiteResult extends DelayedResultSupport {

    private DecomposableTask prerequisite;
    private TaskExecution prerequisiteExecution;

    @Override
    public List<TaskExecution> createPrerequisiteExecutions(DecomposedContext parentContext) {
        this.prerequisiteExecution = TaskExecution.create(prerequisite, ChildContext.create(parentContext));
        return Arrays.asList(this.prerequisiteExecution);
    }

    @Override
    public Object get() {
        return this.prerequisiteExecution.getEndResult();
    }

    public static OnlyPrerequisiteResult create(DecomposableTask prerequisite) {
        OnlyPrerequisiteResult result = new OnlyPrerequisiteResult();
        result.prerequisite = prerequisite;
        return result;
    }

}
