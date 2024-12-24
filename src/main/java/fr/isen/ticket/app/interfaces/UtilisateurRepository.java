package fr.isen.ticket.app.interfaces;

import fr.isen.ticket.app.interfaces.models.Utilisateur;
import fr.isen.ticket.app.interfaces.models.UtilisateurModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UtilisateurRepository implements PanacheRepository<Utilisateur> {

    // Méthode pour insérer un utilisateur dans la base de données
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        persist(utilisateur);  // Persiste l'utilisateur dans la BDD
        return utilisateur;  // Retourne l'utilisateur après l'insertion
    }
}