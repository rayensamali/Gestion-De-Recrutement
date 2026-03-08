package Entity;

import java.util.Objects;

public class Poste {
    private Integer id;
    private String intitule;
    private String departement;
    private GradePoste grade;
    private ModeTravail modeTravail;
    private TypeContrat typeContrat;

    public Poste() {}

    public Poste(Integer id, String intitule, String departement, GradePoste grade, ModeTravail modeTravail, TypeContrat typeContrat) {
        this.id = id;
        this.intitule = intitule;
        this.departement = departement;
        this.grade = grade;
        this.modeTravail = modeTravail;
        this.typeContrat = typeContrat;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }
    public GradePoste getGrade() { return grade; }
    public void setGrade(GradePoste grade) { this.grade = grade; }
    public ModeTravail getModeTravail() { return modeTravail; }
    public void setModeTravail(ModeTravail modeTravail) { this.modeTravail = modeTravail; }
    public TypeContrat getTypeContrat() { return typeContrat; }
    public void setTypeContrat(TypeContrat typeContrat) { this.typeContrat = typeContrat; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poste)) return false;
        return Objects.equals(id, ((Poste)o).id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
