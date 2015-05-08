package ar.com.kfgodel.decomposer.impl.v2.context;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class RootContext implements DecomposedContext {

    private Supplier<Object> sharedObject;
    @Override
    public void share(Object sharedObject) {
        this.sharedObject = ()-> sharedObject;
    }

    @Override
    public <R> R getShared() {
        if(sharedObject == null){
            throw new DecomposerException("There's no shared object defined");
        }
        return (R) sharedObject.get();
    }

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

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
