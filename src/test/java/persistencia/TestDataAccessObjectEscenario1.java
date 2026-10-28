package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 1 (conexion abierta, tabla con 20 asociados) sobre la clase DataAccessObject.
 * Verificar las escrituras con los helpers JDBC de BaseIntegracion, NO con dao.cargar().
 *
 * CUIDADO con las precondiciones (assert): no usar try { ...; fail() } catch (AssertionError e),
 * porque fail() tambien lanza AssertionError y quedaria atrapado. Usar un flag boolean
 * (lanzo = true dentro del catch) y verificarlo con assertTrue despues.
 */
public class TestDataAccessObjectEscenario1 extends BaseIntegracion {

	@BeforeEach
	public void setUp() {
		// Escenario 1: ya lo deja armado BaseIntegracion
	}

	// ================= cargarBD =================

	@Test
	public void testCargarBDDevuelveVeinteAsociados() throws SQLException {
		assertEquals(20, dao.cargarBD().size());
	}

	@Test
	public void testCargarBDMapeaCamposCorrectamente() {
		fail("Pendiente: comparar el DTO de un asociado semilla con sus valores (nombre, apellido, dni, calle, numero, ciudad, telefono)");
	}

	// ================= cargarConLimite =================

	@Test
	public void testCargarConLimiteCinco() throws SQLException {
		assertEquals(5, dao.cargarConLimite(5).size());
	}

	@Test
	public void testCargarConLimiteUno() throws SQLException {
		assertEquals(1, dao.cargarConLimite(1).size());
	}

	@Test
	public void testCargarConLimiteIgualAlTotal() throws SQLException {
		assertEquals(20, dao.cargarConLimite(20).size());
	}

	@Test
	public void testCargarConLimiteMayorAlTotal() throws SQLException {
		assertEquals(20, dao.cargarConLimite(100).size());
	}

	@Test
	public void testCargarConLimiteCeroViolaPrecondicion() {
		fail("Pendiente: usar flag boolean (ver nota de la clase)");
	}

	@Test
	public void testCargarConLimiteNegativoViolaPrecondicion() {
		fail("Pendiente: usar flag boolean (ver nota de la clase)");
	}

	// ================= cargar por id =================

	@Test
	public void testCargarIdExistente() throws SQLException {
		AsociadoDTO a = dao.cargar(1); // hoy falla: cargar() no llama a resultado.next()
		assertEquals("Juan", a.getNombre());
		assertEquals("State", a.getApellido());
		assertEquals("12345678", a.getDni());
	}

	@Test
	public void testCargarIdInexistente() {
		fail("Pendiente: decidir comportamiento esperado (null o excepcion) para cargar(999)");
	}

	@Test
	public void testCargarIdCeroViolaPrecondicion() {
		fail("Pendiente: usar flag boolean (ver nota de la clase)");
	}

	// ================= guardar =================
	// Necesitan construir un Asociado del dominio (con Domicilio): revisar su constructor.

	@Test
	public void testGuardarAsociadoNuevoIncrementaFilas() {
		fail("Pendiente: guardar y verificar contarFilas() == 21");
	}

	@Test
	public void testGuardarMapeaCamposCorrectamente() {
		fail("Pendiente: guardar y comparar cada campo con valorColumna(dni, columna)");
	}

	@Test
	public void testGuardarDniDuplicadoLanzaSQLException() {
		fail("Pendiente: guardar un Asociado con DNI 12345678 y esperar SQLException (try / fail / catch)");
	}

	@Test
	public void testGuardarCampoExcedeVarchar() {
		fail("Pendiente: nombre de 31+ caracteres (el resultado depende del sql_mode de MySQL)");
	}

	@Test
	public void testGuardarNuloViolaPrecondicion() {
		fail("Pendiente: usar flag boolean (ver nota de la clase)");
	}

	// ================= eliminar =================

	@Test
	public void testEliminarDniExistente() throws SQLException {
		dao.eliminar("12345678");
		assertEquals(19, contarFilas());
		assertFalse(existeDni("12345678"));
	}

	@Test
	public void testEliminarDniInexistenteNoAlteraNada() throws SQLException {
		dao.eliminar("00000000");
		assertEquals(20, contarFilas());
	}

	@Test
	public void testEliminarDniVacioViolaPrecondicion() {
		fail("Pendiente: usar flag boolean (ver nota de la clase)");
	}

	@Test
	public void testGuardarYEliminarMismoAsociado() {
		fail("Pendiente: guardar un Asociado, eliminarlo por DNI y verificar contarFilas() == 20");
	}

	// ================= actualizar (acordar antes el criterio: por DNI o por id) =================

	@Test
	public void testActualizarModificaElAsociadoCorrecto() {
		fail("Pendiente: hoy siempre modifica id=1 (setInt(8, 1) hardcodeado)");
	}

	@Test
	public void testActualizarNoAlteraAlAsociadoConId1() {
		fail("Pendiente: actualizar otro asociado y verificar que el id=1 sigue igual");
	}

	@Test
	public void testActualizarConDniDeOtroLanzaSQLException() {
		fail("Pendiente: viola el UNIQUE (try / fail / catch)");
	}

	@Test
	public void testActualizarAsociadoInexistenteNoCambiaNada() {
		fail("Pendiente");
	}

	@Test
	public void testActualizarNuloViolaPrecondicion() {
		fail("Pendiente: usar flag boolean (ver nota de la clase)");
	}

	// ================= flujo completo =================

	@Test
	public void testGuardarCargarActualizarEliminar() {
		fail("Pendiente: guardar -> cargar -> actualizar -> eliminar, verificando el estado en cada paso");
	}
}