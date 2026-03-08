package Service;

import Entity.*;
import Utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ServiceCandidature implements CandidatureRepository {

    private final Connection cnx = DataSource.getInstance().getConnection();

        @Override
        public Integer create(Candidature c) {
            String sql = "INSERT INTO candidature(code, candidat_id, poste_id, date_candidature, phase, resultat_screening, decision_finale, date_decision) " +
                    "VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getCode());
                ps.setInt(2, c.getCandidatId());
                ps.setInt(3, c.getPosteId());
                ps.setDate(4, Date.valueOf(c.getDateCandidature()));
                ps.setString(5, c.getPhase().name());
                ps.setString(6, c.getResultatScreening() == null ? null : c.getResultatScreening().name());
                ps.setString(7, c.getDecisionFinale() == null ? null : c.getDecisionFinale().name());
                ps.setObject(8, c.getDateDecision() == null ? null : Date.valueOf(c.getDateDecision()));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        c.setId(id);
                        return id;
                    }
                }
            } catch (SQLException e) { throw new RuntimeException("create Candidature failed: " + e.getMessage(), e); }
            return null;
        }

        @Override
        public boolean update(Candidature c) {
            String sql = "UPDATE candidature SET code=?, candidat_id=?, poste_id=?, date_candidature=?, phase=?, " +
                    "resultat_screening=?, decision_finale=?, date_decision=? WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, c.getCode());
                ps.setInt(2, c.getCandidatId());
                ps.setInt(3, c.getPosteId());
                ps.setDate(4, Date.valueOf(c.getDateCandidature()));
                ps.setString(5, c.getPhase().name());
                ps.setString(6, c.getResultatScreening() == null ? null : c.getResultatScreening().name());
                ps.setString(7, c.getDecisionFinale() == null ? null : c.getDecisionFinale().name());
                ps.setObject(8, c.getDateDecision() == null ? null : Date.valueOf(c.getDateDecision()));
                ps.setInt(9, c.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException("update Candidature failed: " + e.getMessage(), e); }
        }

        @Override
        public boolean deleteById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM candidature WHERE id=?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        @Override
        public Optional<Candidature> findById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM candidature WHERE id=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return Optional.empty();
        }

        @Override
        public List<Candidature> findAll() {
            List<Candidature> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM candidature ORDER BY id DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public List<Candidature> findByPhase(PhaseRecrutement phase) {
            List<Candidature> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM candidature WHERE phase=? ORDER BY id DESC")) {
                ps.setString(1, phase.name());
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public List<Candidature> findByCandidat(int candidatId) {
            List<Candidature> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM candidature WHERE candidat_id=? ORDER BY id DESC")) {
                ps.setInt(1, candidatId);
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public boolean updatePhase(int candidatureId, PhaseRecrutement nouvellePhase) {
            try (PreparedStatement ps = cnx.prepareStatement("UPDATE candidature SET phase=? WHERE id=?")) {
                ps.setString(1, nouvellePhase.name());
                ps.setInt(2, candidatureId);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        @Override
        public boolean setDecisionFinale(int candidatureId, DecisionFinale decision, LocalDate dateDecision) {
            try (PreparedStatement ps = cnx.prepareStatement("UPDATE candidature SET decision_finale=?, date_decision=? WHERE id=?")) {
                ps.setString(1, decision.name());
                ps.setDate(2, dateDecision == null ? null : Date.valueOf(dateDecision));
                ps.setInt(3, candidatureId);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        private Candidature map(ResultSet rs) throws SQLException {
            Candidature c = new Candidature();
            c.setId(rs.getInt("id"));
            c.setCode(rs.getString("code"));
            c.setCandidatId(rs.getInt("candidat_id"));
            c.setPosteId(rs.getInt("poste_id"));
            c.setDateCandidature(rs.getDate("date_candidature").toLocalDate());
            c.setPhase(PhaseRecrutement.valueOf(rs.getString("phase")));
            String res = rs.getString("resultat_screening");
            c.setResultatScreening(res == null ? null : ResultatScreening.valueOf(res));
            String dec = rs.getString("decision_finale");
            c.setDecisionFinale(dec == null ? null : DecisionFinale.valueOf(dec));
            Date dd = rs.getDate("date_decision");
            c.setDateDecision(dd == null ? null : dd.toLocalDate());
            return c;
        }
    }
