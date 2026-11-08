package mocks;

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

		assertEquals(Thread.State.WAITING, hilo.getState());
		verifyNoInteractions(estadoMock);

		ambulancia.regresarSinPaciente();
		hilo.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo.getState());
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

		assertEquals(Thread.State.WAITING, hilo.getState());

		ambulancia.setSimulacionActiva(false);
		hilo.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo.getState());
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

		assertEquals(Thread.State.TERMINATED, hilo.getState());
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

		assertEquals(Thread.State.TERMINATED, hilo.getState());
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

		assertEquals(Thread.State.WAITING, hilo1.getState());
		assertEquals(Thread.State.WAITING, hilo2.getState());

		ambulancia.regresarSinPaciente();
		Thread.sleep(150);

		boolean unoTermino = (hilo1.getState() == Thread.State.TERMINATED && hilo2.getState() == Thread.State.WAITING)
				|| (hilo2.getState() == Thread.State.TERMINATED && hilo1.getState() == Thread.State.WAITING);
		assertTrue(unoTermino);
		assertTrue(ambulancia.isOcupado());

		ambulancia.regresarSinPaciente();
		hilo1.join(1000);
		hilo2.join(1000);

		assertEquals(Thread.State.TERMINATED, hilo1.getState());
		assertEquals(Thread.State.TERMINATED, hilo2.getState());
	}

	@Test
	void testHilosFinalizan_simulacionTerminaYAmbulanciaLibre() throws InterruptedException {
		ambulancia.setSimulacionActiva(true);
		ambulancia.setOcupado(false);

		Domicilio dom = new Domicilio("Calle", 100, "MDP");
		Asociado asociado = new Asociado("Mateo", "Vazquez", "333", "2233", dom, 0, ambulancia);
		Thread hiloAsoc = new Thread(asociado);
		hiloAsoc.start();
		hiloAsoc.join(1000);

		ambulancia.verificarYFinalizarSimulacion();
		ambulancia.regresarSinPaciente();

		assertFalse(ambulancia.isSimulacionActiva());
		assertFalse(ambulancia.isOcupado());
	}

	@Test
	void testRetornoAutomatico_simulacionActiva_sigueVivo() throws InterruptedException {
		ambulancia.setSimulacionActiva(true);

		RetornoAutomatico retorno = new RetornoAutomatico(ambulancia);
		Thread hilo = new Thread(retorno);
		hilo.start();

		Thread.sleep(100);

		assertTrue(hilo.isAlive());
		hilo.interrupt();
		hilo.join(500);
		
	}

	@Test
	void testRetornoAutomatico_simulacionFinalizada_termina() throws InterruptedException {
		ambulancia.setSimulacionActiva(false);

		RetornoAutomatico retorno = new RetornoAutomatico(ambulancia);
		Thread hilo = new Thread(retorno);
		hilo.start();

		Thread.sleep(100);

		
		assertFalse(hilo.isAlive());
		
		hilo.interrupt();
		hilo.join(500);
		
	}

}
