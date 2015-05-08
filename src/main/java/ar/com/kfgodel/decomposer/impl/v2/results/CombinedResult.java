package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposableTask;
import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.api.v2.DelayedResult;
import ar.com.kfgodel.decomposer.impl.v2.context.CombinedContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.ArrayList;
import java.util.List;

/**
 * This type represents a combined result that works on previous results to produce the final one
 * using a combinator task
 *
 * Created by kfgodel on 07/05/2015.
 */
public class CombinedResult implements DelayedResult {

    private DelayedResult prerequisite;
    private DecomposableTask resultCombinator;
    private TaskExecution combinatorExecution;

    @Override
    public DelayedResult andFinally(DecomposableTask endTask) {
        throw new DecomposerException("The end result already depends on a task. Cannot depend on other");
    }

    @Override
    public DelayedResult returning(Object returnedResult) {
        throw new DecomposerException("The end result was already defined to depend on a task. Cannot accept new value: ["+returnedResult+"]");
    }

    @Override
    public List<TaskExecution> getPrerequisites(DecomposedContext parentContext) {
        List<TaskExecution> combinableExecutions = prerequisite.getPrerequisites(parentContext);
        this.combinatorExecution = TaskExecution.create(resultCombinator, CombinedContext.create(parentContext, combinableExecutions));
        List<TaskExecution> combinedRequisites = new ArrayList<>(combinableExecutions);
        combinedRequisites.add(this.combinatorExecution);
        return combinedRequisites;
    }

    public static CombinedResult create(DelayedResult prerequisite, DecomposableTask resultCombinator) {
        CombinedResult combinedResult = new CombinedResult();
        combinedResult.prerequisite = prerequisite;
        combinedResult.resultCombinator = resultCombinator;
        return combinedResult;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

    @Override
    public Object get() {
        return combinatorExecution.getEndResult();
    }
}
