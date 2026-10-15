package persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
	
}
