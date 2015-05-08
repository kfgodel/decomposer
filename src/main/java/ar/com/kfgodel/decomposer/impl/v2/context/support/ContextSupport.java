package ar.com.kfgodel.decomposer.impl.v2.context.support;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.api.v2.DecomposerException;
import ar.com.kfgodel.tostring.ImplementedWithStringer;
import ar.com.kfgodel.tostring.Stringer;

import java.util.function.Supplier;

/**
 * This type serves as a base class to ease the implementation of a task context
 *
 * Created by kfgodel on 08/05/2015.
 */
public abstract class ContextSupport implements DecomposedContext {

    private Supplier<Object> sharedObject;

    @Override
    public void share(Object sharedObject) {
        this.sharedObject = ()-> sharedObject;
    }

    @Override
    public <R> R getShared() {
        if(!hasOwnSharedObject()){
            throw new DecomposerException("There's no shared object defined");
        }
        return (R) sharedObject.get();
    }

    protected boolean hasOwnSharedObject() {
        return sharedObject != null;
    }

    @Override
    @ImplementedWithStringer
    public String toString() {
        return Stringer.representationOf(this);
    }

}
