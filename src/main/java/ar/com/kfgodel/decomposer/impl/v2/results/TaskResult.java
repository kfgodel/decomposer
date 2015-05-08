package ar.com.kfgodel.decomposer.impl.v2.results;

import ar.com.kfgodel.decomposer.api.v2.DecomposedContext;
import ar.com.kfgodel.decomposer.impl.v2.execution.TaskExecution;

import java.util.List;
import java.util.function.Supplier;

/**
 * This type represents an abstraction over two kinds of results: direct or delayed.
 * Created by kfgodel on 07/05/2015.
 */
public interface TaskResult extends Supplier<Object> {

    List<TaskExecution> getPrerequisites(DecomposedContext parentContext);

}
