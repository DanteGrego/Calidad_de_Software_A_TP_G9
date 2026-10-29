package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 3 (conexion cerrada) sobre la clase DataAccessObject.
 * Toda operacion debe terminar en SQLException.
 * No usar los helpers contarFilas/existeDni/valorColumna: usan la misma conexion cerrada.
 */
public class TestDataAccessObjectEscenario3 extends BaseIntegracion {

	@BeforeEach
	public void setUp() throws SQLException {
		bd.cerrarConexion();
	}

	@Test
	public void testCargarBDLanzaSQLException() {
		try {
			dao.cargarBD();
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testCargarConLimiteLanzaSQLException() {
		try {
			dao.cargarConLimite(5);
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testCargarPorIdLanzaSQLException() {
		try {
			dao.cargar(1);
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testEliminarLanzaSQLException() {
		try {
			dao.eliminar("12345678");
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testGuardarLanzaSQLException() {
		fail("Pendiente: guardar un Asociado y esperar SQLException (try / fail / catch)");
	}

	@Test
	public void testActualizarLanzaSQLException() {
		fail("Pendiente: actualizar un Asociado y esperar SQLException (try / fail / catch)");
	}

	@Test
	public void testFlujoSeInterrumpeYNoQuedaNadaPersistido() {
		fail("Pendiente: guardar lanza SQLException; reabrir la conexion y verificar que no hay fila nueva");
	}
}