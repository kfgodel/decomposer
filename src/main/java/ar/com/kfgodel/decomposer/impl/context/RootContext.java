package ar.com.kfgodel.decomposer.impl.context;

import ar.com.kfgodel.decomposer.api.DecomposerException;
import ar.com.kfgodel.decomposer.impl.context.support.ContextSupport;

import java.util.List;

/**
 * This type represents the execution context of the first processed task (the root task)
 *
 * Created by kfgodel on 07/05/2015.
 */
public class RootContext extends ContextSupport {

    @Override
    public <R> R getSubTaskResult() {
        throw new DecomposerException("There's no inherited sub task result in the context");
    }

    @Override
    public <R> List<R> getSubTaskResults() {
        throw new DecomposerException("There are no inherited sub task results in the context");
    }

    public static RootContext create() {
        RootContext context = new RootContext();
        return context;
    }

}
