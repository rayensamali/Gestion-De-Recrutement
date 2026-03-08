package Service;

import Entity.Recruteur;
import Entity.Role;
import java.util.List;

public interface RecruteurRepository extends CrudRepository<Recruteur, Integer> {

    List<Recruteur> findByRole(Role role);

}
