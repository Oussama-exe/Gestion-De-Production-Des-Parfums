package dao;

import model.MS_enum;
import model.Matiere_synthetique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `matiere_synthetique` table.
 *
 * Columns: id, nom, type, disponibilite, prix_par_gramme
 * Same note as FixateurDAO: prix_par_gramme is NOT read back —
 * Matiere_synthetique.calculer_prix() relies on
 * MS_enum.getPrixParGramme() instead.
 */
public class MatiereSynthetiqueDAO {

    public List<Matiere_synthetique> findAll() {
        List<Matiere_synthetique> result = new ArrayList<>();
        String sql = "SELECT id, nom, type, disponibilite FROM matiere_synthetique";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des matières synthétiques", e);
        }

        return result;
    }

    public Matiere_synthetique findById(int id) {
        String sql = "SELECT id, nom, type, disponibilite FROM matiere_synthetique WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la matière synthétique id=" + id, e);
        }

        return null;
    }

    public int insert(Matiere_synthetique matiere) {
        String sql = "INSERT INTO matiere_synthetique (nom, type, disponibilite) VALUES (?, ?, ?)";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, matiere.getNom());
            stmt.setString(2, matiere.getType().name());
            stmt.setBoolean(3, matiere.getDisponibilite());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    matiere.setID(generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de la matière synthétique", e);
        }

        return -1;
    }

    public boolean update(Matiere_synthetique matiere) {
        String sql = "UPDATE matiere_synthetique SET nom = ?, type = ?, disponibilite = ? WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matiere.getNom());
            stmt.setString(2, matiere.getType().name());
            stmt.setBoolean(3, matiere.getDisponibilite());
            stmt.setInt(4, matiere.getID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la matière synthétique id=" + matiere.getID(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM matiere_synthetique WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la matière synthétique id=" + id, e);
        }
    }

    /**
     * Maps the current row of the ResultSet to a Matiere_synthetique.
     * Quantite isn't stored on this table (it's per-order, in
     * ingredients_cmd.quantite) — set to 0 here as a neutral default.
     */
    private Matiere_synthetique mapRow(ResultSet rs) throws SQLException {
        Matiere_synthetique matiere = new Matiere_synthetique(
                rs.getString("nom"),
                0.0,
                rs.getBoolean("disponibilite"),
                MS_enum.valueOf(rs.getString("type"))
        );
        matiere.setID(rs.getInt("id"));
        return matiere;
    }
}
