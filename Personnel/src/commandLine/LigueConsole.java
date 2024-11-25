package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;

import personnel.*;

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	private Option afficherLigues()
	{
		return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
	}

	private Option afficher(final Ligue ligue)
	{
		return new Option("Afficher la ligue", "l", 
				() -> 
				{
					System.out.println(ligue);
					System.out.println("administrée par " + ligue.getAdministrateur());
				}
		);
	}
	private Option afficherEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
	}

	private Option ajouterLigue()
	{
		return new Option("Ajouter une ligue", "a", () -> 
		{
			try
			{
				gestionPersonnel.addLigue(getString("nom : "));
			}
			catch(SauvegardeImpossible exception)
			{
				System.err.println("Impossible de sauvegarder cette ligue");
			}
		});
	}
	
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.add(afficher(ligue));
		menu.add(gererEmployes(ligue));
		//menu.add(changerAdministrateur(ligue));
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {ligue.setNom(getString("Nouveau nom : "));});
	}

	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",
				() -> 
		
				{
					try {
					ligue.addEmploye(getString("nom : "), 
						getString("prenom : "), getString("mail : "), 
						getString("password : ") , getString("Donner le status") , LocalDate.parse(getString("Saisir date arriver")) , LocalDate.parse(getString("Saisir date depart")));
					}catch(ExceptionDate e) {
						e.printStackTrace();
						
					}
					catch(DateTimeParseException e) {
						System.out.println("Veuillez saisir un bon format de date ex: aaaa-mm-jj");
					}
				}
		);
	}
	
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.add(selectEmploye(ligue));
		menu.addBack("q");
		return menu;
	}
	
	private Menu editerEmployer(Employe employe) {
		Menu menu = new Menu("Gerer :" + employe.getNom());
		menu.add(modifierEmploye(employe));
		menu.add(supprimerEmploye(employe));
		menu.setAutoBack(true);
		menu.addBack("q");
		return menu;
	}
	
	//menu.add(modifierEmploye(ligue));
	//menu.add(supprimerEmploye(ligue));

	private Option supprimerEmploye(final Employe employe)
	{
		return new Option("Supprimer Employe", "s" , () -> employe.remove());
	}
	
	private List<Employe> changerAdministrateur(final Ligue ligue)
	{
		return null;
	}		

	private Option modifierEmploye(final Employe employe)
	{
		return new Option("Modifier Employe" , "c" , () -> employeConsole.editerEmploye());
	}
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {ligue.remove();});
	}
	
	private Option selectEmploye(Ligue ligue)
	{
		return new List<>("Selectionner un employe", "n", 
				() -> new ArrayList<>(ligue.getEmployes()),
				(nb) -> editerEmployer(nb));}
				;
	}
	

