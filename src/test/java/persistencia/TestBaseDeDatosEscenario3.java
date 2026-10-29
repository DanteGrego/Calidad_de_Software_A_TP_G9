package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 3 (conexion cerrada) sobre la clase BaseDeDatos.
 * No usar los helpers contarFilas/existeDni/valorColumna: usan la misma conexion cerrada.
 */
public class TestBaseDeDatosEscenario3 extends BaseIntegracion {

	@BeforeEach
	public void setUp() throws SQLException {
		bd.cerrarConexion();
	}

	@Test
	public void testConexionQuedaCerrada() throws SQLException {
		assertTrue(bd.getConexion().isClosed());
	}

	@Test
	public void testCerrarConexionDosVecesNoFalla() {
		try {
			bd.cerrarConexion();
		} catch (SQLException e) {
			fail("Cerrar una conexion ya cerrada no deberia lanzar excepcion");
		}
	}

	@Test
	public void testEjecutarConsultaLanzaSQLException() {
		try {
			bd.ejecutarConsulta("SELECT * FROM Asociados ;", -1);
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testEjecutarInsertLanzaSQLException() {
		try {
			bd.ejecutarInsert(INSERT, crearDTO("11111111"));
			fail("Deberia lanzar SQLException");
		} catch (SQLException e) {
			// correcto
		}
	}

	@Test
	public void testGetInstanciaTrasCerrarDevuelveConexionCerrada() {
		fail("Pendiente: decidir comportamiento esperado (el Singleton no reabre la conexion) y documentarlo como hallazgo");
	}

	@Test
	public void testIniciarBDConConexionCerradaNoRepuebla() {
		fail("Pendiente: iniciarBD() traga las SQLException con printStackTrace; "
				+ "verificar (reabriendo la conexion) que la tabla no se repuebla y documentar el hallazgo");
	}
}