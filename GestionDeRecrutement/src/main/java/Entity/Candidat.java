package Entity;

import java.util.Objects;

public class Candidat {
    private Integer id;
    private String nomComplet;
    private String email;
    private String telephone;
    private String linkedinUrl;

    public Candidat() {}

    public Candidat(Integer id, String nomComplet, String email, String telephone, String linkedinUrl) {
        this.id = id;
        this.nomComplet = nomComplet;
        this.email = email;
        this.telephone = telephone;
        this.linkedinUrl = linkedinUrl;
    }

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidat)) return false;
        Candidat that = (Candidat) o;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
    @Override public String toString() { return nomComplet; }
}
