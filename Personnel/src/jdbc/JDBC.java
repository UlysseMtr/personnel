package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.Employe;
import personnel.ExceptionDate;
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
	public GestionPersonnel getGestionPersonnel() throws SauvegardeImpossible, ExceptionDate
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try
		{
			PreparedStatement instructionRoot;
			instructionRoot = connection.prepareStatement("select id_employe, prenomEmploye, nomEmploye, ID_Ligue, passwd from employe where id_ligue is null and id_employe = 1");
			ResultSet root = instructionRoot.executeQuery();

			if (!root.next()) {
				gestionPersonnel.addRoot("roverveot", null, null, "toor",
					LocalDate.parse("2020-12-12"), LocalDate.parse("2020-12-13"));
			} else {
				gestionPersonnel.addRoot(root.getInt("id_employe"), root.getString("nomEmploye"),
					null, null, root.getString("passwd"),
					LocalDate.parse("2020-12-12"), LocalDate.parse("2020-12-13"));
			}

			PreparedStatement instruction;
			instruction = connection.prepareStatement("select * from employe");
			ResultSet resultats = instruction.executeQuery();

			Ligue ligueCourante = null;
			int idLiguePrecedente = -1;

			while (resultats.next())
			{

				int idLigue = resultats.getInt("ID_Ligue");
				if (idLigue != idLiguePrecedente)
				{
					PreparedStatement instruction2;
					instruction2 = connection.prepareStatement("select * from ligue where id_ligue = (?)");
					instruction2.setInt(1, idLigue);
					ResultSet res = instruction2.executeQuery();


					if(res.next()){
						String Ligue = res.getString(2);
						ligueCourante = gestionPersonnel.addLigue(idLigue,Ligue);
						idLiguePrecedente = idLigue;
						Employe employe = ligueCourante.addEmploye(
								resultats.getInt("id_employe"),
								resultats.getString("nomEmploye"),
								resultats.getString("prenomEmploye"),
								resultats.getString("mail"),
								resultats.getString("passwd"),
								LocalDate.parse(resultats.getString("datearv")),
								resultats.getString("datedepart") != null ?
									LocalDate.parse(resultats.getString("datedepart")) : null,
								resultats.getBoolean("Admin")

							);
						if (resultats.getBoolean("Admin"))
						{
							ligueCourante.setAdministrateur(employe);
						}
					}
				}



			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SauvegardeImpossible(e);
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
			instruction = connection.prepareStatement("insert into employe (prenomEmploye, nomEmploye, mail, passwd, datearv, datedepart, Admin, ID_Ligue) " +
			"values(?, ?, ?, ?, ?, ?, ?, ?)",
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

			if (employe.getLigue() == null) {
				instruction.setNull(8, java.sql.Types.INTEGER);
				instruction.setBoolean(7,false);
			}
			else {
				instruction.setInt(8, employe.getLigue().getIdLigue());
				instruction.setBoolean(7, employe.estAdmin(employe.getLigue()));
			}
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
	public void update(Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
				"UPDATE ligue SET nomLigue = ? WHERE ID_Ligue = ?"
			);
			instruction.setString(1, ligue.getNom());
			instruction.setInt(2, ligue.getIdLigue());

			int lignesModifiees = instruction.executeUpdate();
			if (lignesModifiees == 0)
				throw new SauvegardeImpossible(new Exception("Aucune ligue n'a été modifiée"));
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	@Override
	public void update(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement(
				"UPDATE employe SET nomEmploye = ?, prenomEmploye = ?, mail = ?, " +
				"passwd = ?, datearv = ?, datedepart = ?, Admin = ? " +
				"WHERE id = ?"
			);

			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, java.sql.Date.valueOf(employe.getDateArrivee()));

			if (employe.getDateDepart() != null)
				instruction.setDate(6, java.sql.Date.valueOf(employe.getDateDepart()));
			else
				instruction.setNull(6, java.sql.Types.DATE);

			instruction.setBoolean(7, employe.estAdmin(employe.getLigue()));
			instruction.setInt(8, employe.getId());

			int lignesModifiees = instruction.executeUpdate();
			if (lignesModifiees == 0)
				throw new SauvegardeImpossible(new Exception("Aucun employé n'a été modifié"));
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

		@Override
	public void delete(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement(
				"DELETE FROM employe WHERE id = ?"
			);
			instruction.setInt(1, employe.getId());
			instruction.executeUpdate();
		}
		catch (SQLException exception)
		{
			throw new SauvegardeImpossible(exception);
		}
	}

	@Override
	public void delete(Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement deleteLigue = connection.prepareStatement(
				"DELETE FROM ligue WHERE ID_Ligue = ?"
			);
			deleteLigue.setInt(1, ligue.getIdLigue());
			deleteLigue.executeUpdate();
		}
		catch (SQLException exception)
		{
			throw new SauvegardeImpossible(exception);
		}
	}
}