package ar.com.kfgodel.decomposer.impl.results.subtasks;

import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.api.context.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.context.ChildContext;
import ar.com.kfgodel.decomposer.impl.execution.TaskExecution;
import ar.com.kfgodel.decomposer.impl.results.support.DelayedResultSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This type represents a delayed result that depends on several subtasks
 * to create a list with all the subtask results as the final result
 *
 * Created by kfgodel on 07/05/2015.
 */
public class MultiPrerequisiteResult extends DelayedResultSupport {

    private List<DecomposableTask> prerequisites;
    private List<TaskExecution> prerequisiteExecutions;

    public static MultiPrerequisiteResult create(List<DecomposableTask> subtasks) {
        MultiPrerequisiteResult multiPrerequisiteResult = new MultiPrerequisiteResult();
        multiPrerequisiteResult.prerequisites = subtasks;
        return multiPrerequisiteResult;
    }

    @Override
    public List<TaskExecution> createPrerequisiteExecutions(DecomposedContext parentContext) {
        this.prerequisiteExecutions = new ArrayList<>();
        for (DecomposableTask prerequisite : prerequisites) {
            TaskExecution childExecution = TaskExecution.create(prerequisite, ChildContext.create(parentContext));
            this.prerequisiteExecutions.add(childExecution);
        }
        return this.prerequisiteExecutions;
    }

    @Override
    public Object get() {
        // The result supplied is the list of all child tasks results
        return prerequisiteExecutions.stream()
                .map(TaskExecution::getResultValue)
                .collect(Collectors.toList());
    }

}
