package mocks_integracion_concurrencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import modelo.modeloDominio.ambulancia.Ambulancia;
import modelo.modeloDominio.ambulancia.RetornoAutomatico;
import modelo.modeloDominio.ambulancia.State;
import modelo.modeloDominio.personas.asociado.Asociado;

import modelo.modeloDominio.util.Domicilio;
import persistencia.AsociadoDTO;

@ExtendWith(MockitoExtension.class)
class AmbulanciaConcurrenciaTest {

	@Mock
	State estadoMock;

	@Mock
	Asociado asociadoMock;

	Ambulancia ambulancia;

	@BeforeEach
	void setUp() {
		ambulancia = new Ambulancia();
	}

	@Test
	void testHiloBloqueadoEsperaHastaLiberarse() throws InterruptedException {
		ambulancia.setState(estadoMock);
		ambulancia.setSimulacionActiva(true);
		ambulancia.setOcupado(true);

		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				ambulancia.trasladarALaClinica(asociadoMock);
			}
		});
		hilo.start();
		Thread.sleep(150);

		assertEquals(Thread.State.WAITING, hilo.getState(), "El hilo debe estar en WAITING mientras la ambulancia está ocupada y la simulación está activa");
		verifyNoInteractions(estadoMock);

		ambulancia.regresarSinPaciente();
		hilo.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo.getState(), "El hilo debe terminar tras liberar la ambulancia con regresarSinPaciente");
		verify(estadoMock, times(1)).SolicitudDeTraslado();
	}

	@Test
	void testHiloEnEsperaTerminaAlFinalizarSimulacion() throws InterruptedException {
		ambulancia.setState(estadoMock);
		ambulancia.setSimulacionActiva(true);
		ambulancia.setOcupado(true);

		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				ambulancia.trasladarALaClinica(asociadoMock);
			}
		});
		hilo.start();
		Thread.sleep(150);

		assertEquals(Thread.State.WAITING, hilo.getState(), "El hilo debe estar en WAITING mientras la ambulancia está ocupada y la simulación está activa");

		ambulancia.setSimulacionActiva(false);
		hilo.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo.getState(), "El hilo debe terminar al finalizar la simulacion");
		verifyNoInteractions(estadoMock);
	}

	@Test
	void testOperacionNoEsperaSiSimulacionFinalizada() throws InterruptedException {
		ambulancia.setState(estadoMock);
		ambulancia.setOcupado(true);
		ambulancia.setSimulacionActiva(false);

		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				ambulancia.trasladarALaClinica(asociadoMock);
			}
		});
		hilo.start();
		hilo.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo.getState(), "El hilo no debe bloquearse cuando la simulacion esta desactivada");
		verifyNoInteractions(estadoMock);
	}

	@Test
	void testHiloEjecutaInmediatamenteCuandoLibre() throws InterruptedException {
		ambulancia.setState(estadoMock);
		ambulancia.setOcupado(false);
		ambulancia.setSimulacionActiva(true);

		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				ambulancia.trasladarALaClinica(asociadoMock);
			}
		});
		hilo.start();
		hilo.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo.getState(), "El hilo debe terminar inmediatamente cuando la ambulancia esta libre");
		verify(estadoMock, times(1)).SolicitudDeTraslado();
	}

	@Test
	void testExclusionMutua_dosHilos() throws InterruptedException {
		ambulancia.setSimulacionActiva(true);
		ambulancia.setOcupado(true);

		Asociado a1 = new Asociado(new AsociadoDTO("Valen", "Acevedo", "111", "MDP", "Calle 1", 10, "2231"));
		Asociado a2 = new Asociado(new AsociadoDTO("Mateo", "Vazquez", "222", "MDP", "Calle 2", 20, "2232"));

		Thread hilo1 = new Thread(new Runnable() {
			@Override
			public void run() {
				ambulancia.trasladarALaClinica(a1);
			}
		});
		Thread hilo2 = new Thread(new Runnable() {
			@Override
			public void run() {
				ambulancia.trasladarALaClinica(a2);
			}
		});

		hilo1.start();
		hilo2.start();
		Thread.sleep(150);

		assertEquals(Thread.State.WAITING, hilo1.getState(), "Hilo 1 debe estar en WAITING mientras la ambulancia esta ocupada");
		assertEquals(Thread.State.WAITING, hilo2.getState(), "Hilo 2 debe estar en WAITING mientras la ambulancia esta ocupada");

		ambulancia.regresarSinPaciente();
		Thread.sleep(150);

		boolean unoTermino = (hilo1.getState() == Thread.State.TERMINATED && hilo2.getState() == Thread.State.WAITING)
				|| (hilo2.getState() == Thread.State.TERMINATED && hilo1.getState() == Thread.State.WAITING);
		assertTrue(unoTermino, "Exactamente uno de los dos hilos debe haber terminado tras liberar la ambulancia una vez");
		assertTrue(ambulancia.isOcupado(), "La ambulancia debe seguir ocupada porque el hilo que paso la uso");

		ambulancia.regresarSinPaciente();
		hilo1.join(1000);
		hilo2.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo1.getState(), "Hilo 1 debe haber terminado tras la segunda liberacion de la ambulancia");
		assertEquals(Thread.State.TERMINATED, hilo2.getState(), "Hilo 2 debe haber terminado tras la segunda liberacion de la ambulancia");
	}

	@Test
	void testRetornoAutomatico_simulacionActiva_sigueVivo() throws InterruptedException {
		ambulancia.setSimulacionActiva(true);

		RetornoAutomatico retorno = new RetornoAutomatico(ambulancia);
		Thread hilo = new Thread(retorno);
		hilo.start();

		Thread.sleep(1000);

		assertTrue(hilo.isAlive(), "El hilo de RetornoAutomatico debe seguir vivo mientras la simulacion esta activa");
		hilo.interrupt();
		hilo.join(500);
		
	}

	@Test
	void testRetornoAutomatico_simulacionFinalizada_termina() throws InterruptedException {
		ambulancia.setSimulacionActiva(false);

		RetornoAutomatico retorno = new RetornoAutomatico(ambulancia);
		Thread hilo = new Thread(retorno);
		hilo.start();

		Thread.sleep(15000);


		assertFalse(hilo.isAlive(), "El hilo de RetornoAutomatico debe haber terminado cuando la simulacion esta desactivada");
		
		hilo.interrupt();
		hilo.join(500);
		
	}

}
