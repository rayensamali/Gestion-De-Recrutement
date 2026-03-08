package Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

    public class Entretien {
        private Integer id;
        private String code; // ex: ENT--0021 (optionnel)
        private Integer candidatureId; // FK vers Candidature
        private Integer candidatId;
        private TypeEntretien type;
        private StatutEntretien statut;
        private LocalDate dateEntretien;
        private LocalTime heureDebut;
        private LocalTime heureFin;
        private Integer dureeMinutes;

        // Recruteurs associés
        private Integer recruteurTechniqueId;
        private Integer recruteurRhId;
        private Integer recruteurManagerId;

        public Entretien() {}

        public Entretien(Integer id, String code, Integer candidatureId, Integer candidatId, TypeEntretien type,
                         StatutEntretien statut, LocalDate dateEntretien, LocalTime heureDebut, LocalTime heureFin,
                         Integer dureeMinutes, Integer recruteurTechniqueId, Integer recruteurRhId, Integer recruteurManagerId) {
            this.id = id;
            this.code = code;
            this.candidatureId = candidatureId;
            this.candidatId = candidatId;
            this.type = type;
            this.statut = statut;
            this.dateEntretien = dateEntretien;
            this.heureDebut = heureDebut;
            this.heureFin = heureFin;
            this.dureeMinutes = dureeMinutes;
            this.recruteurTechniqueId = recruteurTechniqueId;
            this.recruteurRhId = recruteurRhId;
            this.recruteurManagerId = recruteurManagerId;
        }

        // getters/setters…

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public Integer getCandidatureId() { return candidatureId; }
        public void setCandidatureId(Integer candidatureId) { this.candidatureId = candidatureId; }
        public Integer getCandidatId() { return candidatId; }
        public void setCandidatId(Integer candidatId) { this.candidatId = candidatId; }
        public TypeEntretien getType() { return type; }
        public void setType(TypeEntretien type) { this.type = type; }
        public StatutEntretien getStatut() { return statut; }
        public void setStatut(StatutEntretien statut) { this.statut = statut; }
        public LocalDate getDateEntretien() { return dateEntretien; }
        public void setDateEntretien(LocalDate dateEntretien) { this.dateEntretien = dateEntretien; }
        public LocalTime getHeureDebut() { return heureDebut; }
        public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
        public LocalTime getHeureFin() { return heureFin; }
        public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
        public Integer getDureeMinutes() { return dureeMinutes; }
        public void setDureeMinutes(Integer dureeMinutes) { this.dureeMinutes = dureeMinutes; }
        public Integer getRecruteurTechniqueId() { return recruteurTechniqueId; }
        public void setRecruteurTechniqueId(Integer recruteurTechniqueId) { this.recruteurTechniqueId = recruteurTechniqueId; }
        public Integer getRecruteurRhId() { return recruteurRhId; }
        public void setRecruteurRhId(Integer recruteurRhId) { this.recruteurRhId = recruteurRhId; }
        public Integer getRecruteurManagerId() { return recruteurManagerId; }
        public void setRecruteurManagerId(Integer recruteurManagerId) { this.recruteurManagerId = recruteurManagerId; }

        public LocalDateTime getStartDateTime() {
            return LocalDateTime.of(dateEntretien, heureDebut);
        }
        public LocalDateTime getEndDateTime() {
            return LocalDateTime.of(dateEntretien, heureFin);
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entretien)) return false;
            return Objects.equals(id, ((Entretien)o).id);
        }
        @Override public int hashCode() { return Objects.hash(id); }
    }


