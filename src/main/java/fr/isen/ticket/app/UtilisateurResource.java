package fr.isen.ticket.app;

import fr.isen.ticket.app.interfaces.UtilisateurRepository;
import fr.isen.ticket.app.interfaces.models.Utilisateur;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@Path("/utilisateur")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurResource {

    private final LecteurJSON lecteurJSON;

    public UtilisateurResource() {
        this.lecteurJSON = new LecteurJSON(); // Initialisation de la classe LecteurJSON

        try {
            lecteurJSON.intitialiserJSON("user.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONArray getutilisateursArray() throws IOException {
        String jsonContent = lecteurJSON.lireJSON("user.json");
        JSONObject jsonObject = new JSONObject(jsonContent);
        return jsonObject.getJSONArray("user");


    }

    @Inject
    UtilisateurRepository utilisateurRepository;

    // GET : Récupérer tous les utilisateurs
    @GET
    public Response getAllutilisateurs() {
        try {
            JSONArray utilisateursArray = getutilisateursArray();
            return Response.ok(utilisateursArray.toString()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la lecture des utilisateurs.").build();
        }
    }

    // GET : Récupérer un utilisateur par ID
    @GET
    @Path("/{id}")
    public Response getutilisateurById(@PathParam("id") String id) {
        try {
            JSONArray utilisateursArray = getutilisateursArray();
            for (int i = 0; i < utilisateursArray.length(); i++) {
                JSONObject utilisateur = utilisateursArray.getJSONObject(i);
                if (utilisateur.getString("id").equals(id)) {
                    return Response.ok(utilisateur.toString()).build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("utilisateur non trouvé.").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la lecture des utilisateurs.").build();
        }
    }

    // POST : Créer un nouveau utilisateur
    @POST
    public Response createutilisateur(String input) {
        try {
            JSONObject newUtilisateur = new JSONObject(input);

            JSONArray utilisateursArray = getutilisateursArray();

            int newId = 0;
            for (int i = 0; i < utilisateursArray.length(); i++) {
                JSONObject utilisateur = utilisateursArray.getJSONObject(i);
                int id = utilisateur.getInt("id");
                if (id > newId) {
                    newId = id;
                }
            }
            newId++;
            newUtilisateur.put("id", String.valueOf(newId));

            utilisateursArray.put(newUtilisateur);

            lecteurJSON.ecrireJSON("user.json", utilisateursArray.toString());

            return Response.status(Response.Status.CREATED).entity(newUtilisateur.toString()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la création du utilisateur.").build();
        }
    }

    // PUT : Mettre à jour un utilisateur
    @PUT
    @Path("/{id}")
    public Response updateutilisateur(@PathParam("id") String id, String input) {
        try {
            JSONObject updatedFields = new JSONObject(input);
            JSONArray utilisateursArray = getutilisateursArray();

            for (int i = 0; i < utilisateursArray.length(); i++) {
                JSONObject utilisateur = utilisateursArray.getJSONObject(i);
                if (utilisateur.getString("id").equals(id)) {
                    for (String key : updatedFields.keySet()) {
                        utilisateur.put(key, updatedFields.get(key));
                    }
                    lecteurJSON.ecrireJSON("user.json", utilisateursArray.toString());

                    return Response.ok("utilisateur mis à jour.").build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("utilisateur non trouvé.").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la mise à jour du utilisateur.").build();
        }
    }

    // DELETE : Supprimer un utilisateur
    @DELETE
    @Path("/{id}")
    public Response deleteutilisateur(@PathParam("id") String id) {
        try {
            JSONArray utilisateursArray = getutilisateursArray();

            for (int i = 0; i < utilisateursArray.length(); i++) {
                JSONObject utilisateur = utilisateursArray.getJSONObject(i);
                if (utilisateur.getString("id").equals(id)) {
                    utilisateursArray.remove(i);
                    return Response.noContent().build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("utilisateur non trouvé.").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la suppression du utilisateur.").build();
        }
    }

    @GET
    @Path("/bdd")
    public List<Utilisateur> getAllUtilisateursBDD() {
        return utilisateurRepository.listAll(); // Renvoie tous les utilisateurs
    }

    @GET
    @Path("/bdd/{id}")
    public Utilisateur getUtilisateursByIdBDD(@PathParam("id") String id) {
        Utilisateur utilisateur = utilisateurRepository.find("id", id).firstResult();
        if (utilisateur == null) {
            throw new NotFoundException("Utilisateur non trouvé.");
        }
        return utilisateur;
    }
}
