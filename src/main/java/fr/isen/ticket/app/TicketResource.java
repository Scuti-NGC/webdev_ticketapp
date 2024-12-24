package fr.isen.ticket.app;

import fr.isen.ticket.app.interfaces.TicketRepository;
import fr.isen.ticket.app.interfaces.models.Ticket;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@Path("/ticket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketResource {

    private final LecteurJSON lecteurJSON;

    public TicketResource() {
        this.lecteurJSON = new LecteurJSON(); // Initialisation de la classe LecteurJSON
        try {
            lecteurJSON.intitialiserJSON("ticket.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject
    TicketRepository ticketRepository;

    // SECTION : Gestion des tickets via JSON
    // Méthode pour obtenir le tableau de tickets depuis le JSON
    private JSONArray getTicketsArray() throws IOException {
        String jsonContent = lecteurJSON.lireJSON("ticket.json"); // Appel à la méthode lireJSON
        JSONObject jsonObject = new JSONObject(jsonContent);
        return jsonObject.getJSONArray("ticket");
    }

    // GET : Récupérer tous les tickets depuis le fichier JSON
    @GET
    public Response getAllTickets() {
        try {
            JSONArray ticketsArray = getTicketsArray();
            return Response.ok(ticketsArray.toString()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la lecture des tickets.").build();
        }
    }

    // GET : Récupérer un ticket par ID depuis le fichier JSON
    @GET
    @Path("/{id}")
    public Response getTicketById(@PathParam("id") String id) {
        try {
            JSONArray ticketsArray = getTicketsArray();
            for (int i = 0; i < ticketsArray.length(); i++) {
                JSONObject ticket = ticketsArray.getJSONObject(i);
                if (ticket.getString("id").equals(id)) {
                    return Response.ok(ticket.toString()).build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket non trouvé.").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la lecture des tickets.").build();
        }
    }

    // POST : Créer un nouveau ticket dans le fichier JSON
    @POST
    public Response createTicket(String input) {
        try {
            JSONObject newTicket = new JSONObject(input);

            JSONArray ticketsArray = getTicketsArray();

            int newId = 0;
            for (int i = 0; i < ticketsArray.length(); i++) {
                JSONObject ticket = ticketsArray.getJSONObject(i);
                int id = ticket.getInt("id");
                if (id > newId) {
                    newId = id;
                }
            }
            newId++;
            newTicket.put("id", String.valueOf(newId));

            ticketsArray.put(newTicket);

            lecteurJSON.ecrireJSON("ticket.json", ticketsArray.toString());

            return Response.status(Response.Status.CREATED).entity(newTicket.toString()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la création du ticket.").build();
        }
    }

    // PUT : Mettre à jour un ticket dans le fichier JSON
    @PUT
    @Path("/{id}")
    public Response updateTicket(@PathParam("id") String id, String input) {
        try {
            JSONObject updatedFields = new JSONObject(input);
            JSONArray ticketsArray = getTicketsArray();

            for (int i = 0; i < ticketsArray.length(); i++) {
                JSONObject ticket = ticketsArray.getJSONObject(i);
                if (ticket.getString("id").equals(id)) {
                    for (String key : updatedFields.keySet()) {
                        ticket.put(key, updatedFields.get(key));
                    }
                    lecteurJSON.ecrireJSON("ticket.json", ticketsArray.toString());
                    return Response.ok("Ticket mis à jour.").build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket non trouvé.").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la mise à jour du ticket.").build();
        }
    }

    // DELETE : Supprimer un ticket du fichier JSON
    @DELETE
    @Path("/{id}")
    public Response deleteTicket(@PathParam("id") String id) {
        try {
            JSONArray ticketsArray = getTicketsArray();

            for (int i = 0; i < ticketsArray.length(); i++) {
                JSONObject ticket = ticketsArray.getJSONObject(i);
                if (ticket.getString("id").equals(id)) {
                    ticketsArray.remove(i);
                    return Response.noContent().build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket non trouvé.").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la suppression du ticket.").build();
        }
    }

    // SECTION : Gestion des tickets via BDD
    // GET : Récupérer tous les tickets depuis la base de données
    @GET
    @Path("/bdd")
    public List<Ticket> getAllTicketsBDD() {
        return ticketRepository.listAll(); // Renvoie tous les tickets
    }

    // GET : Récupérer un ticket par ID depuis la base de données
    @GET
    @Path("/bdd/{id}")
    public Ticket getTicketByIdBDD(@PathParam("id") String id) {
        Ticket ticket = ticketRepository.find("id", id).firstResult();
        if (ticket == null) {
            throw new NotFoundException("Ticket non trouvé.");
        }
        return ticket;
    }

    // POST : Créer un nouveau ticket dans la base de données
    @POST
    @Path("/bdd")
    public Response createTicketBDD(Ticket ticket) {
        ticketRepository.persist(ticket); // Ajoute un nouveau ticket
        return Response.status(Response.Status.CREATED).entity(ticket).build();
    }

    // PUT : Mettre à jour un ticket dans la base de données
    @PUT
    @Path("/bdd/{id}")
    public Response updateTicketBDD(@PathParam("id") String id, Ticket updatedTicket) {
        Ticket ticket = ticketRepository.find("id", id).firstResult();
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket non trouvé.").build();
        }
        ticket.setTitle(updatedTicket.getTitle());
        ticket.setDescription(updatedTicket.getDescription());
        // Ajoutez les autres champs à mettre à jour ici...
        ticketRepository.persist(ticket);
        return Response.ok("Ticket mis à jour.").build();
    }

    // DELETE : Supprimer un ticket de la base de données
    @DELETE
    @Path("/bdd/{id}")
    public Response deleteTicketBDD(@PathParam("id") String id) {
        Ticket ticket = ticketRepository.find("id", id).firstResult();
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket non trouvé.").build();
        }
        ticketRepository.delete(ticket);
        return Response.noContent().build();
    }
}
