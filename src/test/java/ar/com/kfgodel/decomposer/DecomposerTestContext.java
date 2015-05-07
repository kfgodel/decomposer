package ar.com.kfgodel.decomposer;

import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.kfgodel.decomposer.api.v2.Decomposer;
import ar.com.kfgodel.decomposer.impl.ProcesadorDeParticionables;

import java.util.function.Supplier;

/**
 * This type defines a common test context used for different tests
 * Created by kfgodel on 03/05/2015.
 */
public interface DecomposerTestContext extends TestContext {

    ProcesadorDeParticionables processor();
    void processor(Supplier<ProcesadorDeParticionables> definition);

    Decomposer decomposer();
    void decomposer(Supplier<Decomposer> definition);
}
