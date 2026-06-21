package dao;

import model.SOLV_SUPP_enum;
import model.Solvant_Support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `solvants_supports` table.
 *
 * Columns: id, nom, type, disponibilite, prix_par_gramme
 * Same note as FixateurDAO: prix_par_gramme is NOT read back —
 * Solvant_Support.calculer_prix() relies on
 * SOLV_SUPP_enum.getPrixParGramme() instead.
 */
public class SolvantSupportDAO {

    public List<Solvant_Support> findAll() {
        List<Solvant_Support> result = new ArrayList<>();
        String sql = "SELECT id, nom, type, disponibilite FROM solvants_supports";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des solvants/supports", e);
        }

        return result;
    }

    public Solvant_Support findById(int id) {
        String sql = "SELECT id, nom, type, disponibilite FROM solvants_supports WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du solvant/support id=" + id, e);
        }

        return null;
    }

    public int insert(Solvant_Support solvant) {
        String sql = "INSERT INTO solvants_supports (nom, type, disponibilite) VALUES (?, ?, ?)";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, solvant.getNom());
            stmt.setString(2, solvant.getType().name());
            stmt.setBoolean(3, solvant.getDisponibilite());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    solvant.setID(generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du solvant/support", e);
        }

        return -1;
    }

    public boolean update(Solvant_Support solvant) {
        String sql = "UPDATE solvants_supports SET nom = ?, type = ?, disponibilite = ? WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, solvant.getNom());
            stmt.setString(2, solvant.getType().name());
            stmt.setBoolean(3, solvant.getDisponibilite());
            stmt.setInt(4, solvant.getID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du solvant/support id=" + solvant.getID(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM solvants_supports WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du solvant/support id=" + id, e);
        }
    }

    /**
     * Maps the current row of the ResultSet to a Solvant_Support.
     * Quantite isn't stored on this table (it's per-order, in
     * ingredients_cmd.quantite) — set to 0 here as a neutral default.
     */
    private Solvant_Support mapRow(ResultSet rs) throws SQLException {
        Solvant_Support solvant = new Solvant_Support(
                rs.getString("nom"),
                0.0,
                rs.getBoolean("disponibilite"),
                SOLV_SUPP_enum.valueOf(rs.getString("type"))
        );
        solvant.setID(rs.getInt("id"));
        return solvant;
    }
}
