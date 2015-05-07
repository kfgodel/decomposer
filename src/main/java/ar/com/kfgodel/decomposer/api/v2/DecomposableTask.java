package ar.com.kfgodel.decomposer.api.v2;

/**
 * This type represents a task that can be decomposed in sub tasks to be processed to process it
 * Created by kfgodel on 06/05/2015.
 */
@FunctionalInterface
public interface DecomposableTask {

    Object executeUnder(DecomposedContext taskContext);
}
