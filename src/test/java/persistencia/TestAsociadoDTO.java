package persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAsociadoDTO {
	AsociadoDTO dto;
	//cambian las tags de Before y After a BeforeEach y AfterEach por ser Junit 5
	@BeforeEach
	public void setUp() {
		dto = new AsociadoDTO();
	}
	@AfterEach
	public void cleanUp() {
	}
	
	@Test
	public void testSetNombreValido() {
		dto.setNombre("JoseJuan");
		assertEquals("JoseJuan", dto.getNombre());
	}
	@Test
	public void testSetApellidoValido() {
		dto.setApellido("Uchija");
		assertEquals("Uchija", dto.getApellido());
	}
	@Test 
	public void testSetDniValido() {
		dto.setDni("47149999");
		assertEquals("47149999", dto.getDni());
	}	
	@Test
	public void testSetCiudadValida() {
		dto.setCiudad("Mar del plata");
		assertEquals("Mar del plata", dto.getCiudad());
	}
	@Test
	public void testSetCalleValida() {
		dto.setCalle("Corrientes");
		assertEquals("Corrientes", dto.getCalle());
	}
	@Test
	public void testSetNumeroValido() {
		dto.setNumero(1450);
		assertEquals(1450, dto.getNumero());
	}
	@Test
	public void testSetTelefonoValido() {
		dto.setTelefono("2231237654");
		assertEquals("2231237654",dto.getTelefono());
	}
	/** Valor limite: el SRS permite numero >= 0. Falla contra el codigo original (numero > 0). */
	@Test
	public void testSetNumeroCeroValido() {
		dto.setNumero(0);
		assertEquals(0, dto.getNumero());
	}
	@Test
	public void testSetNumeroUnoValido() {
		dto.setNumero(1);
		assertEquals(1, dto.getNumero());
	}
	
	// ================= Valores invalidos: no pisan el valor previo =================
	 
	@Test
	public void testSetNombreNuloNoPisaValorPrevio() {
		dto.setNombre("Original");
		try {
			dto.setNombre(null);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals("Original", dto.getNombre());
	}
 
	@Test
	public void testSetApellidoNuloNoPisaValorPrevio() {
		dto.setApellido("Original");
		try {
			dto.setApellido(null);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals("Original", dto.getApellido());
	}
 
	@Test
	public void testSetDniNuloNoPisaValorPrevio() {
		dto.setDni("12345678");
		try {
			dto.setDni(null);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals("12345678", dto.getDni());
	}
 
	@Test
	public void testSetCiudadNulaNoPisaValorPrevio() {
		dto.setCiudad("Original");
		try {
			dto.setCiudad(null);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals("Original", dto.getCiudad());
	}
 
	@Test
	public void testSetCalleNulaNoPisaValorPrevio() {
		dto.setCalle("Original");
		try {
			dto.setCalle(null);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals("Original", dto.getCalle());
	}
 
	@Test
	public void testSetTelefonoNuloNoPisaValorPrevio() {
		dto.setTelefono("2231237654");
		try {
			dto.setTelefono(null);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals("2231237654", dto.getTelefono());
	}
 
	/** Valor limite invalido: -1 (inmediatamente debajo del 0). */
	@Test
	public void testSetNumeroNegativoNoPisaValorPrevio() {
		dto.setNumero(100);
		try {
			dto.setNumero(-1);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals(100, dto.getNumero());
	}
 
	@Test
	public void testSetNumeroMuyNegativoNoPisaValorPrevio() {
		dto.setNumero(100);
		try {
			dto.setNumero(Integer.MIN_VALUE);
		} catch (AssertionError e) {
			// esperado con -ea
		}
		assertEquals(100, dto.getNumero());
	}
}
