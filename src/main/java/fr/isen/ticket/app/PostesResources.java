package fr.isen.ticket.app;

import fr.isen.ticket.app.interfaces.PosteRepository;
import fr.isen.ticket.app.interfaces.UtilisateurRepository;
import fr.isen.ticket.app.interfaces.models.PosteInformatique;
import fr.isen.ticket.app.interfaces.models.Utilisateur;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@Path("/poste")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostesResources {

    private final LecteurJSON lecteurJSON;

    public PostesResources() {
        this.lecteurJSON = new LecteurJSON();
        try {
            lecteurJSON.intitialiserJSON("device.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONArray getTicketsArray() throws IOException {
        String jsonContent = lecteurJSON.lireJSON("device.json");
        JSONObject jsonObject = new JSONObject(jsonContent);
        return jsonObject.getJSONArray("device");
    }

    @Inject
    PosteRepository posteRepository;

    // GET : Récupérer tous les tickets
    @GET
    public Response getAllTickets() {
        try {
            JSONArray ticketsArray = getTicketsArray();
            return Response.ok(ticketsArray.toString()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la lecture des tickets.").build();
        }
    }

    // GET : Récupérer un ticket par ID
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la lecture des tickets.").build();
        }
    }

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

            lecteurJSON.ecrireJSON("device.json", ticketsArray.toString());

            return Response.status(Response.Status.CREATED).entity(newTicket.toString()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la création du ticket.").build();
        }
    }



    // PUT : Mettre à jour un ticket
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
                    lecteurJSON.ecrireJSON("device.json", ticketsArray.toString());

                    return Response.ok("Ticket mis à jour.").build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket non trouvé.").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la mise à jour du ticket.").build();
        }
    }

    // DELETE : Supprimer un ticket
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la suppression du ticket.").build();
        }
    }

    @GET
    @Path("/bdd")
    public List<PosteInformatique> getAllPotesInformatiqueBDD() {
        return posteRepository.listAll(); // Renvoie tous les tickets
    }

    @GET
    @Path("/bdd/{id}")
    public PosteInformatique getPostesInformatiqueByIdBDD(@PathParam("id") String id) {
        PosteInformatique posteInformatique = posteRepository.find("id", id).firstResult();
        if (posteInformatique == null) {
            throw new NotFoundException("Ticket non trouvé.");
        }
        return posteInformatique;
    }
}
