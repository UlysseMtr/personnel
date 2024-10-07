package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import personnel.*;

public class TestGestionPersonnel {
	
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void getLigue() throws SauvegardeImpossible {
		
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("root", "", "g.bouchard@gmail.com", "azerty"); 
		assertEquals("root", ligue.getAdministrateur());	
	}
	
	@Test 
	void addLigue() throws SauvegardeImpossible {
		
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}
}
