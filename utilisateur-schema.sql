CREATE TABLE `utilisateur` (
  `id` INT NOT NULL AUTO_INCREMENT,                            -- Identifiant unique
  `nom` VARCHAR(100) NOT NULL,                                -- Nom de l'utilisateur
  `email` VARCHAR(150) NOT NULL UNIQUE,                       -- Email unique
  `mot_de_passe` VARCHAR(255) NOT NULL,                       -- Mot de passe
  `date_derniere_connexion` DATETIME DEFAULT NULL,            -- Dernière connexion
  `statut` BOOLEAN DEFAULT TRUE,                              -- Statut actif/inactif
  PRIMARY KEY (`id`)                                          -- Définition de la clé primaire
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
