package ar.com.kfgodel.decomposer.impl.v2.context;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.impl.v2.context.support.NonRootContextSupport;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This type represents the execution context of a combinator task (executed after prerequisites)
 *
 * Created by kfgodel on 07/05/2015.
 */
public class CombinatorContext extends NonRootContextSupport {

    private List<TaskExecution> combinableExecutions;

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

    public static CombinatorContext create(DecomposedContext parentContext, List<TaskExecution> combinableExecutions) {
        CombinatorContext context = new CombinatorContext();
        context.setParentContext(parentContext);
        context.combinableExecutions = combinableExecutions;
        return context;
    }

}
