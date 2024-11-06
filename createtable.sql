CREATE TABLE Ligue(
   id_ligue VARCHAR(50),
   PRIMARY KEY(id_ligue)
);

CREATE TABLE Employe(
   id_employe VARCHAR(50),
   nom VARCHAR(50),
   prenom VARCHAR(50),
   password VARCHAR(50),
   mail VARCHAR(50),
   status VARCHAR(50),
   dateArrivee DATE,
   dateDepart DATE,
   id_ligue VARCHAR(50),
   PRIMARY KEY(id_employe),
   FOREIGN KEY(id_ligue) REFERENCES Ligue(id_ligue)
);