package fr.isen.ticket.app.interfaces;

import fr.isen.ticket.app.interfaces.models.PosteInformatique;
import fr.isen.ticket.app.interfaces.models.PosteModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PosteRepository implements PanacheRepository<PosteInformatique> {

    // Méthode pour insérer un poste dans la base de données
    public PosteInformatique createPoste(PosteInformatique poste) {
        persist(poste);  // Persiste le poste dans la BDD
        return poste;  // Retourne le poste après l'insertion
    }
}