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

	/*
	@Test
	public void testCargarIdSobreTablaVacia() {
		fail("Comportamiento indefinido por el contrato, no especifica si cargar un id inexistente debe lanzar excepcion o devolver null");
		//lo mismo que antes en escenario 1, cargar id inexistente no esta especificado que debe hacer
	}
	*/

	@Test
	public void testGuardarEnTablaVaciaCreaUnaFila() throws SQLException {
		dao.guardar(this.crearAsociado("111000111"));
		assertEquals(this.contarFilas(), 1);
	}

	@Test
	public void testEliminarEnTablaVaciaNoFalla() throws SQLException {
		dao.eliminar("12345678");
		assertEquals(0, contarFilas());
	}

	@Test
	public void testActualizarSobreTablaVaciaNoInsertaNiFalla() throws SQLException {
		dao.actualizar(this.crearAsociado("12345678"));
		assertEquals(this.contarFilas(), 0);
	}

	@Test
	public void testGuardarCargarEliminarDesdeCero() throws SQLException {
		dao.guardar(this.crearAsociado("111000111"));
		assertEquals(this.contarFilas(), 1);
		dao.cargar(1);
		assertEquals(this.contarFilas(), 1);
		dao.eliminar("111000111");
		assertEquals(this.contarFilas(), 0);
	}
}