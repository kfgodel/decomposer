package ar.com.kfgodel.decomposer.impl.v2.context;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class CombinedContext implements DecomposedContext {
    private DecomposedContext parentContext;
    private Supplier<Object> sharedObject;

    private List<TaskExecution> combinableExecutions;

    @Override
    public void share(Object sharedObject) {
        this.sharedObject = ()-> sharedObject;
    }

    @Override
    public <R> R getShared() {
        if(sharedObject != null){
            // There's an object defined in this context
            return (R) sharedObject.get();
        }
        // If not use the one inherited
        return parentContext.getShared();
    }

    @Override
    public <R> R getSubTaskResult() {
        List<R> subTaskResults = getSubTaskResults();
        int resultCount = subTaskResults.size();
        // We treat different 0 and N results to get a better error message
        switch (resultCount){
            case 0:
                throw new DecomposerException("There are no results to return");
            case 1:
                return subTaskResults.get(0);
            default:
                throw new DecomposerException("There are more than 1 result to return: "+ resultCount);
        }
    }

    @Override
    public <R> List<R> getSubTaskResults() {
        List previousResults = combinableExecutions.stream()
                .map(TaskExecution::getEndResult)
                .collect(Collectors.toList());
        return previousResults;
    }

    public static CombinedContext create(DecomposedContext parentContext, List<TaskExecution> combinableExecutions) {
        CombinedContext context = new CombinedContext();
        context.parentContext = parentContext;
        context.combinableExecutions = combinableExecutions;
        return context;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
