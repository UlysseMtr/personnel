package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import personnel.*;

public class TestEmploye {
	
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void AddEmploye() throws SauvegardeImpossible, ExceptionDate
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
		assertEquals("Bouchard", employe.getNom());
		assertEquals("Gérard", employe.getPrenom());
		assertEquals("g.bouchard@gmail.com", employe.getMail());
		assertEquals(true, employe.checkPassword("azerty"));
	}
	
	@Test
	void NomEmploye() throws SauvegardeImpossible, ExceptionDate
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
		assertEquals("Bouchard", employe.getNom());
	}
	
	@Test
	void PrenomEmploye() throws SauvegardeImpossible, ExceptionDate
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
		assertEquals("Gérard", employe.getPrenom());
	}
	
	@Test
	void MailEmploye() throws SauvegardeImpossible, ExceptionDate
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
		assertEquals("g.bouchard@gmail.com", employe.getMail());
	}
	
	@Test
	void PasswordEmploye() throws SauvegardeImpossible, ExceptionDate
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
		assertEquals("azerty", employe.getPassword());
	}
	
	@Test
	void deleteEmploye() throws SauvegardeImpossible, ExceptionDate
	{
		Employe employe;
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
		employe.remove();
		assertFalse(ligue.getEmployes().contains(employe));
	}
	
	@Test
    void testValidDates() throws SauvegardeImpossible , ExceptionDate
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertDoesNotThrow(() -> ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty" , "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30)));  
   }
	
	@Test
    void testInvalidDates() throws SauvegardeImpossible, ExceptionDate
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Exception exception = assertThrows(ExceptionDate.class, () -> {
		ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test" , LocalDate.of(2023, 12, 31), LocalDate.of(2023, 1, 1));
		});
		assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());  
    }
	 
	@Test
	void testDateArriveNull() throws SauvegardeImpossible, ExceptionDate
	{
		 Ligue ligue = gestionPersonnel.addLigue("Football");
		 Exception exception1 = assertThrows(ExceptionDate.class, () -> {
	     ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test" , LocalDate.of(2023, 12, 31), LocalDate.of(2023, 1, 1));
	     });
	     assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception1.getMessage());     
	}
	@Test
	void testDateDepartNull() throws SauvegardeImpossible , ExceptionDate
	{
		 Ligue ligue = gestionPersonnel.addLigue("Football");
		 Exception exception1 = assertThrows(ExceptionDate.class, () -> {
	     ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test" , LocalDate.of(2023, 12, 31), LocalDate.of(2023, 1, 1));
	     });
	     assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception1.getMessage()); 	
	}
	
	@Test
	void setDateArriveNull()throws SauvegardeImpossible, ExceptionDate 
	{
		  Ligue ligue = gestionPersonnel.addLigue("Football");
		  Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test" , LocalDate.of(2023, 12, 31), LocalDate.of(2024, 1, 1));
		  Exception exception1 = assertThrows(ExceptionDate.class, () -> {employe.setDateArrivee(null);
	});
	}
	   
	void setDateDepartNull()throws SauvegardeImpossible, ExceptionDate 
	{
		  Ligue ligue = gestionPersonnel.addLigue("Football");
		  Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test" , LocalDate.of(2023, 12, 31), LocalDate.of(2023, 1, 1));
		  Exception exception1 = assertThrows(ExceptionDate.class, () -> {employe.setDateDepart(null);
	  });
    }
	    
	@Test
	void testSetDateDepartInvalid() throws SauvegardeImpossible , ExceptionDate 
	{
		  Ligue ligue = gestionPersonnel.addLigue("Fléchettes"); 
		  Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
		  Exception exception = assertThrows(ExceptionDate.class , () -> employe.setDateDepart(LocalDate.of(2022, 1, 1)));
		  assertEquals("La date de départ ne peut pas être avant la date d'arrivée." , exception.getMessage());    
	}
	    
	@Test
	void testSetDateArriveInvalid() throws SauvegardeImpossible , ExceptionDate 
	{
		  Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		  Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test" , LocalDate.of(2023, 12, 31), LocalDate.of(2024, 1, 1));
		  Exception erreur = assertThrows(ExceptionDate.class, () -> employe.setDateArrivee(LocalDate.of(2024, 12, 2)));
		  assertEquals("La date de départ ne peut pas être avant la date d'arrivée." , erreur.getMessage());
	}
	  
	@Test
	void testGetDate() throws SauvegardeImpossible ,  ExceptionDate 
	{
		  Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		  Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test" , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
		  assertEquals( employe.getDateArrivee(), LocalDate.of(2023, 1, 1));
		  assertEquals( employe.getDateDepart(), LocalDate.of(2023, 12, 31));
	 }	 
	   
}