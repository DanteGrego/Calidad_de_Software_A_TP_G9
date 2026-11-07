package mocks;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import modelo.modeloDominio.ambulancia.Ambulancia;
import modelo.modeloDominio.ambulancia.DisponibleState;
import modelo.modeloDominio.ambulancia.RetornoAutomatico;
import modelo.modeloDominio.ambulancia.State;
import modelo.modeloDominio.personas.asociado.Asociado;
import modelo.modeloDominio.personas.operario.Operario;

@ExtendWith(MockitoExtension.class)
class AmbulanciaDelegacionSetterGetterStateTest {
	
	@Mock
	Asociado asociadoMock;
	
	@Mock
	Operario operarioMock;
	
	@Mock
	RetornoAutomatico retornoAutomaticoMock;
	
	@Mock
	State estadoMock;
	
	Ambulancia ambulancia;
	
	
	@BeforeEach
	void setUp() {
		ambulancia = new Ambulancia();
	}
	
	// Getter solo: estado inicial sin haber llamado a setState
    @Test
    void testGetEstadoActual_alCrearse_esDisponible() {
        assertEquals(DisponibleState.class, ambulancia.getEstadoActual().getClass());
    }
 

    // Setter + getter juntos
    @Test
    void testSetState_getEstadoActual_devuelveLaMismaInstancia() {
        ambulancia.setState(estadoMock);

        assertSame(estadoMock, ambulancia.getEstadoActual());
    }

    //Pruebas de correcta delegación
    
	@Test
	void testSolicitarMantenimiento_llamaSolicitudMantenimiento() {
		
		ambulancia.setState(estadoMock);
		ambulancia.solicitarMantenimiento(operarioMock);
		
		//verifico que haya llamado solo una vez a SolicitudMantenimiento()
		verify(estadoMock, times(1)).SolicitudMantenimiento();
		
		//verifico que no haya llamdo a ningún otro método de State
		verifyNoMoreInteractions(estadoMock);
	}

	@Test
	void testAtenderDomicilio_llamaSolicitudDeAtencionDomicilio() {
		
		ambulancia.setState(estadoMock);
		ambulancia.atenderDomicilio(asociadoMock);
		
		verify(estadoMock, times(1)).SolicitudDeAtencionDomicilio();
		
		verifyNoMoreInteractions(estadoMock);
	}
	
	@Test
	void testRetornoAutomatico_llamaRetornoClinica() {

	    ambulancia.setState(estadoMock);
	    ambulancia.retornoAutomatico(retornoAutomaticoMock);

	    verify(estadoMock, times(1)).RetornoClinica();

	    verifyNoMoreInteractions(estadoMock);
	}

	@Test
	void testTrasladarALaClinica_llamaSolicitudDeTraslado() {
	    
	    ambulancia.setState(estadoMock);
	    ambulancia.trasladarALaClinica(asociadoMock);

	    verify(estadoMock, times(1)).SolicitudDeTraslado();

	    verifyNoMoreInteractions(estadoMock);
	}

}
