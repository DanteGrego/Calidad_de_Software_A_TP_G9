package persistencia;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 3 (conexion cerrada) sobre la clase BaseDeDatos.
 *
 * El estado "conexion cerrada" lo arma esta clase en setUp(); NO es codigo
 * bajo prueba. Por eso no se testea el cierre en si (eso seria testear JDBC),
 * solo se verifica como sanity check que el escenario quedo bien armado.
 *
 * No se usan los helpers contarFilas/existeDni/valorColumna: dependen de la
 * conexion, que en este escenario esta cerrada.
 *
 * No se testean precondiciones (los 'assert' internos de BaseDeDatos):
 * violar una precondicion es comportamiento indefinido, no un bug.
 *
 * NOTA: el hallazgo "iniciarBD() silencia las SQLException" NO se testea aca
 * porque iniciarBD() no declara 'throws' y atrapa internamente: por firma,
 * es imposible que propague la excepcion al cliente. Se documenta en el
 * informe como hallazgo de diseno, no como test.
 *
 * Para JUnit 4: cambiar imports (Assertions -> Assert, BeforeEach -> @Before,
 * Test -> org.junit.Test).
 */
public class TestBaseDeDatosEscenario3 extends BaseIntegracion {

    @BeforeEach
    public void setUp() throws SQLException {
        bd.cerrarConexion();
    }

    // ================= Sanity check del arnes (no es codigo bajo prueba) =================

    @Test
    public void testEscenarioQuedaArmadoConConexionCerrada() throws SQLException {
        assertTrue(bd.getConexion().isClosed(),
            "El escenario 3 requiere conexion cerrada");
    }

    // ================= cerrarConexion() =================

    @Test
    public void testCerrarConexionDosVecesNoFalla() {
        try {
            bd.cerrarConexion();
        } catch (SQLException e) {
            fail("Cerrar una conexion ya cerrada no deberia lanzar excepcion");
        }
    }

    // ================= getConexion() sobre conexion cerrada =================

    @Test
    public void testConexionCerradaNoPermiteCrearStatement() {
        try {
            bd.getConexion().createStatement();
            fail("Una conexion cerrada no deberia permitir crear un Statement");
        } catch (SQLException e) {
            // correcto: la conexion esta cerrada
        }
    }

    // ================= CRUD con conexion cerrada: todos lanzan SQLException =================

    @Test
    public void testEjecutarConsultaLanzaSQLException() {
        try {
            bd.ejecutarConsulta("SELECT * FROM Asociados;", -1);
            fail("Deberia lanzar SQLException con conexion cerrada");
        } catch (SQLException e) {
            // correcto
        }
    }

    @Test
    public void testEjecutarInsertLanzaSQLException() {
        try {
            bd.ejecutarInsert(INSERT, crearDTO("11111111"));
            fail("Deberia lanzar SQLException con conexion cerrada");
        } catch (SQLException e) {
            // correcto
        }
    }

    @Test
    public void testEjecutarActualizacionLanzaSQLException() {
        // Constructor: (nombre, apellido, dni, ciudad, calle, numero, telefono)
        AsociadoDTO dto = new AsociadoDTO("X", "Y", "11111111",
                                          "Ciudad", "Calle", 1, "2230000000");
        try {
            bd.ejecutarActualizacion(
                "UPDATE Asociados SET nombre=?, apellido=?, dni=?, calle=?, numero=?, " +
                "ciudad=?, telefono=? WHERE id=?", dto);
            fail("Deberia lanzar SQLException con conexion cerrada");
        } catch (SQLException e) {
            // correcto
        }
    }

    @Test
    public void testEjecutarDeleteLanzaSQLException() {
        try {
            bd.ejecutarDelete("DELETE FROM Asociados WHERE DNI = ?", "11111111");
            fail("Deberia lanzar SQLException con conexion cerrada");
        } catch (SQLException e) {
            // correcto
        }
    }
}