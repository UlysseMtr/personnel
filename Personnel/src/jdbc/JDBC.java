package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.Passerelle;
import personnel.SauvegardeImpossible;

public class JDBC implements Passerelle 
{
	Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel() 
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try 
		{
			// Lecture du root
			String requeteRoot = "select * from employe where ID_Ligue is null";
			Statement instructionRoot = connection.createStatement();
			ResultSet root = instructionRoot.executeQuery(requeteRoot);
			if (root.next())
			{
				Employe rootEmploye = new Employe(gestionPersonnel, 
					root.getInt("id"),
					root.getString("nomEmploye"),
					root.getString("prenomEmploye"),
					root.getString("mail"),
					root.getString("passwd"),
					LocalDate.parse(root.getString("datearv")),
					root.getString("datedepart") != null ? LocalDate.parse(root.getString("datedepart")) : null,
					null);
				gestionPersonnel.addRoot(rootEmploye);
			}

			// Lecture des ligues (code existant)
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return gestionPersonnel;
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
				"INSERT INTO ligue (nomLigue) VALUES(?)", 
				Statement.RETURN_GENERATED_KEYS
			);
			instruction.setString(1, ligue.getNom());     
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	@Override
	public int insert(Employe employe) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into employe (prenomEmploye, nomEmploye, mail, passwd, datearv, datedepart, Admin, ID_Ligue) values(?)" + 
			"values(?, ?, ?, ?, ?, ?, ?)",
			Statement.RETURN_GENERATED_KEYS);
			
			instruction.setString(1, employe.getPrenom());	
			instruction.setString(2, employe.getNom());	
			instruction.setString(3, employe.getMail());	
			instruction.setString(4, employe.getPassword());	
			instruction.setString(5, employe.getDateArrivee().toString());	

			if (employe.getDateDepart() != null)
				instruction.setString(6, employe.getDateDepart().toString());    
			else
				instruction.setNull(6, java.sql.Types.VARCHAR);
				
			instruction.setBoolean(7, employe.estAdmin(employe.getLigue()));
			instruction.setInt(8, employe.getLigue().getIdLigue());	
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
}
