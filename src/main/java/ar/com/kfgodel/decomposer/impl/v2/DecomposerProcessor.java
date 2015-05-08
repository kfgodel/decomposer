package ar.com.kfgodel.decomposer.impl.v2;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.Decomposer;
import ar.com.kfgodel.decomposer.impl.v2.context.RootContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.ExecutionStack;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.List;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class DecomposerProcessor implements Decomposer {

    @Override
    public <R> R process(DecomposableTask task) {
        TaskExecution rootExecution = TaskExecution.create(task, RootContext.create());

        ExecutionStack executionStack = ExecutionStack.create();
        executionStack.push(rootExecution);

        while(executionStack.isNotEmpty()){
            TaskExecution nextExecution = executionStack.pop();
            List<TaskExecution> additionalTasks = nextExecution.execute();
            executionStack.push(additionalTasks);
        }

        Object taskResult = rootExecution.getEndResult();
        return (R) taskResult;
    }

    public static DecomposerProcessor create() {
        DecomposerProcessor processor = new DecomposerProcessor();
        return processor;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
