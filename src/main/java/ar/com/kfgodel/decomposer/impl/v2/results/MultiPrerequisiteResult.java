package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import ar.com.kfgodel.decomposer.impl.v2.context.ChildContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class MultiPrerequisiteResult implements DelayedResult {

    private List<DecomposableTask> prerequisites;
    private List<TaskExecution> prerequisiteExecutions;

    public static MultiPrerequisiteResult create(List<DecomposableTask> subtasks) {
        MultiPrerequisiteResult multiPrerequisiteResult = new MultiPrerequisiteResult();
        multiPrerequisiteResult.prerequisites = subtasks;
        return multiPrerequisiteResult;
    }

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
                .map(TaskExecution::getEndResult)
                .collect(Collectors.toList());
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
