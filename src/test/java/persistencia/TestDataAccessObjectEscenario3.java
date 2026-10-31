package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.DriverManager;
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
		try {
			dao.guardar(this.crearAsociado("111000111"));
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testActualizarLanzaSQLException() {
		try {
			dao.actualizar(this.crearAsociado("111000111"));
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testFlujoSeInterrumpeYNoQuedaNadaPersistido() throws SQLException {
		//guardar lanza SQLException; reabrir la conexion y verificar que no hay fila nueva
		try {
			dao.guardar(this.crearAsociado("111000111"));
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
		fail("No hay manera de reabrir la conexion y no modificar la tabla de asociados en la base de datos con los metodos provistos");
	}
}