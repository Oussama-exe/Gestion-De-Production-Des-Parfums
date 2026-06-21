package dao;

import model.MPN_enum;
import model.Matiere_Premiere_naturelle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `matiere_prem_naturelle` table.
 *
 * Columns: id, nom, type, disponibilite, prix_par_gramme
 * Same note as FixateurDAO: prix_par_gramme is NOT read back —
 * Matiere_Premiere_naturelle.calculer_prix() relies on
 * MPN_enum.getPrixParGramme() instead.
 */
public class MatierePremiereNaturelleDAO {

    public List<Matiere_Premiere_naturelle> findAll() {
        List<Matiere_Premiere_naturelle> result = new ArrayList<>();
        String sql = "SELECT id, nom, type, disponibilite FROM matiere_prem_naturelle";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des matières premières naturelles", e);
        }

        return result;
    }

    public Matiere_Premiere_naturelle findById(int id) {
        String sql = "SELECT id, nom, type, disponibilite FROM matiere_prem_naturelle WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la matière première naturelle id=" + id, e);
        }

        return null;
    }

    public int insert(Matiere_Premiere_naturelle matiere) {
        String sql = "INSERT INTO matiere_prem_naturelle (nom, type, disponibilite) VALUES (?, ?, ?)";

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
            throw new RuntimeException("Erreur lors de l'insertion de la matière première naturelle", e);
        }

        return -1;
    }

    public boolean update(Matiere_Premiere_naturelle matiere) {
        String sql = "UPDATE matiere_prem_naturelle SET nom = ?, type = ?, disponibilite = ? WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matiere.getNom());
            stmt.setString(2, matiere.getType().name());
            stmt.setBoolean(3, matiere.getDisponibilite());
            stmt.setInt(4, matiere.getID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la matière première naturelle id=" + matiere.getID(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM matiere_prem_naturelle WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la matière première naturelle id=" + id, e);
        }
    }

    /**
     * Maps the current row of the ResultSet to a Matiere_Premiere_naturelle.
     * Quantite isn't stored on this table (it's per-order, in
     * ingredients_cmd.quantite) — set to 0 here as a neutral default.
     */
    private Matiere_Premiere_naturelle mapRow(ResultSet rs) throws SQLException {
        Matiere_Premiere_naturelle matiere = new Matiere_Premiere_naturelle(
                rs.getString("nom"),
                0.0,
                rs.getBoolean("disponibilite"),
                MPN_enum.valueOf(rs.getString("type"))
        );
        matiere.setID(rs.getInt("id"));
        return matiere;
    }
}
