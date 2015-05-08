package ar.com.kfgodel.decomposer.api.results;

import ar.com.kfgodel.decomposer.api.DecomposableTask;
import ar.com.kfgodel.decomposer.impl.results.subtasks.MultiPrerequisiteResult;
import ar.com.kfgodel.decomposer.impl.results.subtasks.NoPrerequisitesResult;
import ar.com.kfgodel.decomposer.impl.results.subtasks.OnePrerequisiteResult;

import java.util.Arrays;
import java.util.List;

/**
 * This type serves as an access point to define results that need to wait execution of other
 * tasks.
 *
 * Created by kfgodel on 07/05/2015.
 */
public interface DelayResult {

    /**
     * Facility method to indicate prerequisite tasks manually using the vararg notation.<br>
     *     This method creates a list and delegates to until(List) method.
     * @param nextTasks The vararg array of child tasks to execute before obtaining the end result
     * @return The delayed result
     */
    static DelayedResult waitingFor(DecomposableTask... nextTasks) {
        return waitingFor(Arrays.asList(nextTasks));
    }

    /**
     * Creates a delayed task that will wait for the given task list resolution before
     * being available
     * @param subtasks The list of tasks that need to be run before accessing the result
     * @return The delayed result
     */
    static DelayedResult waitingFor(List<DecomposableTask> subtasks) {
        if(subtasks.isEmpty()){
            return NoPrerequisitesResult.create();
        }
        if(subtasks.size() == 1){
            return OnePrerequisiteResult.create(subtasks.get(0));
        }
        return MultiPrerequisiteResult.create(subtasks);
    }
}
