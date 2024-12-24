CREATE TABLE `poste` (
  `id` INT NOT NULL AUTO_INCREMENT,                            -- Identifiant unique
  `utilisateur_affecte` INT,                                  -- Référence à l'utilisateur affecté
  `configuration` TEXT,                                       -- Configuration du poste
  `etat` ENUM('EnFonction', 'EnMaintenance', 'EnCommande') DEFAULT 'EnFonction', -- État du poste
  PRIMARY KEY (`id`),                                         -- Définition de la clé primaire
  FOREIGN KEY (`utilisateur_affecte`) REFERENCES `utilisateur`(`id`) ON DELETE SET NULL -- FK utilisateur
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
