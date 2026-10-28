package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 2 (conexion abierta, tabla vacia) sobre la clase BaseDeDatos.
 */
public class TestBaseDeDatosEscenario2 extends BaseIntegracion {

	@BeforeEach
	public void setUp() throws SQLException {
		vaciarTabla();
	}

	@Test
	public void testTablaQuedaVacia() throws SQLException {
		assertEquals(0, contarFilas());
	}

	@Test
	public void testIniciarBDRepueblaLaTabla() throws SQLException {
		bd.iniciarBD();
		assertEquals(20, contarFilas());
	}

	@Test
	public void testEjecutarConsultaNoDevuelveFilas() throws SQLException {
		ResultSet rs = bd.ejecutarConsulta("SELECT * FROM Asociados ;", -1);
		assertFalse(rs.next());
		rs.close();
	}

	@Test
	public void testEjecutarInsertEnTablaVaciaCreaUnaFila() throws SQLException {
		bd.ejecutarInsert(INSERT, crearDTO("11111111"));
		assertEquals(1, contarFilas());
		assertTrue(existeDni("11111111"));
	}
}