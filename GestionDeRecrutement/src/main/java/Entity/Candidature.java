package Entity;

import java.time.LocalDate;
import java.util.Objects;

public class Candidature {
    private Integer id;
    private String code; // ex: CAND--0032 (facultatif)
    private Integer candidatId; // FK
    private Integer posteId;    // FK
    private LocalDate dateCandidature;
    private PhaseRecrutement phase;         // étape actuelle
    private ResultatScreening resultatScreening; // pour « Screening RH »
    private DecisionFinale decisionFinale;  // Décision finale
    private LocalDate dateDecision;

    public Candidature() {}

    public Candidature(Integer id, String code, Integer candidatId, Integer posteId, LocalDate dateCandidature,
                       PhaseRecrutement phase, ResultatScreening resultatScreening,
                       DecisionFinale decisionFinale, LocalDate dateDecision) {
        this.id = id;
        this.code = code;
        this.candidatId = candidatId;
        this.posteId = posteId;
        this.dateCandidature = dateCandidature;
        this.phase = phase;
        this.resultatScreening = resultatScreening;
        this.decisionFinale = decisionFinale;
        this.dateDecision = dateDecision;
    }

    // getters/setters…

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getCandidatId() { return candidatId; }
    public void setCandidatId(Integer candidatId) { this.candidatId = candidatId; }
    public Integer getPosteId() { return posteId; }
    public void setPosteId(Integer posteId) { this.posteId = posteId; }
    public LocalDate getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(LocalDate dateCandidature) { this.dateCandidature = dateCandidature; }
    public PhaseRecrutement getPhase() { return phase; }
    public void setPhase(PhaseRecrutement phase) { this.phase = phase; }
    public ResultatScreening getResultatScreening() { return resultatScreening; }
    public void setResultatScreening(ResultatScreening resultatScreening) { this.resultatScreening = resultatScreening; }
    public DecisionFinale getDecisionFinale() { return decisionFinale; }
    public void setDecisionFinale(DecisionFinale decisionFinale) { this.decisionFinale = decisionFinale; }
    public LocalDate getDateDecision() { return dateDecision; }
    public void setDateDecision(LocalDate dateDecision) { this.dateDecision = dateDecision; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidature)) return false;
        return Objects.equals(id, ((Candidature)o).id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
