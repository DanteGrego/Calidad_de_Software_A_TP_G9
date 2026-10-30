package persistencia;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
// si no te tira ningun error al ejecutar esta clase entonces tenes bien configurado el SQL
public class BaseDeDatosMinimoTest {

    private static BaseDeDatos bd;

    @BeforeAll
    static void setUpAll() {
        // Al pedir la instancia se dispara el constructor:
        // cargarDriver() + abrirConexion() + iniciarBD()
        bd = BaseDeDatos.getInstancia();
    }

    @AfterAll
    static void tearDownAll() throws SQLException {
        bd.cerrarConexion();
    }

    @Test
    void getInstancia_devuelveSiempreLaMismaInstancia() {
        BaseDeDatos otra = BaseDeDatos.getInstancia();
        assertSame(bd, otra, "El Singleton debe devolver la misma instancia");
    }

    @Test
    void getConexion_noEsNulaYEstaAbierta() throws SQLException {
        Connection c = bd.getConexion();
        assertNotNull(c, "La conexión no debe ser null");
        assertFalse(c.isClosed(), "La conexión debe estar abierta");
    }

    @Test
    void iniciarBD_creaLaBaseYLaTablaAsociados() throws SQLException {
        // Verificamos que la base Grupo_7 existe consultando information_schema
        ResultSet rs = bd.ejecutarConsulta(
                "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA " +
                "WHERE SCHEMA_NAME = 'Grupo_7'", -1);
        assertTrue(rs.next(), "La base Grupo_7 debe existir");

        // Verificamos que la tabla Asociados existe
        rs = bd.ejecutarConsulta(
                "SELECT TABLE_NAME FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = 'Grupo_7' AND TABLE_NAME = 'Asociados'", -1);
        assertTrue(rs.next(), "La tabla Asociados debe existir");
    }
}