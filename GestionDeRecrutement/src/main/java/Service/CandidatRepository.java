package Service;
import Entity.Candidat;
import java.util.List;
import java.util.Optional;

public interface CandidatRepository extends CrudRepository<Candidat, Integer>{

     Optional<Candidat> findByEmail(String email);
        List<Candidat> searchByName(String nameLike);
    }
