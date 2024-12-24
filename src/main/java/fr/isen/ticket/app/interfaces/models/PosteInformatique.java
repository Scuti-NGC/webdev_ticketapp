package fr.isen.ticket.app.interfaces.models;

import jakarta.persistence.*;

@Entity
@Table(name = "postes_informatique")  // Correspond au nom de la table dans la BDD
public class PosteInformatique {

    @Id
    private String id;

    @Column(name = "utilisateur_affecte")
    private String utilisateurAffecte;

    @Column(name = "etat")
    private String etat;

    @Column(name = "configuration")
    private String configuration;

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUtilisateurAffecte() {
        return utilisateurAffecte;
    }

    public void setUtilisateurAffecte(String utilisateurAffecte) {
        this.utilisateurAffecte = utilisateurAffecte;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return String.format(
                "PosteInformatique {\n" +
                        "  id='%s',\n" +
                        "  utilisateurAffecte='%s',\n" +
                        "  etat='%s',\n" +
                        "  configuration='%s'\n" +
                        "}",
                id, utilisateurAffecte, etat, configuration
        );
    }

}
