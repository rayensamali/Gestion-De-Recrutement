package Service;

import Entity.CategorieCompetence;
import Entity.Competence;
import Entity.Niveau;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceCompetence implements CompetenceRepository {

     private final Connection cnx = DataSource.getInstance().getConnection();

        @Override
        public Integer create(Competence c) {
            String sql = "INSERT INTO competence(nom, categorie, niveau) VALUES (?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getNom());
                ps.setString(2, c.getCategorie().name());
                ps.setString(3, c.getNiveau().name());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        c.setId(id);
                        return id;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("create Competence failed: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        public boolean update(Competence c) {
            String sql = "UPDATE competence SET nom=?, categorie=?, niveau=? WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, c.getNom());
                ps.setString(2, c.getCategorie().name());
                ps.setString(3, c.getNiveau().name());
                ps.setInt(4, c.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("update Competence failed: " + e.getMessage(), e);
            }
        }

        @Override
        public boolean deleteById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM competence WHERE id=?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        @Override
        public Optional<Competence> findById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM competence WHERE id=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return Optional.empty();
        }

        @Override
        public List<Competence> findAll() {
            List<Competence> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM competence ORDER BY nom");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public List<Competence> findByCategorie(CategorieCompetence categorie) {
            List<Competence> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM competence WHERE categorie=? ORDER BY nom")) {
                ps.setString(1, categorie.name());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        private Competence map(ResultSet rs) throws SQLException {
            Competence c = new Competence();
            c.setId(rs.getInt("id"));
            c.setNom(rs.getString("nom"));
            c.setCategorie(CategorieCompetence.valueOf(rs.getString("categorie")));
            c.setNiveau(Niveau.valueOf(rs.getString("niveau")));
            return c;
        }
    }
