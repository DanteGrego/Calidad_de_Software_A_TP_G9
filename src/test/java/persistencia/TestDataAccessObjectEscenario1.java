package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.modeloDominio.personas.asociado.Asociado;

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
/*"(1, 'Juan', 'State', '12345678', 'Buenos Aires', 123, 'Mar del Plata', '2235551234'),\n" +
                "(2, 'Maria', 'Template', '87654321', 'Libertad', 742, 'Mar del Plata', '2235555678'),\n" +
                "(3, 'Carlos', 'Observer', '11223344', 'Colon', 456, 'Mar del Plata', '2235558765'),\n" +
                "(4, 'Jose', 'Concurrencia', '21842190', 'Juan B Justo', 200, ' Mar del Plata', '1551232349'),\n" +
                "(5, 'Xin', 'Dao', '33445566', 'Corrientes', 789, 'Mar del Plata', '2235554321'),\n" +
                "(6, 'Ana', 'Facade', '66554433', 'San Martin', 321, 'Mar del Plata', '2235556789'),\n" +
                "(7, 'Laura', 'Singleton', '22334455', 'Alem', 1450, 'Mar del Plata', '2236112233'),\n" +
                "(8, 'Martin', 'Factory', '33445577', 'Guemes', 2870, 'Mar del Plata', '2236223344'),\n" +
                "(9, 'Sofia', 'Builder', '44556677', 'Independencia', 1100, 'Mar del Plata', '2236334455'),\n" +
                "(10, 'David', 'Adapter', '55667788', 'Luro', 3450, 'Mar del Plata', '2236445566'),\n" +
                "(11, 'Elena', 'Decorator', '66778899', 'Cordoba', 2120, 'Mar del Plata', '2236556677'),\n" +
                "(12, 'Pedro', 'Proxy', '77889900', 'Rivadavia', 3000, 'Mar del Plata', '155889900'),\n" +
                "(13, 'Lucia', 'Strategy', '88990011', 'Belgrano', 2500, 'Mar del Plata', '2235001122'),\n" +
                "(14, 'Diego', 'Command', '99001122', 'Moreno', 3100, 'Mar del Plata', '2235112233'),\n" +
                "(15, 'Valeria', 'Iterator', '10112233', 'Jara', 850, 'Mar del Plata', '2235223344'),\n" +
                "(16, 'Miguel', 'Mediator', '12131415', 'Alberti', 1700, 'Mar del Plata', '2235334455'),\n" +
                "(17, 'Clara', 'Memento', '16171819', 'Garay', 1900, 'Mar del Plata', '155445566'),\n" +
                "(18, 'Sergio', 'Visitor', '20212223', 'Castelli', 2300, 'Mar del Plata', '2235556677'),\n" +
                "(19, 'Paula', 'Bridge', '24252627', 'Falucho', 2010, 'Mar del Plata', '2235667788'),\n" +
                "(20, 'Adrian', 'Composite', '28293031', 'Olavarria', 2700, 'Mar del Plata','2235778899');";*/
	@Test
	public void testCargarBDMapeaCamposCorrectamente() throws SQLException {
		//comparar el DTO de un asociado semilla con sus valores (nombre, apellido, dni, calle, numero, ciudad, telefono)
		//comparar columna aca no serviria, no queres ver la tabla sino lo que te devuelve?
		//registro 1
		AsociadoDTO dto = dao.cargarBD().get(0);
		assertEquals(dto.getNombre(), "Juan");
		assertEquals(dto.getApellido(), "State");
		assertEquals(dto.getDni(), "12345678");
		assertEquals(dto.getCalle(), "Buenos Aires");
		assertEquals(dto.getNumero(), 123);
		assertEquals(dto.getCiudad(), "Mar del Plata");
		assertEquals(dto.getTelefono(), "2235551234");
		
		//registro 4
		dto = dao.cargarBD().get(3);
		assertEquals(dto.getNombre(), "Jose");
		assertEquals(dto.getApellido(), "Concurrencia");
		assertEquals(dto.getDni(), "21842190");
		assertEquals(dto.getCalle(), "Juan B Justo");
		assertEquals(dto.getNumero(), 200);
		assertEquals(dto.getCiudad(), " Mar del Plata");
		assertEquals(dto.getTelefono(), "1551232349");
		
		//registro 20
		dto = dao.cargarBD().get(19);
		assertEquals(dto.getNombre(), "Adrian");
		assertEquals(dto.getApellido(), "Composite");
		assertEquals(dto.getDni(), "28293031");
		assertEquals(dto.getCalle(), "Olavarria");
		assertEquals(dto.getNumero(), 2700);
		assertEquals(dto.getCiudad(), "Mar del Plata");
		assertEquals(dto.getTelefono(), "2235778899");
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
	public void testCargarConLimiteCeroViolaPrecondicion() throws SQLException{
		boolean lanzo = false;
		try {
			dao.cargarConLimite(0);
		} catch (AssertionError e) {
			//bien: lanzo excepcion
			lanzo = true;
		}
		
		assertTrue(lanzo);
	}

	@Test
	public void testCargarConLimiteNegativoViolaPrecondicion() throws SQLException {
		boolean lanzo = false;
		try {
			dao.cargarConLimite(-1);
		} catch (AssertionError e) {
			//bien: lanzo excepcion
			lanzo = true;
		}
		
		assertTrue(lanzo);
	}

	// ================= cargar por id =================

	@Test
	public void testCargarIdExistente() throws SQLException {
		AsociadoDTO a = dao.cargar(1); // hoy falla: cargar() no llama a resultado.next()
		assertEquals("Juan", a.getNombre());
		assertEquals("State", a.getApellido());
		assertEquals("12345678", a.getDni());
	}

	/*
	@Test
	public void testCargarIdInexistente() {
		fail("Comportamiento indefinido por el contrato, no especifica si cargar un id inexistente debe lanzar excepcion o devolver null");
	}
	*/

	@Test
	public void testCargarIdCeroViolaPrecondicion() throws SQLException {
		boolean lanzo = false;
		try {
			dao.cargar(0);
		} catch (AssertionError e) {
			//bien: lanzo excepcion
			lanzo = true;
		}
		
		assertTrue(lanzo);
	}

	// ================= guardar =================
	// Necesitan construir un Asociado del dominio (con Domicilio): revisar su constructor.

	@Test
	public void testGuardarAsociadoNuevoIncrementaFilas() throws SQLException {
		dao.guardar(this.crearAsociado("111000111"));
		assertEquals(this.contarFilas(), 21);
	}

	@Test
	public void testGuardarMapeaCamposCorrectamente() throws SQLException {
		dao.guardar(this.crearAsociado("111000111"));
		
		assertEquals(this.valorColumna("111000111", "id"), "21");
		assertEquals(this.valorColumna("111000111", "nombre"), "Test");
		assertEquals(this.valorColumna("111000111", "apellido"), "Prueba");
		assertEquals(this.valorColumna("111000111", "dni"), "111000111");
		assertEquals(this.valorColumna("111000111", "ciudad"), "Mar del Plata");
		assertEquals(this.valorColumna("111000111", "calle"), "Calle Test");
		assertEquals(this.valorColumna("111000111", "numero"), "1");
		assertEquals(this.valorColumna("111000111", "telefono"), "2230000000");
	}

	@Test
	public void testGuardarDniDuplicadoLanzaSQLException() {
		try {
			dao.guardar(this.crearAsociado("12345678"));
			fail("Deberia lanzar SQLException por querer insertar duplicado");
		} catch (SQLException e) {
			//bien
		}
	}

	/*
	@Test
	public void testGuardarCampoExcedeVarchar() {
		fail("Comportamiento indefinido por el contrato, no especifica si guardar un dni que excede varchar debe lanzar excepcion o no");
	}
	*/

	@Test
	public void testGuardarNuloViolaPrecondicion() throws SQLException {
		boolean lanzo = false;
		try {
			dao.guardar(null);
		} catch (AssertionError e) {
			//bien: lanzo excepcion
			lanzo = true;
		}
		
		assertTrue(lanzo);
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
	public void testEliminarDniVacioViolaPrecondicion() throws SQLException {
		boolean lanzo = false;
		try {
			dao.eliminar("");
		} catch (AssertionError e) {
			//bien: lanzo excepcion
			lanzo = true;
		}
		
		assertTrue(lanzo);
	}

	@Test
	public void testGuardarYEliminarMismoAsociado() throws SQLException {
		//guardar un Asociado, eliminarlo por DNI y verificar contarFilas() == 20
		dao.guardar(this.crearAsociado("111000111"));
		dao.eliminar("111000111");
		assertEquals(this.contarFilas(), 20);
	}

	// ================= actualizar (criterio: por id o dni?) =================

	//suponiendo que actualizar() tiene solo un parametro y es Asociado, se toma como identificador su dni, actualiza el Asociado con su dni
	@Test
	public void testActualizarModificaElAsociadoCorrecto() throws SQLException {
		//registro 1
		dao.actualizar(this.crearAsociado("12345678"));
		assertEquals(this.valorColumna("12345678", "id"), 1);
		assertEquals(this.valorColumna("12345678", "nombre"), "Test");
		assertEquals(this.valorColumna("12345678", "apellido"), "Prueba");
		assertEquals(this.valorColumna("12345678", "dni"), "12345678");
		assertEquals(this.valorColumna("12345678", "ciudad"), "Mar del Plata");
		assertEquals(this.valorColumna("12345678", "calle"), "Calle Test");
		assertEquals(this.valorColumna("12345678", "numero"), 1);
		assertEquals(this.valorColumna("12345678", "telefono"), "223000000");
		
		//registro 17
		dao.actualizar(this.crearAsociado("16171819"));
		assertEquals(this.valorColumna("16171819", "id"), 2);
		assertEquals(this.valorColumna("16171819", "nombre"), "Test");
		assertEquals(this.valorColumna("16171819", "apellido"), "Prueba");
		assertEquals(this.valorColumna("16171819", "dni"), "87654321");
		assertEquals(this.valorColumna("16171819", "ciudad"), "Mar del Plata");
		assertEquals(this.valorColumna("16171819", "calle"), "Calle Test");
		assertEquals(this.valorColumna("16171819", "numero"), 1);
		assertEquals(this.valorColumna("16171819", "telefono"), "223000000");
		
		//registro 20
		dao.actualizar(this.crearAsociado("28293031"));
		assertEquals(this.valorColumna("28293031", "id"), 2);
		assertEquals(this.valorColumna("28293031", "nombre"), "Test");
		assertEquals(this.valorColumna("28293031", "apellido"), "Prueba");
		assertEquals(this.valorColumna("28293031", "dni"), "87654321");
		assertEquals(this.valorColumna("28293031", "ciudad"), "Mar del Plata");
		assertEquals(this.valorColumna("28293031", "calle"), "Calle Test");
		assertEquals(this.valorColumna("28293031", "numero"), 1);
		assertEquals(this.valorColumna("28293031", "telefono"), "223000000");
	}

	@Test
	public void testActualizarNoAlteraAlAsociadoConId1() throws SQLException {
		//actualizo registro 2
		dao.actualizar(this.crearAsociado("876543210"));
		assertEquals(this.valorColumna("123456789", "id"), 1);
		assertEquals(this.valorColumna("123456789", "nombre"), "Juan");
		assertEquals(this.valorColumna("123456789", "apellido"), "State");
		assertEquals(this.valorColumna("123456789", "dni"), "12345678");
		assertEquals(this.valorColumna("123456789", "calle"), "Buenos Aires");
		assertEquals(this.valorColumna("123456789", "numero"), 123);
		assertEquals(this.valorColumna("123456789", "ciudad"), "Mar del Plata");
		assertEquals(this.valorColumna("123456789", "telefono"), "2235551234");
	}

	@Test
	public void testActualizarConDniDeOtroLanzaSQLException() {
		//al usar dni de identificador nunca voy a poder cambiarselo a un asociado como para que repita el dni de otro
		//segun el criterio usado es imposible de probar pues nunca pasara con actualizar()
		//fail("Pendiente: viola el UNIQUE (try / fail / catch)");
	}

	@Test
	public void testActualizarAsociadoInexistenteNoCambiaNada() throws SQLException {
		dao.actualizar(this.crearAsociado("111000111"));
		assertEquals(this.contarFilas(), 20);
		assertEquals(this.valorColumna("12345678", "nombre"), "Juan");
	}

	@Test
	public void testActualizarNuloViolaPrecondicion() throws SQLException {
		boolean lanzo = false;
		try {
			dao.actualizar(null);
		} catch (AssertionError e) {
			//bien: lanzo excepcion
			lanzo = true;
		}
		
		assertTrue(lanzo);
	}

	// ================= flujo completo =================

	@Test
	public void testGuardarCargarActualizarEliminar() throws SQLException {
		//Pendiente: guardar -> cargar -> actualizar -> eliminar, verificando el estado en cada paso
		Asociado a = this.crearAsociado("111000111");
		dao.guardar(a);
		assertEquals(this.contarFilas(), 21);
	
		AsociadoDTO dto = dao.cargar(21);
		assertEquals(this.contarFilas(), 21);
		assertEquals(dto.getNombre(), "Test");
		assertEquals(dto.getApellido(), "Prueba");
		assertEquals(dto.getDni(), "111000111");
		assertEquals(dto.getCalle(), "Calle Test");
		assertEquals(dto.getNumero(), 1);
		assertEquals(dto.getCiudad(), "Mar del Plata");
		assertEquals(dto.getTelefono(), "223000000");
		//quiza el chequear los valores dentro de lo que se guardo esta de mas (sacar si es el caso)
		a.setNombre("Daniel");
		a.setApellido("no anda");
		dao.actualizar(a);
		assertEquals(this.contarFilas(), 21);
		dto = dao.cargar(21);
		assertEquals(this.contarFilas(), 21);
		assertEquals(dto.getNombre(), "Daniel");
		assertEquals(dto.getApellido(), "no anda");
		assertEquals(dto.getDni(), "111000111");
		assertEquals(dto.getCalle(), "Calle Test");
		assertEquals(dto.getNumero(), 1);
		assertEquals(dto.getCiudad(), "Mar del Plata");
		assertEquals(dto.getTelefono(), "223000000");
		
		dao.eliminar("111000111");
		assertEquals(this.contarFilas(), 20);
	}
}