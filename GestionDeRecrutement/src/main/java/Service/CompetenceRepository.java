package Service;

import Entity.Competence;
import Entity.CategorieCompetence;
import java.util.List;

public interface CompetenceRepository extends CrudRepository<Competence, Integer> {

        List<Competence> findByCategorie(CategorieCompetence categorie);
    }
