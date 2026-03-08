package Service;

import Entity.CompetencePoste;
import Entity.Niveau;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceCompetencePoste implements CompetencePosteRepository{

     private final Connection cnx = DataSource.getInstance().getConnection();

        @Override
        public Integer create(CompetencePoste cp) {
            String sql = "INSERT INTO competence_poste(competence_id, poste_id, niveau) VALUES (?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, cp.getCompetenceId());
                ps.setInt(2, cp.getPosteId());
                ps.setString(3, cp.getNiveau().name());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        cp.setId(id);
                        return id;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("create CompetencePoste failed: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        public boolean update(CompetencePoste cp) {
            String sql = "UPDATE competence_poste SET competence_id=?, poste_id=?, niveau=? WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setInt(1, cp.getCompetenceId());
                ps.setInt(2, cp.getPosteId());
                ps.setString(3, cp.getNiveau().name());
                ps.setInt(4, cp.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        @Override
        public boolean deleteById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM competence_poste WHERE id=?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        @Override
        public Optional<CompetencePoste> findById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM competence_poste WHERE id=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return Optional.empty();
        }

        @Override
        public List<CompetencePoste> findAll() {
            List<CompetencePoste> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM competence_poste ORDER BY id DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public List<CompetencePoste> findByPoste(int posteId) {
            List<CompetencePoste> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM competence_poste WHERE poste_id=?")) {
                ps.setInt(1, posteId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public List<CompetencePoste> findByCompetence(int competenceId) {
            List<CompetencePoste> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM competence_poste WHERE competence_id=?")) {
                ps.setInt(1, competenceId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(map(rs));
                }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        private CompetencePoste map(ResultSet rs) throws SQLException {
            CompetencePoste cp = new CompetencePoste();
            cp.setId(rs.getInt("id"));
            cp.setCompetenceId(rs.getInt("competence_id"));
            cp.setPosteId(rs.getInt("poste_id"));
            cp.setNiveau(Niveau.valueOf(rs.getString("niveau")));
            return cp;
        }
    }


