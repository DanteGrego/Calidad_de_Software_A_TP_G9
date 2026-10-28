package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 1 (conexion abierta, tabla con 20 asociados) sobre la clase BaseDeDatos.
 * Para JUnit 4: cambiar imports (Assertions -> Assert, BeforeEach -> Before, Test -> org.junit.Test).
 */
public class TestBaseDeDatosEscenario1 extends BaseIntegracion {

	@BeforeEach
	public void setUp() {
		// Escenario 1: ya lo deja armado BaseIntegracion
	}

	@Test
	public void testSingletonDevuelveMismaInstancia() {
		assertSame(BaseDeDatos.getInstancia(), BaseDeDatos.getInstancia());
		assertSame(bd, BaseDeDatos.getInstancia());
	}

	@Test
	public void testConexionNoNulaYValida() throws SQLException {
		assertNotNull(bd.getConexion());
		assertTrue(bd.getConexion().isValid(2));
	}

	@Test
	public void testIniciarBDCreaTablaConVeinteRegistros() throws SQLException {
		assertEquals(20, contarFilas());
	}

	@Test
	public void testIniciarBDEsIdempotente() throws SQLException {
		bd.iniciarBD();
		assertEquals(20, contarFilas());
	}

	@Test
	public void testDatosSemillaCorrectos() {
		fail("Pendiente: verificar campos de la semilla con valorColumna(dni, columna). "
				+ "Ojo: ciudad del registro 4 ('Jose Concurrencia', DNI 21842190) tiene un espacio inicial");
	}

	@Test
	public void testDniUnicoRechazaDuplicados() {
		try {
			bd.ejecutarInsert(INSERT, crearDTO("12345678")); // DNI ya existente en la semilla
			fail("Deberia lanzar SQLException por DNI duplicado");
		} catch (SQLException e) {
			// correcto
		}
	}
}