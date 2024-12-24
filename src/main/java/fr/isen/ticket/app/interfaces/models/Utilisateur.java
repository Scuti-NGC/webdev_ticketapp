package fr.isen.ticket.app.interfaces.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur") // Correspond au nom de la table dans la BDD
public class Utilisateur {

    @Id
    private String id;

    @Column(name = "nom")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mot_de_passe")
    private String password;

    @Column(name = "date_derniere_connexion")
    private LocalDateTime lastLogin;

    @Column(name = "statut")
    private String status;

    @Embedded
    private Role role;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = "U" + String.format("%03d", (int) (Math.random() * 1000)); // Génération automatique d'un ID unique
        }
    }

    // Getters et setters...
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Utilisateur [\n" +
                "  id='" + id + "',\n" +
                "  name='" + name + "',\n" +
                "  email='" + email + "',\n" +
                "  password='" + password + "',\n" +
                "  lastLogin=" + lastLogin + ",\n" +
                "  status='" + status + "',\n" +
                "  role=" + role + "\n" +
                "]";
    }

    // Classe interne pour gérer les rôles
    @Embeddable
    public static class Role {

        @Column(name = "utilisateur")
        private String utilisateur;

        @Column(name = "intervenant")
        private String intervenant;

        // Getters et setters...
        public String getUtilisateur() {
            return utilisateur;
        }

        public void setUtilisateur(String utilisateur) {
            this.utilisateur = utilisateur;
        }

        public String getIntervenant() {
            return intervenant;
        }

        public void setIntervenant(String intervenant) {
            this.intervenant = intervenant;
        }

        @Override
        public String toString() {
            return "{\n" +
                    "  utilisateur='" + utilisateur + "',\n" +
                    "  intervenant='" + intervenant + "'\n" +
                    "}";
        }
    }
}
