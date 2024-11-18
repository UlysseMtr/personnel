CREATE TABLE Ligue(
   id_ligue INT,
   PRIMARY KEY(id_ligue)
);

CREATE TABLE Employe(
   id_employe INT,
   nom VARCHAR(30),
   prenom VARCHAR(30),
   mail VARCHAR(50),
   password VARCHAR(30),
   status VARCHAR(20),
   dateArrivee DATE,
   dateDepart DATE,
   id_ligue INT,
   PRIMARY KEY(id_employe),
   FOREIGN KEY(id_ligue) REFERENCES Ligue(id_ligue)
);
