package mocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import modelo.modeloDominio.ambulancia.Ambulancia;
import modelo.modeloDominio.ambulancia.DisponibleState;
import modelo.modeloDominio.ambulancia.State;

@ExtendWith(MockitoExtension.class)
class AmbulanciaStateGetterSetterTest {

    @Mock
    State estadoMock;

    Ambulancia ambulancia;

    @BeforeEach
    void setUp() {
        ambulancia = new Ambulancia();
    }

    @Test
    void testGetEstadoActual_alCrearse_esDisponible() {
        assertEquals(DisponibleState.class, ambulancia.getEstadoActual().getClass());
    }

    @Test
    void testSetState_getEstadoActual_devuelveLaMismaInstancia() {
        ambulancia.setState(estadoMock);

        assertSame(estadoMock, ambulancia.getEstadoActual());
    }
}
