package ar.com.kfgodel.decomposer.impl.v2.context;

import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.decomposer.impl.v2.context.support.ContextSupport;

import java.util.List;

/**
 * This type represents the context of execution of the first processed task
 * Created by kfgodel on 07/05/2015.
 */
public class RootContext extends ContextSupport {

    @Override
    public <R> R getSubTaskResult() {
        throw new DecomposerException("There's no inherited sub task result in the root context");
    }

    @Override
    public <R> List<R> getSubTaskResults() {
        throw new DecomposerException("There are no inherited sub task results in the root context");
    }

    public static RootContext create() {
        RootContext context = new RootContext();
        return context;
    }

}
