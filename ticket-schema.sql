-- ticketapp2024.ticket definition
 
CREATE TABLE `ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,            -- Identifiant unique
  `titre` VARCHAR(255) NOT NULL,               -- Titre du ticket
  `description` TEXT,                          -- Description détaillée
  `impact` ENUM('Mineur', 'Majeur', 'Bloquant') DEFAULT 'Mineur', -- Impact (faible, moyen, élevé)
  `etat` ENUM('Ouvert', 'EnCours', 'Resolu', 'Ferme') DEFAULT 'Ouvert', -- État du ticket
  `utilisateur_createur` INT NOT NULL,         -- Référence à l'utilisateur créateur
  `type_demande` VARCHAR(100),                 -- Type de la demande
  `poste_informatique` INT,                    -- Référence à un poste informatique
  `date_mise_a_jour` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Dernière mise à jour
  `date_creation` DATETIME DEFAULT CURRENT_TIMESTAMP, -- Date de création
  PRIMARY KEY (`id`),                          -- Définition de la clé primaire
  FOREIGN KEY (`utilisateur_createur`) REFERENCES `utilisateur`(`id`) ON DELETE CASCADE, -- FK utilisateur
  FOREIGN KEY (`poste_informatique`) REFERENCES `poste`(`id`) ON DELETE SET NULL -- FK poste
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
