package persistencia;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Suite de integracion del modulo de persistencia: agrupa las 6 clases (clase bajo prueba x escenario).
 * No tiene tests propios ni hereda de BaseIntegracion. Requiere MySQL en ejecucion.
 *
 * Es el UNICO archivo que cambia entre JUnit 5 y JUnit 4. Equivalente en JUnit 4:
 *
 *   import org.junit.runner.RunWith;
 *   import org.junit.runners.Suite;
 *   import org.junit.runners.Suite.SuiteClasses;
 *
 *   @RunWith(Suite.class)
 *   @SuiteClasses({ TestBaseDeDatosEscenario1.class, ... })
 *   public class SuiteIntegracionPersistencia { }
 *
 * En JUnit 5 requiere la dependencia junit-platform-suite.
 */
@Suite
@SelectClasses({
		TestBaseDeDatosEscenario1.class,
		TestBaseDeDatosEscenario2.class,
		TestBaseDeDatosEscenario3.class,
		TestDataAccessObjectEscenario1.class,
		TestDataAccessObjectEscenario2.class,
		TestDataAccessObjectEscenario3.class })
public class SuiteIntegracionPersistencia {
}

