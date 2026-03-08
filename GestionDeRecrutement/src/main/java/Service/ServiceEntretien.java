package Service;

import Entity.*;
import Utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceEntretien implements EntretienRepository{

     private final Connection cnx = DataSource.getInstance().getConnection();

        @Override
        public Integer create(Entretien e) {
            if (existsOverlapForCandidat(e.getCandidatId(), e.getStartDateTime(), e.getEndDateTime(), null))
                throw new IllegalStateException("Le candidat a déjà un entretien sur ce créneau.");
            if (e.getRecruteurTechniqueId() != null &&
                    existsOverlapForRecruteur(e.getRecruteurTechniqueId(), e.getStartDateTime(), e.getEndDateTime(), null))
                throw new IllegalStateException("Le recruteur technique est indisponible sur ce créneau.");
            if (e.getRecruteurRhId() != null &&
                    existsOverlapForRecruteur(e.getRecruteurRhId(), e.getStartDateTime(), e.getEndDateTime(), null))
                throw new IllegalStateException("Le recruteur RH est indisponible sur ce créneau.");
            if (e.getRecruteurManagerId() != null &&
                    existsOverlapForRecruteur(e.getRecruteurManagerId(), e.getStartDateTime(), e.getEndDateTime(), null))
                throw new IllegalStateException("Le recruteur Manager est indisponible sur ce créneau.");

            String sql = "INSERT INTO entretien(code, candidature_id, candidat_id, type, statut, date_entretien, heure_debut, heure_fin, duree_minutes, " +
                    "recruteur_tech_id, recruteur_rh_id, recruteur_manager_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, e.getCode());
                ps.setInt(2, e.getCandidatureId());
                ps.setInt(3, e.getCandidatId());
                ps.setString(4, e.getType().name());
                ps.setString(5, e.getStatut().name());
                ps.setDate(6, Date.valueOf(e.getDateEntretien()));
                ps.setTime(7, Time.valueOf(e.getHeureDebut()));
                ps.setTime(8, Time.valueOf(e.getHeureFin()));
                ps.setInt(9, e.getDureeMinutes());
                if (e.getRecruteurTechniqueId() == null) ps.setNull(10, Types.INTEGER); else ps.setInt(10, e.getRecruteurTechniqueId());
                if (e.getRecruteurRhId() == null) ps.setNull(11, Types.INTEGER); else ps.setInt(11, e.getRecruteurRhId());
                if (e.getRecruteurManagerId() == null) ps.setNull(12, Types.INTEGER); else ps.setInt(12, e.getRecruteurManagerId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        e.setId(id);
                        return id;
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException("create Entretien failed: " + ex.getMessage(), ex);
            }
            return null;
        }

        @Override
        public boolean update(Entretien e) {
            if (existsOverlapForCandidat(e.getCandidatId(), e.getStartDateTime(), e.getEndDateTime(), e.getId()))
                throw new IllegalStateException("Le candidat a déjà un entretien sur ce créneau.");
            if (e.getRecruteurTechniqueId() != null &&
                    existsOverlapForRecruteur(e.getRecruteurTechniqueId(), e.getStartDateTime(), e.getEndDateTime(), e.getId()))
                throw new IllegalStateException("Le recruteur technique est indisponible sur ce créneau.");
            // idem RH + Manager
            String sql = "UPDATE entretien SET code=?, candidature_id=?, candidat_id=?, type=?, statut=?, date_entretien=?, heure_debut=?, heure_fin=?, " +
                    "duree_minutes=?, recruteur_tech_id=?, recruteur_rh_id=?, recruteur_manager_id=? WHERE id=?";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setString(1, e.getCode());
                ps.setInt(2, e.getCandidatureId());
                ps.setInt(3, e.getCandidatId());
                ps.setString(4, e.getType().name());
                ps.setString(5, e.getStatut().name());
                ps.setDate(6, Date.valueOf(e.getDateEntretien()));
                ps.setTime(7, Time.valueOf(e.getHeureDebut()));
                ps.setTime(8, Time.valueOf(e.getHeureFin()));
                ps.setInt(9, e.getDureeMinutes());
                if (e.getRecruteurTechniqueId() == null) ps.setNull(10, Types.INTEGER); else ps.setInt(10, e.getRecruteurTechniqueId());
                if (e.getRecruteurRhId() == null) ps.setNull(11, Types.INTEGER); else ps.setInt(11, e.getRecruteurRhId());
                if (e.getRecruteurManagerId() == null) ps.setNull(12, Types.INTEGER); else ps.setInt(12, e.getRecruteurManagerId());
                ps.setInt(13, e.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException ex) { throw new RuntimeException("update Entretien failed: " + ex.getMessage(), ex); }
        }

        @Override
        public boolean deleteById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM entretien WHERE id=?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        @Override
        public Optional<Entretien> findById(Integer id) {
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM entretien WHERE id=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return Optional.empty();
        }

        @Override
        public List<Entretien> findAll() {
            List<Entretien> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM entretien ORDER BY date_entretien DESC, heure_debut DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public List<Entretien> findByCandidature(int candidatureId) {
            List<Entretien> list = new ArrayList<>();
            try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM entretien WHERE candidature_id=? ORDER BY date_entretien DESC, heure_debut")) {
                ps.setInt(1, candidatureId);
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public List<Entretien> findBetween(LocalDateTime from, LocalDateTime to) {
            List<Entretien> list = new ArrayList<>();
            String sql = "SELECT * FROM entretien WHERE TIMESTAMP(date_entretien, heure_debut) >= ? AND TIMESTAMP(date_entretien, heure_fin) <= ? ORDER BY date_entretien, heure_debut";
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setTimestamp(1, Timestamp.valueOf(from));
                ps.setTimestamp(2, Timestamp.valueOf(to));
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
            } catch (SQLException e) { throw new RuntimeException(e); }
            return list;
        }

        @Override
        public boolean existsOverlapForCandidat(int candidatId, LocalDateTime start, LocalDateTime end, Integer excludeEntretienId) {
            String sql = "SELECT COUNT(*) FROM entretien " +
                    "WHERE candidat_id=? " +
                    "AND TIMESTAMP(date_entretien, heure_debut) < ? " +
                    "AND TIMESTAMP(date_entretien, heure_fin) > ? " +
                    (excludeEntretienId != null ? "AND id<>?" : "");
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setInt(1, candidatId);
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setTimestamp(3, Timestamp.valueOf(start));
                if (excludeEntretienId != null) ps.setInt(4, excludeEntretienId);
                try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1) > 0; }
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        @Override
        public boolean existsOverlapForRecruteur(int recruteurId, LocalDateTime start, LocalDateTime end, Integer excludeEntretienId) {
            String sql = "SELECT COUNT(*) FROM entretien " +
                    "WHERE (recruteur_tech_id=? OR recruteur_rh_id=? OR recruteur_manager_id=?) " +
                    "AND TIMESTAMP(date_entretien, heure_debut) < ? " +
                    "AND TIMESTAMP(date_entretien, heure_fin) > ? " +
                    (excludeEntretienId != null ? "AND id<>?" : "");
            try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                ps.setInt(1, recruteurId);
                ps.setInt(2, recruteurId);
                ps.setInt(3, recruteurId);
                ps.setTimestamp(4, Timestamp.valueOf(end));
                ps.setTimestamp(5, Timestamp.valueOf(start));
                if (excludeEntretienId != null) ps.setInt(6, excludeEntretienId);
                try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1) > 0; }
            } catch (SQLException e) { throw new RuntimeException(e); }
        }

        private Entretien map(ResultSet rs) throws SQLException {
            Entretien e = new Entretien();
            e.setId(rs.getInt("id"));
            e.setCode(rs.getString("code"));
            e.setCandidatureId(rs.getInt("candidature_id"));
            e.setCandidatId(rs.getInt("candidat_id"));
            e.setType(TypeEntretien.valueOf(rs.getString("type")));
            e.setStatut(StatutEntretien.valueOf(rs.getString("statut")));
            e.setDateEntretien(rs.getDate("date_entretien").toLocalDate());
            e.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
            e.setHeureFin(rs.getTime("heure_fin").toLocalTime());
            e.setDureeMinutes(rs.getInt("duree_minutes"));
            int tech = rs.getInt("recruteur_tech_id"); e.setRecruteurTechniqueId(rs.wasNull()? null : tech);
            int rh = rs.getInt("recruteur_rh_id"); e.setRecruteurRhId(rs.wasNull()? null : rh);
            int man = rs.getInt("recruteur_manager_id"); e.setRecruteurManagerId(rs.wasNull()? null : man);
            return e;
        }
    }

