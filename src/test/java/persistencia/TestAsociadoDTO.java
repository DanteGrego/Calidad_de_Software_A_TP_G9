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
	public void testSetNombreNull() {
		try {
		dto.setNombre(null);
		fail();
		}
		catch(Exception e) { //no se que tan bien esté esto porque el metodo setNombre no lanza excepcion 
		}
	}
}
