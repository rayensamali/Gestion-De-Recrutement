package Service;

import Entity.*;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicePoste  implements PosteRepository {

     private final Connection cnx = DataSource.getInstance().getConnection();

        @Override
        public Integer create(Poste p) {
            String sql = "INSERT INTO poste(intitule, departement, grade, mode_travail, type_contrat) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getIntitule());
                ps.setString(2, p.getDepartement());
                ps.setString(3, p.getGrade().name());
                ps.setString(4, p.getModeTravail().name());
                ps.setString(5, p.getTypeContrat().name());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        p.setId(id);
                        return id;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("create Poste failed: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        public boolean update(Poste p) {
            String sql = "UPDATE poste SET intitule=?, departement=?, grade=?, mode_travail=?, type_contrat=? WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, p.getIntitule());
                ps.setString(2, p.getDepartement());
                ps.setString(3, p.getGrade().name());
                ps.setString(4, p.getModeTravail().name());
                ps.setString(5, p.getTypeContrat().name());
                ps.setInt(6, p.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("update Poste failed: " + e.getMessage(), e);
            }
        }

        @Override
        public boolean deleteById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM poste WHERE id=?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("delete Poste failed: " + e.getMessage(), e);
            }
        }

        @Override
        public Optional<Poste> findById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM poste WHERE id=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(map(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("findById Poste failed: " + e.getMessage(), e);
            }
            return Optional.empty();
        }

        @Override
        public List<Poste> findAll() {
            List<Poste> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM poste ORDER BY created_on DESC, id DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            } catch (SQLException e) {
                throw new RuntimeException("findAll Poste failed: " + e.getMessage(), e);
            }
            return list;
        }

        private Poste map(ResultSet rs) throws SQLException {
            Poste p = new Poste();
            p.setId(rs.getInt("id"));
            p.setIntitule(rs.getString("intitule"));
            p.setDepartement(rs.getString("departement"));
            p.setGrade(GradePoste.valueOf(rs.getString("grade")));
            p.setModeTravail(ModeTravail.valueOf(rs.getString("mode_travail")));
            p.setTypeContrat(TypeContrat.valueOf(rs.getString("type_contrat")));
            return p;
        }
    }

