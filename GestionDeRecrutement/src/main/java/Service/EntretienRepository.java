package Service;

import Entity.Entretien;
import java.time.LocalDateTime;
import java.util.List;
public interface EntretienRepository extends CrudRepository<Entretien, Integer>{

        List<Entretien> findByCandidature(int candidatureId);
        List<Entretien> findBetween(LocalDateTime from, LocalDateTime to);
        boolean existsOverlapForCandidat(int candidatId, LocalDateTime start, LocalDateTime end, Integer excludeEntretienId);
        boolean existsOverlapForRecruteur(int recruteurId, LocalDateTime start, LocalDateTime end, Integer excludeEntretienId);
    }

