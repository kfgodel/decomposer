package ar.com.kfgodel.decomposer.impl.v2.context;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.impl.v2.context.support.NonRootContextSupport;

import java.util.List;

/**
 * This type represents the execution context of a prerequisite task
 *
 * Created by kfgodel on 07/05/2015.
 */
public class ChildContext extends NonRootContextSupport {

    @Override
    public <R> R getSubTaskResult() {
        throw new DecomposerException("There is no inherited sub task result in this child context");
    }

    @Override
    public <R> List<R> getSubTaskResults() {
        throw new DecomposerException("There are no inherited sub task results in this child context");
    }

    public static ChildContext create(DecomposedContext parentContext) {
        ChildContext context = new ChildContext();
        context.setParentContext(parentContext);
        return context;
    }

}
