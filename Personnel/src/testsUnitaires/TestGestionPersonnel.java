package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import personnel.*;

class TestGestionPersonnel {
	
	GestionPersonnel gestionPersonnel;
	
	public TestGestionPersonnel() throws SauvegardeImpossible, ExceptionDate {
		gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	}
	
	@Test 
	void addLigue() throws SauvegardeImpossible {
		
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}
	
	@Test 
	void addEmploye() throws SauvegardeImpossible, ExceptionDate {
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe;
		employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
		assertEquals(employe, ligue.getEmployes().first());
	}
	
}