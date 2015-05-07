package ar.com.kfgodel.decomposer.api.v2;

import java.util.List;

/**
 * This type represents the context of a task execution that allows sharing of state between tasks
 * and their sub tasks
 *
 * Created by kfgodel on 06/05/2015.
 */
public interface DecomposedContext {
    void share(Object sharedObject);

    <R> R getShared();

    <R> R getSubTaskResult();

    <R> List<R> getSubTaskResults();
}
