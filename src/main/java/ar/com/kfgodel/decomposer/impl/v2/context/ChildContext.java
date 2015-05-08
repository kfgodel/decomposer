package ar.com.kfgodel.decomposer.impl.v2.context;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by kfgodel on 07/05/2015.
 */
public class ChildContext implements DecomposedContext {

    private DecomposedContext parentContext;
    private Supplier<Object> sharedObject;

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
        return null;
    }

    @Override
    public <R> List<R> getSubTaskResults() {
        return null;
    }

    public static ChildContext create(DecomposedContext parentContext) {
        ChildContext context = new ChildContext();
        context.parentContext = parentContext;
        return context;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }
}
