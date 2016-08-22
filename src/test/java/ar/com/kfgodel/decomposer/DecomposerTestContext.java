package ar.com.kfgodel.decomposer;

import ar.com.dgarcia.javaspec.api.contexts.TestContext;
import ar.com.kfgodel.decomposer.api.Decomposer;

import java.util.function.Supplier;

/**
 * This type defines a common test context used for different tests
 * Created by kfgodel on 03/05/2015.
 */
public interface DecomposerTestContext extends TestContext {

    Decomposer decomposer();
    void decomposer(Supplier<Decomposer> definition);
}
