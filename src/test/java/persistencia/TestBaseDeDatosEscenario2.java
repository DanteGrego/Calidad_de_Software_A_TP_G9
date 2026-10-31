package persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ESCENARIO 2 (conexion abierta, tabla vacia) sobre la clase BaseDeDatos.
 *
 * El estado "tabla vacia" lo arma esta clase en setUp() con TRUNCATE; NO es
 * codigo bajo prueba. El TRUNCATE en si no se testea (eso seria testear MySQL),
 * solo se verifica como sanity check que el escenario quedo bien armado.
 *
 * La cobertura de metodos de BaseDeDatos se repite respecto del escenario 1,
 * pero sobre una condicion de entrada distinta (tabla vacia en lugar de tabla
 * con 20 filas). No es redundancia: es cobertura por condiciones.
 *
 * No se testean precondiciones (los 'assert' internos de BaseDeDatos):
 * violar una precondicion es comportamiento indefinido, no un bug.
 *
 * Para JUnit 4: cambiar imports (Assertions -> Assert, BeforeEach -> @Before,
 * Test -> org.junit.Test).
 */
public class TestBaseDeDatosEscenario2 extends BaseIntegracion {

    @BeforeEach
    public void setUp() throws SQLException {
        vaciarTabla();
    }

    // ================= Sanity check del arnes (no es codigo bajo prueba) =================

    @Test
    public void testEscenarioQuedaArmadoConTablaVacia() throws SQLException {
        // Verifica que el setUp dejo el escenario como se espera.
        // Si esto falla, los demas tests corren sobre datos erroneos.
        assertEquals(0, contarFilas(), "El escenario 2 requiere tabla vacia");
    }

    // ================= Conexion y Singleton sobre tabla vacia =================

    @Test
    public void testConexionSigueValidaConTablaVacia() throws SQLException {
        // Misma propiedad que en escenario 1, pero tras un TRUNCATE
        assertTrue(bd.getConexion().isValid(2),
            "La conexion debe seguir valida aunque la tabla este vacia");
    }

    @Test
    public void testSingletonSigueSiendoMismaInstanciaConTablaVacia() {
        // Misma propiedad que en escenario 1, pero tras un TRUNCATE
        assertSame(bd, BaseDeDatos.getInstancia(),
            "Vaciar la tabla no debe recrear el Singleton");
    }

    // ================= Consultas sobre tabla vacia =================

    @Test
    public void testEjecutarConsultaNoDevuelveFilas() throws SQLException {
        ResultSet rs = bd.ejecutarConsulta("SELECT * FROM Asociados;", -1);
        assertFalse(rs.next(), "Una tabla vacia no debe devolver filas");
        rs.close();
    }

    @Test
    public void testEjecutarConsultaConIdSobreTablaVaciaDevuelveVacio() throws SQLException {
        // Ejercita la rama id != -1 de ejecutarConsulta sobre tabla vacia
        ResultSet rs = bd.ejecutarConsulta("SELECT * FROM Asociados WHERE id = ?", 1);
        assertFalse(rs.next(), "No debe haber fila con id=1 en tabla vacia");
        rs.close();
    }

    // ================= Insert sobre tabla vacia =================

    @Test
    public void testEjecutarInsertEnTablaVaciaCreaUnaFila() throws SQLException {
        bd.ejecutarInsert(INSERT, crearDTO("11111111"));
        assertEquals(1, contarFilas());
        assertTrue(existeDni("11111111"));
    }

    // ================= iniciarBD() repuebla desde estado vacio =================

    @Test
    public void testIniciarBDRepueblaLaTabla() throws SQLException {
        bd.iniciarBD();
        assertEquals(20, contarFilas(), "iniciarBD() debe repoblar con los 20 semilla");
    }

    @Test
    public void testIniciarBDSobreTablaVaciaRestauraDatosSemilla() throws SQLException {
        bd.iniciarBD();

        // Verifica que los datos semilla volvieron, no solo la cantidad.
        // El constructor de AsociadoDTO recibe (nombre, apellido, dni,
        // ciudad, calle, numero, telefono); aca leemos por columna.
        assertEquals("Juan", valorColumna("12345678", "nombre"));
        assertEquals("State", valorColumna("12345678", "apellido"));
        assertEquals("Adrian", valorColumna("28293031", "nombre"));
        assertEquals("Composite", valorColumna("28293031", "apellido"));
    }

    // ================= Caso limite de borrado =================

    @Test
    public void testEjecutarDeleteSobreTablaVaciaNoLanzaExcepcion() throws SQLException {
        // Borrar un DNI que no existe no debe romper ni insertar nada
        bd.ejecutarDelete("DELETE FROM Asociados WHERE dni = ?", "99999999");
        assertEquals(0, contarFilas(), "La tabla debe seguir vacia");
    }
}