package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 2 (conexion abierta, tabla vacia) sobre la clase DataAccessObject.
 */
public class TestDataAccessObjectEscenario2 extends BaseIntegracion {

	@BeforeEach
	public void setUp() throws SQLException {
		vaciarTabla();
	}

	@Test
	public void testCargarBDDevuelveListaVacia() throws SQLException {
		assertEquals(0, dao.cargarBD().size());
	}

	@Test
	public void testCargarConLimiteDevuelveListaVacia() throws SQLException {
		assertEquals(0, dao.cargarConLimite(5).size());
	}

	@Test
	public void testCargarIdSobreTablaVacia() {
		fail("Pendiente: decidir comportamiento esperado de cargar(1) sin filas (null o excepcion)");
	}

	@Test
	public void testGuardarEnTablaVaciaCreaUnaFila() {
		fail("Pendiente: guardar un Asociado y verificar contarFilas() == 1");
	}

	@Test
	public void testEliminarEnTablaVaciaNoFalla() throws SQLException {
		dao.eliminar("12345678");
		assertEquals(0, contarFilas());
	}

	@Test
	public void testActualizarSobreTablaVaciaNoInsertaNiFalla() {
		fail("Pendiente: actualizar un Asociado y verificar que contarFilas() sigue en 0");
	}

	@Test
	public void testGuardarCargarEliminarDesdeCero() {
		fail("Pendiente: guardar -> cargar(1) -> eliminar -> tabla vacia otra vez");
	}
}