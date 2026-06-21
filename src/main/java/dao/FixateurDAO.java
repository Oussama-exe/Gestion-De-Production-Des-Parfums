package dao;

import model.FIX_enum;
import model.Fixateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `fixateurs` table.
 *
 * Columns: id, nom, type, disponibilite, prix_par_gramme
 * The `prix_par_gramme` column exists in the DB but is NOT read back
 * into the Java object — Fixateur.calculer_prix() relies on
 * FIX_enum.getPrixParGramme() instead (the enum is the source of
 * truth for pricing, by design). The column is left untouched here
 * in case you want to use it later.
 */
public class FixateurDAO {

    public List<Fixateur> findAll() {
        List<Fixateur> result = new ArrayList<>();
        String sql = "SELECT id, nom, type, disponibilite FROM fixateurs";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des fixateurs", e);
        }

        return result;
    }

    public Fixateur findById(int id) {
        String sql = "SELECT id, nom, type, disponibilite FROM fixateurs WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du fixateur id=" + id, e);
        }

        return null;
    }

    public int insert(Fixateur fixateur) {
        String sql = "INSERT INTO fixateurs (nom, type, disponibilite) VALUES (?, ?, ?)";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, fixateur.getNom());
            stmt.setString(2, fixateur.getType().name());
            stmt.setBoolean(3, fixateur.getDisponibilite());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    fixateur.setID(generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du fixateur", e);
        }

        return -1;
    }

    public boolean update(Fixateur fixateur) {
        String sql = "UPDATE fixateurs SET nom = ?, type = ?, disponibilite = ? WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fixateur.getNom());
            stmt.setString(2, fixateur.getType().name());
            stmt.setBoolean(3, fixateur.getDisponibilite());
            stmt.setInt(4, fixateur.getID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du fixateur id=" + fixateur.getID(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM fixateurs WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du fixateur id=" + id, e);
        }
    }

    /**
     * Maps the current row of the ResultSet to a Fixateur.
     * Quantite isn't stored on this table (it's per-order, in
     * ingredients_cmd.quantite) — set to 0 here as a neutral default.
     */
    private Fixateur mapRow(ResultSet rs) throws SQLException {
        Fixateur fixateur = new Fixateur(
                rs.getString("nom"),
                0.0,
                rs.getBoolean("disponibilite"),
                FIX_enum.valueOf(rs.getString("type"))
        );
        fixateur.setID(rs.getInt("id"));
        return fixateur;
    }
}
