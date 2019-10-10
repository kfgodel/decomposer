package ar.com.kfgodel.decomposer.impl;

import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.api.Decomposer;
import ar.com.kfgodel.decomposer.impl.context.RootContext;
import ar.com.kfgodel.decomposer.impl.execution.ExecutionStack;
import ar.com.kfgodel.decomposer.impl.execution.TaskExecution;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * This type implements the decomposer processor for nested task resolutions
 *
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

        Object taskResult = rootExecution.getResultValue();
        return (R) taskResult;
    }

    public static DecomposerProcessor create() {
        DecomposerProcessor processor = new DecomposerProcessor();
        return processor;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
          .toString();
    }
}
