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

    /**
     * Adds the given execution as next to be executed
     * @param pendingExecution The execution to push in the head of the stack
     */
    public void push(TaskExecution pendingExecution) {
        executions.push(pendingExecution);
    }

    /**
     * Indicates if this stack is empty
     * @return true if there are still pending executions
     */
    public boolean isNotEmpty() {
        return !executions.isEmpty();
    }

    /**
     * Removes and returns the next execution (the head of the stack)
     * @return The existing next execution
     */
    public TaskExecution pop() {
        return this.executions.pop();
    }

    /**
     * Adds the given execution list to be pushed in the stack. Ensuring that last
     * element of the list is going to be last to be processed
     * @param additionalTasks The list of task to execute next
     */
    public void push(List<TaskExecution> additionalTasks) {
        //Because this is a stack we need to put last first (to keep the list order)
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
