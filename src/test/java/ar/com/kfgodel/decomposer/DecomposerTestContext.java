package ar.com.kfgodel.decomposer;

import ar.com.kfgodel.decomposer.api.Decomposer;
import info.kfgodel.jspek.api.contexts.TestContext;

import java.util.function.Supplier;

/**
 * This type defines a common test context used for different tests
 * Created by kfgodel on 03/05/2015.
 */
public interface DecomposerTestContext extends TestContext {

    Decomposer decomposer();
    void decomposer(Supplier<Decomposer> definition);
}
