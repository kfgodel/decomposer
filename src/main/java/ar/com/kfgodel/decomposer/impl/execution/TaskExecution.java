package ar.com.kfgodel.decomposer.impl.execution;

import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.api.context.DecomposedContext;
import ar.com.kfgodel.decomposer.api.results.DelayedResult;
import ar.com.kfgodel.decomposer.api.results.TaskResult;
import ar.com.kfgodel.decomposer.impl.results.DirectResult;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.List;
import java.util.function.Supplier;

/**
 * This type represents the execution of a single task keeping a reference to the task result.<br>
 * <br>
 * This object is used to execute the actual task, but wait for the end result to be accessed only
 * when sub tasks are processed first
 *
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

    /**
     * Executes the task with the defined context returning the list of pending
     * executions to fulfill before accessing this execution result
     * @return The list of prerequisite executions
     */
    public List<TaskExecution> execute() {
        TaskResult taskResult = executeWithContextAndGetResult();
        this.resultSupplier = taskResult;
        return taskResult.createPrerequisiteExecutions(this.context);
    }

    /**
     * Executes the task and interprets the result based on its type (delayed or not)
     * @return The task result description
     */
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

    /**
     * @return The final value produced by the task execution
     */
    public Object getResultValue() {
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
