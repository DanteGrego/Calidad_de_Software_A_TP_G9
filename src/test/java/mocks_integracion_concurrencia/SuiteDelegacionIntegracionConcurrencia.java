package mocks_integracion_concurrencia;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Suite de pruebas para el Punto 3: Testing de integración con Mocks, Máquina de Estados y Concurrencia.
 * Agrupa:
 *  1. AmbulanciaDelegacionTest (Pruebas unitarias de delegación con Mocks)
 *  2. AmbulanciaStateGetterSetterTest (Pruebas de getter/setter de estado)
 *  3. AmbulanciaTransicionEstadosTest (Pruebas de integración de la máquina de estados)
 *  4. AmbulanciaConcurrenciaTest (Pruebas de concurrencia, sincronización y RetornoAutomatico)
 */
@Suite
@SelectClasses({
    AmbulanciaDelegacionTest.class,
    AmbulanciaTransicionesTest.class,
    AmbulanciaConcurrenciaTest.class
})
public class SuiteDelegacionIntegracionConcurrencia {
}

