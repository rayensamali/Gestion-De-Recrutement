package Entity;

import java.util.Objects;

public class Competence {

        private Integer id;
        private String nom;
        private CategorieCompetence categorie;
        private Niveau niveau; // niveau « par défaut » s’il y a lieu

        public Competence() {}

        public Competence(Integer id, String nom, CategorieCompetence categorie, Niveau niveau) {
            this.id = id;
            this.nom = nom;
            this.categorie = categorie;
            this.niveau = niveau;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public CategorieCompetence getCategorie() { return categorie; }
        public void setCategorie(CategorieCompetence categorie) { this.categorie = categorie; }
        public Niveau getNiveau() { return niveau; }
        public void setNiveau(Niveau niveau) { this.niveau = niveau; }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Competence)) return false;
            return Objects.equals(id, ((Competence)o).id);
        }
        @Override public int hashCode() { return Objects.hash(id); }
}


