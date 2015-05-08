package ar.com.kfgodel.decomposer.api.v2;

import ar.com.kfgodel.decomposer.impl.v2.results.MultiPrerequisiteResult;
import ar.com.kfgodel.decomposer.impl.v2.results.NoPrerequisitesResult;
import ar.com.kfgodel.decomposer.impl.v2.results.OnlyPrerequisiteResult;

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

    static DelayedResult waitingFor(List<DecomposableTask> subtasks) {
        if(subtasks.isEmpty()){
            return NoPrerequisitesResult.create();
        }
        if(subtasks.size() == 1){
            return OnlyPrerequisiteResult.create(subtasks.get(0));
        }
        return MultiPrerequisiteResult.create(subtasks);
    }
}
