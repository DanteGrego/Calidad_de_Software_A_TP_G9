package mocks;


import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import modelo.modeloDominio.ambulancia.Ambulancia;
import modelo.modeloDominio.ambulancia.RetornoAutomatico;
import modelo.modeloDominio.ambulancia.State;
import modelo.modeloDominio.personas.asociado.Asociado;
import modelo.modeloDominio.personas.operario.Operario;

@ExtendWith(MockitoExtension.class)
class AmbulanciaDelegacionTest {
	
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
		ambulancia.setOcupado(false);
		ambulancia.setSimulacionActiva(true);
		ambulancia.setState(estadoMock);
	}
    
	@Test
	void testSolicitarMantenimiento_llamaSolicitudMantenimiento() {
		
		ambulancia.solicitarMantenimiento(operarioMock);
		
		//verifico que haya llamado solo una vez a SolicitudMantenimiento()
		verify(estadoMock, times(1)).SolicitudMantenimiento();
		
		//verifico que no haya llamdo a ningún otro método de State
		verifyNoMoreInteractions(estadoMock);
	}

	@Test
	void testAtenderDomicilio_llamaSolicitudDeAtencionDomicilio() {
		
		ambulancia.atenderDomicilio(asociadoMock);
		
		verify(estadoMock, times(1)).SolicitudDeAtencionDomicilio();
		
		verifyNoMoreInteractions(estadoMock);
	}
	
	@Test
	void testRetornoAutomatico_llamaRetornoClinica() {

	    ambulancia.retornoAutomatico(retornoAutomaticoMock);

	    verify(estadoMock, times(1)).RetornoClinica();

	    verifyNoMoreInteractions(estadoMock);
	}

	@Test
	void testTrasladarALaClinica_llamaSolicitudDeTraslado() {
	    
	    ambulancia.trasladarALaClinica(asociadoMock);

	    verify(estadoMock, times(1)).SolicitudDeTraslado();

	    verifyNoMoreInteractions(estadoMock);
	}

	@Test
	void testVolviendoDelTaller_llamaSolicitudMantenimiento() {
		
		ambulancia.volviendoDelTaller();
		
		verify(estadoMock, times(1)).SolicitudMantenimiento();
		
		verifyNoMoreInteractions(estadoMock);
	}

	@Test
	void testRegresarSinPaciente_llamaRetornoClinica() {
		
		ambulancia.regresarSinPaciente();
		
		verify(estadoMock, times(1)).RetornoClinica();
		
		verifyNoMoreInteractions(estadoMock);
	}

}
