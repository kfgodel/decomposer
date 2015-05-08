package ar.com.kfgodel.decomposer.impl.execution;

import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * This type represents the execution stack used to keep track of pending executions
 *
 * Created by kfgodel on 07/05/2015.
 */
public class ExecutionStack {

    private Deque<TaskExecution> executions;

    public static ExecutionStack create() {
        ExecutionStack executionStack = new ExecutionStack();
        executionStack.executions = new LinkedList<>();
        return executionStack;
    }

    public void push(TaskExecution pendingExecution) {
        executions.push(pendingExecution);
    }

    public boolean isNotEmpty() {
        return !executions.isEmpty();
    }

    public TaskExecution pop() {
        return this.executions.pop();
    }

    public void push(List<TaskExecution> additionalTasks) {
        //Because this is a stack we need to put last first
        for (int i = additionalTasks.size() - 1; i >=0 ; i--) {
            TaskExecution previousTask = additionalTasks.get(i);
            this.push(previousTask);
        }
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
