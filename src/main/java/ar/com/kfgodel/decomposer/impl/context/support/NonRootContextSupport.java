package ar.com.kfgodel.decomposer.impl.context.support;

import ar.com.kfgodel.decomposer.api.context.DecomposedContext;

/**
 * This type serves as base class for non root contexts
 * Created by kfgodel on 08/05/2015.
 */
public abstract class NonRootContextSupport extends ContextSupport {

    private DecomposedContext parentContext;

    @Override
    public <R> R getShared() {
        if(hasOwnSharedObject()){
            // There's an object defined in this context
            return super.getShared();
        }
        // If not use the one inherited
        return parentContext.getShared();
    }

    protected void setParentContext(DecomposedContext parentContext) {
        this.parentContext = parentContext;
    }

    protected DecomposedContext getParentContext() {
        return parentContext;
    }

}
