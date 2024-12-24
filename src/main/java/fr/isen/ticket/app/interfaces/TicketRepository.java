package fr.isen.ticket.app.interfaces;

import fr.isen.ticket.app.interfaces.models.Ticket;
import fr.isen.ticket.app.interfaces.models.TicketModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TicketRepository implements PanacheRepository<Ticket> {

    // Méthode pour insérer un ticket dans la base de données
    public Ticket createTicket(Ticket ticket) {
        persist(ticket);  // Persiste le ticket dans la BDD
        return ticket;  // Retourne le ticket après l'insertion
    }
}