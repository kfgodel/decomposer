package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.decomposer.impl.v2.results.support.DelayedResultSupport;

import java.util.Collections;
import java.util.List;

/**
 * This type represents a delayed result that has no dependency on subtasks
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
