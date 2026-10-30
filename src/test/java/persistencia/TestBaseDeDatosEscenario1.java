package persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 1 (conexion abierta, tabla con 20 asociados) sobre la clase BaseDeDatos.
 *
 * Pautas aplicadas:
 *  - No se usan lambdas anonimas ni X.class: las excepciones esperadas se
 *    verifican con try/fail/catch al estilo JUnit 4.
 *  - No se testean precondiciones (los 'assert' internos de BaseDeDatos):
 *    violar una precondicion es comportamiento indefinido, no un bug.
 *
 * Para JUnit 4: cambiar imports (Assertions -> Assert, BeforeEach -> @Before,
 * Test -> org.junit.Test).
 */
public class TestBaseDeDatosEscenario1 extends BaseIntegracion {

    @BeforeEach
    public void setUp() {
        // Escenario 1: BaseIntegracion ya deja la tabla con los 20 registros semilla
    }

    // ================= Singleton y conexion =================

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
    public void testCerrarConexionDejaConexionCerrada() throws SQLException {
        bd.cerrarConexion();
        assertTrue(bd.getConexion().isClosed(),
            "Tras cerrarConexion(), la conexion debe estar cerrada");
    }

    @Test
    public void testGetInstanciaTrasCerrarConexionDevuelveInstanciaConConexionCerrada() throws SQLException {
        bd.cerrarConexion();
        BaseDeDatos otra = BaseDeDatos.getInstancia();
        // El Singleton NO reabre la conexion: devuelve la misma instancia con conexion cerrada
        assertSame(bd, otra, "Debe ser la misma instancia");
        assertTrue(otra.getConexion().isClosed(),
            "El Singleton no reabre la conexion: queda inutilizable hasta resetear la instancia");
    }

    // ================= iniciarBD() =================

    @Test
    public void testIniciarBDCreaTablaConVeinteRegistros() throws SQLException {
        assertEquals(20, contarFilas());
    }

    @Test
    public void testIniciarBDEliminaDatosPrevios() throws SQLException {
        bd.ejecutarInsert(INSERT, crearDTO("77777777"));
        assertEquals(21, contarFilas());

        bd.iniciarBD(); // hace DROP TABLE -> borra el insertado

        assertEquals(20, contarFilas(), "iniciarBD() deberia dejar solo los 20 semilla");
        assertFalse(existeDni("77777777"), "El registro insertado no deberia sobrevivir");
    }

    @Test
    public void testDatosSemillaCorrectos() throws SQLException {
        // Registro 1
        assertEquals("Juan",   valorColumna("12345678", "nombre"));
        assertEquals("State",  valorColumna("12345678", "apellido"));
        assertEquals("Mar del Plata", valorColumna("12345678", "ciudad"));

        // Registro 4: la ciudad tiene un espacio inicial en el INSERT del codigo
        // (" Mar del Plata" con espacio). El test documenta ese dato tal cual esta.
        assertEquals(" Mar del Plata", valorColumna("21842190", "ciudad"),
            "El semilla del DNI 21842190 tiene un espacio inicial en ciudad (dato del codigo provisto)");

        // Registro 20 (ultimo)
        assertEquals("Adrian",   valorColumna("28293031", "nombre"));
        assertEquals("Composite", valorColumna("28293031", "apellido"));
    }

    // ================= ejecutarConsulta =================

    @Test
    public void testEjecutarConsultaConIdFiltraPorId() throws SQLException {
        ResultSet rs = bd.ejecutarConsulta("SELECT nombre FROM Asociados WHERE id = ?", 1);
        assertTrue(rs.next(), "Deberia devolver la fila con id=1");
        assertEquals("Juan", rs.getString("nombre"));
        assertFalse(rs.next(), "Solo deberia devolver una fila");
        rs.close();
    }

    @Test
    public void testEjecutarConsultaConIdInexistenteDevuelveVacio() throws SQLException {
        ResultSet rs = bd.ejecutarConsulta("SELECT * FROM Asociados WHERE id = ?", 99999);
        assertFalse(rs.next(), "No deberia haber filas para un id inexistente");
        rs.close();
    }

    @Test
    public void testEjecutarConsultaConSQLInvalidoLanzaSQLException() {
        try {
            bd.ejecutarConsulta("SELECT * FROM TablaQueNoExiste", -1);
            fail("Deberia tirar SQLException");
        } catch (SQLException e) {
            // correcto: la excepcion se esperaba
        }
    }

    // ================= ejecutarInsert =================

    @Test
    public void testDniUnicoRechazaDuplicados() {
        try {
            bd.ejecutarInsert(INSERT, crearDTO("12345678")); // DNI ya existente en la semilla
            fail("Deberia lanzar SQLException por DNI duplicado");
        } catch (SQLException e) {
            // correcto: la restriccion UNIQUE de la tabla rechazo el duplicado
        }
    }

    @Test
    public void testInsertYDeleteFuncionanSobreConexionReal() throws SQLException {
        bd.ejecutarInsert(INSERT, crearDTO("55555555"));
        assertTrue(existeDni("55555555"), "El insert deberia persistir");
        assertEquals(21, contarFilas());

        bd.ejecutarDelete("DELETE FROM Asociados WHERE DNI = ?", "55555555");
        assertFalse(existeDni("55555555"), "El delete deberia eliminar la fila");
        assertEquals(20, contarFilas());
    }

    // ================= ejecutarDelete =================

    @Test
    public void testEjecutarDeleteConDniInexistenteNoLanzaExcepcion() throws SQLException {
        int antes = contarFilas();
        bd.ejecutarDelete("DELETE FROM Asociados WHERE DNI = ?", "00000000");
        assertEquals(antes, contarFilas(), "Borrar un DNI inexistente no debe afectar la tabla");
    }

    // ================= ejecutarActualizacion (expone el bug) =================

    /**
     * BUG detectado: ejecutarActualizacion hardcodea stmt.setInt(8, 1), por lo
     * que siempre apunta a la fila con id=1 sin importar el DTO recibido.
     * Este test DEBE FALLAR en rojo: el assert espera que se modifique la fila
     * del DNI 87654321, pero el codigo modifica la fila 1 (DNI 12345678).
     */
    @Test
    public void testEjecutarActualizacionModificaFilaCorrecta() throws SQLException {
        // Constructor: (nombre, apellido, dni, ciudad, calle, numero, telefono)
        AsociadoDTO dto = new AsociadoDTO("MariaMod", "TemplateMod", "87654321",
                                          "Mar del Plata", "Calle Nueva", 999, "2230000000");

        bd.ejecutarActualizacion(
            "UPDATE Asociados SET nombre=?, apellido=?, dni=?, calle=?, numero=?, " +
            "ciudad=?, telefono=? WHERE id=?", dto);

        // El bug: el codigo actual siempre setea id=1 en el parametro 8
        // -> modifica la fila 1 (Juan), no la fila 2 (Maria)
        assertEquals("MariaMod", valorColumna("87654321", "nombre"),
            "El UPDATE deberia haber modificado la fila del DNI 87654321, pero el codigo " +
            "hardcodea id=1 -> este assert FALLA y expone el bug");
    }
}