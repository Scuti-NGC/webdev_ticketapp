package fr.isen.ticket.app.interfaces.models;

import java.util.Date;
import fr.isen.ticket.app.interfaces.models.enums.ROLE;

public class UtilisateurModel {
    private int id;

    private String nom;

    private String email;

    private String mot_de_passe;

    private Date date_derniere_connexion;

    private boolean statut;

    private ROLE role;

}
