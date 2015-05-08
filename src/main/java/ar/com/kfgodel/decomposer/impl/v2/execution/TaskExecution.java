package ar.com.kfgodel.decomposer.impl.v2.execution;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import ar.com.kfgodel.decomposer.impl.v2.results.DirectResult;
import ar.com.kfgodel.decomposer.impl.v2.results.TaskResult;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class TaskExecution {

    private DecomposableTask task;
    private DecomposedContext context;
    private Supplier<Object> resultSupplier;

    public static TaskExecution create(DecomposableTask task, DecomposedContext context) {
        TaskExecution execution = new TaskExecution();
        execution.task = task;
        execution.context = context;
        return execution;
    }

    public List<TaskExecution> execute() {
        TaskResult taskResult = executeWithContextAndGetResult();
        this.resultSupplier = taskResult;
        return taskResult.getPrerequisites(this.context);
    }

    private TaskResult executeWithContextAndGetResult() {
        Object returnedObject = task.executeUnder(context);
        TaskResult taskResult;
        if(returnedObject instanceof DelayedResult){
            taskResult = (DelayedResult) returnedObject;
        }else{
            taskResult = DirectResult.create(returnedObject);
        }
        return taskResult;
    }

    public Object getEndResult() {
        if(resultSupplier == null){
            throw new IllegalStateException("There's no task result defined and one is requested");
        }
        return resultSupplier.get();
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}