package mocks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import modelo.modeloDominio.ambulancia.Ambulancia;
import modelo.modeloDominio.ambulancia.AtendiendoEnDomicilioState;
import modelo.modeloDominio.ambulancia.DisponibleState;
import modelo.modeloDominio.ambulancia.EnTallerState;
import modelo.modeloDominio.ambulancia.RegresandoClinicaSinPacienteState;
import modelo.modeloDominio.ambulancia.RegresandoDelTallerState;
import modelo.modeloDominio.ambulancia.RetornoAutomatico;
import modelo.modeloDominio.ambulancia.State;
import modelo.modeloDominio.ambulancia.TrasladandoPacienteClinicaState;
import modelo.modeloDominio.personas.asociado.Asociado;
import modelo.modeloDominio.personas.operario.Operario;

@ExtendWith(MockitoExtension.class)
class AmbulanciaTransicionesTest {
	
	@Mock
	Asociado asociadoMock;
	
	@Mock
	Operario operarioMock;
	
	@Mock
	RetornoAutomatico retornoAutomaticoMock;
	
	Ambulancia ambulancia;
	
	State estadoAsignado;
	
	@BeforeEach
	void setUp() {
		ambulancia = new Ambulancia();
		ambulancia.setOcupado(false);
		ambulancia.setSimulacionActiva(true);
	}
	
	@Nested
	class DisponibleStateTest {
		
		@BeforeEach
		void setUp() {
			estadoAsignado = new DisponibleState(ambulancia);
			ambulancia.setState(estadoAsignado);
		}
		
		@Test
		void testRetornoClinica_noCambiaEstado() {
			ambulancia.retornoAutomatico(retornoAutomaticoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "RetornoAutomatico en estado disponible no debe cambiar estado");
		}
		
		@Test
		void testSolicitudDeAtencionDomicilio_aAtendiendoEnDomicilio() {
			ambulancia.atenderDomicilio(asociadoMock);
			assertEquals(AtendiendoEnDomicilioState.class, ambulancia.getEstadoActual().getClass(), "SolicitudDeAtencionDomicilio en estado disponible debe cambiar a AtendiendoEnDomicilio");
		}
		
		@Test
		void testSolicitudMantenimiento_aEnTaller() {
			ambulancia.solicitarMantenimiento(operarioMock);
			assertEquals(EnTallerState.class, ambulancia.getEstadoActual().getClass(), "SolicitudMantenimiento en estado disponible debe cambiar a estado EnTaller");
		}
		
		@Test
		void testSolicitudDeTraslado_aTrasladandoPacienteClinica() {
			ambulancia.trasladarALaClinica(asociadoMock);
			assertEquals(TrasladandoPacienteClinicaState.class, ambulancia.getEstadoActual().getClass(), "SolicitudDeTraslado en estado disponible debe cambiar a estado TrasladandoPacienteClinica");
		}
	}
	
	@Nested
	class TrasladandoPacienteClinicaStateTest {

		@BeforeEach
		void setUp() {
			estadoAsignado = new TrasladandoPacienteClinicaState(ambulancia);
			ambulancia.setState(estadoAsignado);
		}

		@Test
		void testRetornoClinica_aDisponible() {
			ambulancia.retornoAutomatico(retornoAutomaticoMock);
			assertEquals(DisponibleState.class, ambulancia.getEstadoActual().getClass(), "RetornoClinica en estado TrasladandoPacienteClinica debe cambiar a Disponible pero cambió a " + ambulancia.getEstadoActual().getClass().getName());
		}

		@Test
		void testSolicitudDeAtencionDomicilio_noCambiaEstado() {
			ambulancia.atenderDomicilio(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeAtencionDomicilio en estado TrasladandoPacienteClinica no debe cambiar estado");
		}

		@Test
		void testSolicitudMantenimiento_noCambiaEstado() {
			ambulancia.solicitarMantenimiento(operarioMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudMantenimiento en estado TrasladandoPacienteClinica no debe cambiar estado");
		}

		@Test
		void testSolicitudDeTraslado_noCambiaEstado() {
			ambulancia.trasladarALaClinica(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeTraslado en estado TrasladandoPacienteClinica no debe cambiar estado");
		}
	}

	@Nested
	class AtendiendoEnDomicilioStateTest {

		@BeforeEach
		void setUp() {
			estadoAsignado = new AtendiendoEnDomicilioState(ambulancia);
			ambulancia.setState(estadoAsignado);
		}

		@Test
		void testRetornoClinica_aRegresandoClinicaSinPaciente() {
			ambulancia.retornoAutomatico(retornoAutomaticoMock);
			assertEquals(RegresandoClinicaSinPacienteState.class, ambulancia.getEstadoActual().getClass(), "RetornoClinica en estado AtendiendoEnDomicilio debe cambiar a RegresandoClinicaSinPaciente");
		}

		@Test
		void testSolicitudDeAtencionDomicilio_noCambiaEstado() {
			ambulancia.atenderDomicilio(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeAtencionDomicilio en estado AtendiendoEnDomicilio no debe cambiar estado");
		}

		@Test
		void testSolicitudMantenimiento_noCambiaEstado() {
			ambulancia.solicitarMantenimiento(operarioMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudMantenimiento en estado AtendiendoEnDomicilio no debe cambiar estado");
		}

		@Test
		void testSolicitudDeTraslado_noCambiaEstado() {
			ambulancia.trasladarALaClinica(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeTraslado en estado AtendiendoEnDomicilio no debe cambiar estado");
		}
	}

	@Nested
	class RegresandoClinicaSinPacienteStateTest {

		@BeforeEach
		void setUp() {
			estadoAsignado = new RegresandoClinicaSinPacienteState(ambulancia);
			ambulancia.setState(estadoAsignado);
		}

		@Test
		void testRetornoClinica_aDisponible() {
			ambulancia.retornoAutomatico(retornoAutomaticoMock);
			assertEquals(DisponibleState.class, ambulancia.getEstadoActual().getClass(), "RetornoClinica en estado RegresandoClinicaSinPaciente debe cambiar a Disponible");
		}

		@Test
		void testSolicitudDeAtencionDomicilio_aAtendiendoEnDomicilio() {
			ambulancia.atenderDomicilio(asociadoMock);
			assertEquals(AtendiendoEnDomicilioState.class, ambulancia.getEstadoActual().getClass(), "SolicitudDeAtencionDomicilio en estado RegresandoClinicaSinPaciente debe cambiar a AtendiendoEnDomicilio");
		}

		@Test
		void testSolicitudMantenimiento_noCambiaEstado() {
			ambulancia.solicitarMantenimiento(operarioMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudMantenimiento en estado RegresandoClinicaSinPaciente no debe cambiar estado");
		}

		@Test
		void testSolicitudDeTraslado_aTrasladandoPacienteClinica() {
			ambulancia.trasladarALaClinica(asociadoMock);
			assertEquals(TrasladandoPacienteClinicaState.class, ambulancia.getEstadoActual().getClass(), "SolicitudDeTraslado en estado RegresandoClinicaSinPaciente debe cambiar a TrasladandoPacienteClinica");
		}
	}

	@Nested
	class EnTallerStateTest {

		@BeforeEach
		void setUp() {
			estadoAsignado = new EnTallerState(ambulancia);
			ambulancia.setState(estadoAsignado);
		}

		@Test
		void testRetornoClinica_noCambiaEstado() {
			ambulancia.retornoAutomatico(retornoAutomaticoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "RetornoClinica en estado EnTaller no debe cambiar estado");
		}

		@Test
		void testSolicitudDeAtencionDomicilio_noCambiaEstado() {
			ambulancia.atenderDomicilio(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeAtencionDomicilio en estado EnTaller no debe cambiar estado");
		}

		@Test
		void testSolicitudMantenimiento_aRegresandoDelTaller() {
			ambulancia.solicitarMantenimiento(operarioMock);
			assertEquals(RegresandoDelTallerState.class, ambulancia.getEstadoActual().getClass(), "SolicitudMantenimiento en estado EnTaller debe cambiar a RegresandoDelTaller");
		}

		@Test
		void testSolicitudDeTraslado_noCambiaEstado() {
			ambulancia.trasladarALaClinica(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeTraslado en estado EnTaller no debe cambiar estado");
		}
	}

	@Nested
	class RegresandoDelTallerStateTest {

		@BeforeEach
		void setUp() {
			estadoAsignado = new RegresandoDelTallerState(ambulancia);
			ambulancia.setState(estadoAsignado);
		}

		@Test
		void testRetornoClinica_aDisponible() {
			ambulancia.retornoAutomatico(retornoAutomaticoMock);
			assertEquals(DisponibleState.class, ambulancia.getEstadoActual().getClass(), "RetornoClinica en estado RegresandoDelTaller debe cambiar a Disponible");
		}

		@Test
		void testSolicitudDeAtencionDomicilio_noCambiaEstado() {
			ambulancia.atenderDomicilio(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeAtencionDomicilio en estado RegresandoDelTaller no debe cambiar estado");
		}

		@Test
		void testSolicitudMantenimiento_noCambiaEstado() {
			ambulancia.solicitarMantenimiento(operarioMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudMantenimiento en estado RegresandoDelTaller no debe cambiar estado");
		}

		@Test
		void testSolicitudDeTraslado_noCambiaEstado() {
			ambulancia.trasladarALaClinica(asociadoMock);
			assertSame(estadoAsignado, ambulancia.getEstadoActual(), "SolicitudDeTraslado en estado RegresandoDelTaller no debe cambiar estado");
		}
	}

}
