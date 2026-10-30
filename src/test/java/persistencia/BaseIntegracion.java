package persistencia;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Clase base de los tests de integracion. NO tiene @Test propios.
 *
 * Deja a todos los escenarios en el mismo punto de partida: conexion abierta y tabla
 * Asociados recien creada con los 20 registros semilla. Cada subclase agrega, en su propio
 * setUp(), la modificacion que define su escenario:
 *   - Escenario 1 (con 20 asociados): nada
 *   - Escenario 2 (tabla vacia):      vaciarTabla()
 *   - Escenario 3 (sin conexion):     bd.cerrarConexion()
 *
 * IMPORTANTE: los setUp de las subclases NO deben llamarse setUpBase ni cleanUpBase
 * (si tuvieran el mismo nombre, anularian a los de esta clase y no se ejecutarian)
 * JUnit siempre ejecuta primero el setUp del padre y luego el del hijo, no hay peligro en eso.
 *
 * Portabilidad a JUnit 4: cambiar solo los imports (@BeforeEach -> @Before, @AfterEach -> @After).
 *
 * Requiere MySQL en localhost:3306 con usuario progra_c / progra_c.
 */
public abstract class BaseIntegracion {

	protected static final String INSERT = "INSERT INTO Asociados(nombre, apellido, DNI, calle, numero, ciudad, telefono) VALUES (?, ?, ?, ?, ?, ?, ?);";

	protected BaseDeDatos bd;
	protected DataAccessObject dao;

	// ================= Preparacion y limpieza comunes =================

	@BeforeEach
	public void setUpBase() {
		bd = BaseDeDatos.getInstancia();
		dao = new DataAccessObject();
	}

	@AfterEach
	public void cleanUpBase() throws Exception {
		if (bd != null) {
			try {
				bd.cerrarConexion();
			} catch (SQLException e) {
				// ya estaba cerrada
			}
		}
		// Resetea el Singleton para que el proximo test abra una conexion nueva
		Field f = BaseDeDatos.class.getDeclaredField("instancia");
		f.setAccessible(true);
		f.set(null, null);
	}

	// ================= Armado de escenarios =================

	/** Escenario 2: deja la tabla existente pero sin filas (los ids vuelven a 1). */
	protected void vaciarTabla() throws SQLException {
		Statement st = bd.getConexion().createStatement();
		st.execute("TRUNCATE TABLE Asociados");
		st.close();
	}

	// ================= Helpers de verificacion (JDBC directo, no dependen del DAO) =================
	// Solo usar con la conexion abierta (escenarios 1 y 2).

	/** Cantidad de filas de la tabla Asociados. 
	 *Testeado que funciona*/
	protected int contarFilas() throws SQLException {
		Connection c = bd.getConexion();
		PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM Asociados");
		ResultSet rs = ps.executeQuery();
		rs.next();
		int cantidad = rs.getInt(1);
		rs.close();
		ps.close();
		return cantidad;
	}

	/** true si existe un asociado con ese DNI. */
	protected boolean existeDni(String dni) throws SQLException {
		Connection c = bd.getConexion();
		PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM Asociados WHERE dni = ?");
		ps.setString(1, dni);
		ResultSet rs = ps.executeQuery();
		rs.next();
		boolean existe = rs.getInt(1) > 0;
		rs.close();
		ps.close();
		return existe;
	}

	/** Valor de una columna (como String) del asociado con ese DNI, o null si no existe. 
	 * testeado que funciona
	 * @param dni valor buscado
	 * @param columna (nombre/apellido/etc...)
	 * @return
	 * @throws SQLException
	 */
	protected String valorColumna(String dni, String columna) throws SQLException {
		Connection c = bd.getConexion();
		PreparedStatement ps = c.prepareStatement("SELECT " + columna + " FROM Asociados WHERE dni = ?");
		ps.setString(1, dni);
		ResultSet rs = ps.executeQuery();
		String valor = rs.next() ? rs.getString(1) : null;
		rs.close();
		ps.close();
		return valor;
	}

	/** DTO de prueba valido con el DNI indicado. */
	protected AsociadoDTO crearDTO(String dni) {
		return new AsociadoDTO("Test", "Prueba", dni, "Mar del Plata", "Calle Test", 1, "2230000000");
	}
}