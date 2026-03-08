package Entity;

import java.util.Objects;

public class Recruteur {
    private Integer id;
    private String nomComplet;
    private String email;
    private Role role; // TECHNIQUE/RH/MANAGER

    public Recruteur() {}

    public Recruteur(Integer id, String nomComplet, String email, Role role) {
        this.id = id;
        this.nomComplet = nomComplet;
        this.email = email;
        this.role = role;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recruteur)) return false;
        Recruteur r = (Recruteur) o;
        return Objects.equals(id, r.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
