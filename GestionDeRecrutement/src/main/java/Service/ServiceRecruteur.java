package Service;

import Entity.Recruteur;
import Entity.Role;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceRecruteur implements RecruteurRepository{

     private final Connection cnx = DataSource.getInstance().getConnection();

        @Override
        public Integer create(Recruteur r) {
            String sql = "INSERT INTO recruteur(nom_complet, email, role) VALUES (?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, r.getNomComplet());
                ps.setString(2, r.getEmail());
                ps.setString(3, r.getRole().name());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        r.setId(id);
                        return id;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("create Recruteur failed: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        public boolean update(Recruteur r) {
            String sql = "UPDATE recruteur SET nom_complet=?, email=?, role=? WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, r.getNomComplet());
                ps.setString(2, r.getEmail());
                ps.setString(3, r.getRole().name());
                ps.setInt(4, r.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("update Recruteur failed: " + e.getMessage(), e);
            }
        }

        @Override
        public boolean deleteById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM recruteur WHERE id=?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("delete Recruteur failed: " + e.getMessage(), e);
            }
        }

        @Override
        public Optional<Recruteur> findById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM recruteur WHERE id=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(map(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("findById Recruteur failed: " + e.getMessage(), e);
            }
            return Optional.empty();
        }

        @Override
        public List<Recruteur> findAll() {
            List<Recruteur> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM recruteur ORDER BY nom_complet");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            } catch (SQLException e) {
                throw new RuntimeException("findAll Recruteur failed: " + e.getMessage(), e);
            }
            return list;
        }

        @Override
        public List<Recruteur> findByRole(Role role) {
            List<Recruteur> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM recruteur WHERE role=? ORDER BY nom_complet")) {
                ps.setString(1, role.name());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("findByRole Recruteur failed: " + e.getMessage(), e);
            }
            return list;
        }

        private Recruteur map(ResultSet rs) throws SQLException {
            Recruteur r = new Recruteur();
            r.setId(rs.getInt("id"));
            r.setNomComplet(rs.getString("nom_complet"));
            r.setEmail(rs.getString("email"));
            r.setRole(Role.valueOf(rs.getString("role")));
            return r;
        }
    }

