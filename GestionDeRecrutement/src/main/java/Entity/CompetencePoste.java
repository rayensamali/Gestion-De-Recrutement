package Entity;
import java.util.Objects;

public class CompetencePoste {

        private Integer id;
        private Integer competenceId;
        private Integer posteId;
        private Niveau niveau; // niveau requis pour ce poste

        public CompetencePoste() {}

        public CompetencePoste(Integer id, Integer competenceId, Integer posteId, Niveau niveau) {
            this.id = id;
            this.competenceId = competenceId;
            this.posteId = posteId;
            this.niveau = niveau;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public Integer getCompetenceId() { return competenceId; }
        public void setCompetenceId(Integer competenceId) { this.competenceId = competenceId; }
        public Integer getPosteId() { return posteId; }
        public void setPosteId(Integer posteId) { this.posteId = posteId; }
        public Niveau getNiveau() { return niveau; }
        public void setNiveau(Niveau niveau) { this.niveau = niveau; }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompetencePoste)) return false;
            return Objects.equals(id, ((CompetencePoste)o).id);
        }
        @Override public int hashCode() { return Objects.hash(id); }

}
