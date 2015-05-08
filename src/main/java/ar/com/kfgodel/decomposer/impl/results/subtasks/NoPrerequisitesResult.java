package ar.com.kfgodel.decomposer.impl.results.subtasks;

import ar.com.kfgodel.decomposer.api.DecomposerException;
import ar.com.kfgodel.decomposer.api.context.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.execution.TaskExecution;
import ar.com.kfgodel.decomposer.impl.results.support.DelayedResultSupport;

import java.util.Collections;
import java.util.List;

/**
 * This type represents a delayed result that has no dependency on subtasks
 * to produce the end result. <br>
 * It's a useful case for tasks that depend on an unknown number of subtasks,
 * and sometimes that number is 0.
 *
 * Created by kfgodel on 07/05/2015.
 */
public class NoPrerequisitesResult extends DelayedResultSupport {

    @Override
    public List<TaskExecution> createPrerequisiteExecutions(DecomposedContext parentContext) {
        return Collections.emptyList();
    }

    public static NoPrerequisitesResult create() {
        NoPrerequisitesResult result = new NoPrerequisitesResult();
        return result;
    }

    @Override
    public Object get() {
        throw new DecomposerException("There's no result available because it was delayed to an empty sub-task list");
    }
}
