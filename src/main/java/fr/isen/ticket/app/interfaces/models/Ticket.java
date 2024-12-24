package fr.isen.ticket.app.interfaces.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket") // Correspond au nom de la table dans la BDD
public class Ticket {

    @Id
    private String id;

    @Column(name = "titre")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date_creation")
    private LocalDateTime createdAt;

    @Column(name = "date_mise_a_jour")
    private LocalDateTime updatedAt;

    @Column(name = "impact")
    private String impact;

    @Column(name = "etat")
    private String state;

    @Column(name = "utilisateur_createur")
    private String createdBy;

    @Column(name = "poste_informatique")
    private String computerStation;

    @Column(name = "type_de_demande")
    private String requestType;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();  // Génération automatique d'un UUID pour l'ID
        }
    }

    // Getters et setters...
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getComputerStation() {
        return computerStation;
    }

    public void setComputerStation(String computerStation) {
        this.computerStation = computerStation;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "Ticket [\n" +
                "  id='" + id + "',\n" +
                "  title='" + title + "',\n" +
                "  description='" + description + "',\n" +
                "  createdAt=" + createdAt + ",\n" +
                "  updatedAt=" + updatedAt + ",\n" +
                "  impact='" + impact + "',\n" +
                "  state='" + state + "',\n" +
                "  createdBy='" + createdBy + "',\n" +
                "  computerStation='" + computerStation + "',\n" +
                "  requestType='" + requestType + "'\n" +
                "]";
    }


}
