package Service;

import Entity.Candidature;
import Entity.DecisionFinale;
import Entity.PhaseRecrutement;
import java.time.LocalDate;
import java.util.List;

public interface CandidatureRepository extends CrudRepository<Candidature, Integer>{

        List<Candidature> findByPhase(PhaseRecrutement phase);
        List<Candidature> findByCandidat(int candidatId);
        boolean updatePhase(int candidatureId, PhaseRecrutement nouvellePhase);
        boolean setDecisionFinale(int candidatureId, DecisionFinale decision, LocalDate dateDecision);
    }


