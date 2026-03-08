package Service;

import Entity.Candidat;
import Utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

    public class ServiceCandidat implements CandidatRepository {
        private final Connection cnx = DataSource.getInstance().getConnection();

        @Override
        public Integer create(Candidat c) {
            String sql = "INSERT INTO candidat(nom_complet, email, telephone, linkedin_url) VALUES (?,?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getNomComplet());
                ps.setString(2, c.getEmail());
                ps.setString(3, c.getTelephone());
                ps.setString(4, c.getLinkedinUrl());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        c.setId(id);
                        return id;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("create Candidat failed: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        public boolean update(Candidat c) {
            String sql = "UPDATE candidat SET nom_complet=?, email=?, telephone=?, linkedin_url=? WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, c.getNomComplet());
                ps.setString(2, c.getEmail());
                ps.setString(3, c.getTelephone());
                ps.setString(4, c.getLinkedinUrl());
                ps.setInt(5, c.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("update Candidat failed: " + e.getMessage(), e);
            }
        }

        @Override
        public boolean deleteById(Integer id) {
            String sql = "DELETE FROM candidat WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException("delete Candidat failed: " + e.getMessage(), e);
            }
        }

        @Override
        public Optional<Candidat> findById(Integer id) {
            String sql = "SELECT * FROM candidat WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(map(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("findById Candidat failed: " + e.getMessage(), e);
            }
            return Optional.empty();
        }

        @Override
        public List<Candidat> findAll() {
            String sql = "SELECT * FROM candidat ORDER BY nom_complet";
            List<Candidat> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            } catch (SQLException e) {
                throw new RuntimeException("findAll Candidat failed: " + e.getMessage(), e);
            }
            return list;
        }

        @Override
        public Optional<Candidat> findByEmail(String email) {
            String sql = "SELECT * FROM candidat WHERE email=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(map(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("findByEmail Candidat failed: " + e.getMessage(), e);
            }
            return Optional.empty();
        }

        @Override
        public List<Candidat> searchByName(String nameLike) {
            String sql = "SELECT * FROM candidat WHERE nom_complet LIKE ? ORDER BY nom_complet";
            List<Candidat> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, "%" + nameLike + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException("searchByName Candidat failed: " + e.getMessage(), e);
            }
            return list;
        }

        private Candidat map(ResultSet rs) throws SQLException {
            Candidat c = new Candidat();
            c.setId(rs.getInt("id"));
            c.setNomComplet(rs.getString("nom_complet"));
            c.setEmail(rs.getString("email"));
            c.setTelephone(rs.getString("telephone"));
            c.setLinkedinUrl(rs.getString("linkedin_url"));
            return c;
        }
    }

