package Service;

import Entity.CompetencePoste;
import java.util.List;
public interface CompetencePosteRepository extends CrudRepository<CompetencePoste, Integer>{

        List<CompetencePoste> findByPoste(int posteId);
        List<CompetencePoste> findByCompetence(int competenceId);
    }


