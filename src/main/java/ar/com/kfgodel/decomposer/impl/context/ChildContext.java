package ar.com.kfgodel.decomposer.impl.context;

import ar.com.kfgodel.decomposer.api.context.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.context.support.NonRootContextSupport;

import java.util.List;

/**
 * This type represents the execution context of a subtask
 *
 * Created by kfgodel on 07/05/2015.
 */
public class ChildContext extends NonRootContextSupport {

    @Override
    public <R> R getSubTaskResult() {
        return getParentContext().getSubTaskResult();
    }

    @Override
    public <R> List<R> getSubTaskResults() {
        return getParentContext().getSubTaskResults();
    }

    public static ChildContext create(DecomposedContext parentContext) {
        ChildContext context = new ChildContext();
        context.setParentContext(parentContext);
        return context;
    }

}
