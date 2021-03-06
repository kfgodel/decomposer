package ar.com.kfgodel.decomposer.impl.results.subtasks;

import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.api.context.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.context.ChildContext;
import ar.com.kfgodel.decomposer.impl.execution.TaskExecution;
import ar.com.kfgodel.decomposer.impl.results.support.DelayedResultSupport;
import com.google.common.base.MoreObjects;

import java.util.Arrays;
import java.util.List;

/**
 * This type represents a delayed result that depends on one subtask
 *
 * Created by kfgodel on 07/05/2015.
 */
public class OnePrerequisiteResult extends DelayedResultSupport {

    private DecomposableTask prerequisite;
    private TaskExecution prerequisiteExecution;

    @Override
    public List<TaskExecution> createPrerequisiteExecutions(DecomposedContext parentContext) {
        this.prerequisiteExecution = TaskExecution.create(prerequisite, ChildContext.create(parentContext));
        return Arrays.asList(this.prerequisiteExecution);
    }

    @Override
    public Object get() {
        return this.prerequisiteExecution.getResultValue();
    }

    public static OnePrerequisiteResult create(DecomposableTask prerequisite) {
        OnePrerequisiteResult result = new OnePrerequisiteResult();
        result.prerequisite = prerequisite;
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
          .add("prerequisite", prerequisite)
          .add("prerequisiteExecution", prerequisiteExecution)
          .toString();
    }
}
