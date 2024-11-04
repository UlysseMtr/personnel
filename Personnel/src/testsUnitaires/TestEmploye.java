package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import personnel.*;

public class TestEmploye {
	
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void AddEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
		assertEquals("Bouchard", employe.getNom());
		assertEquals("Gérard", employe.getPrenom());
		assertEquals("g.bouchard@gmail.com", employe.getMail());
		assertEquals(true, employe.checkPassword("azerty"));
	}
	
	@Test
	void NomEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
		assertEquals("Bouchard", employe.getNom());
	}
	
	@Test
	void PrenomEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
		assertEquals("Gérard", employe.getPrenom());
	}
	
	@Test
	void MailEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
		assertEquals("g.bouchard@gmail.com", employe.getMail());
	}
	
	@Test
	void PasswordEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
		assertEquals("azerty", employe.getPassword());
	}
	
	@Test
	void deleteEmploye() throws SauvegardeImpossible
	{
		Employe employe;
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
		employe.remove();
		assertFalse(ligue.getEmployes().contains(employe));
	}
}