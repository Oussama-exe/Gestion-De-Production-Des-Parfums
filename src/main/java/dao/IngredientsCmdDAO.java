package dao;

import model.Ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `ingredients_cmd` junction table.
 *
 * Columns: id_cmd, id_ing, quantite, type_ingredient
 * type_ingredient is one of: 'fixateur', 'naturelle', 'synthetique', 'solvant'
 *
 * This table is a polymorphic association: id_ing points to a row in
 * a DIFFERENT table depending on type_ingredient. To load the actual
 * Ingredient object for a given row, this DAO delegates to the
 * matching *DAO (FixateurDAO, MatierePremiereNaturelleDAO, etc.)
 * based on type_ingredient, then sets the real per-order quantite on
 * the result (overriding the 0.0 placeholder those DAOs return when
 * loading from their own catalog tables).
 */
public class IngredientsCmdDAO {

    private final FixateurDAO fixateurDAO = new FixateurDAO();
    private final MatierePremiereNaturelleDAO matierePremiereNaturelleDAO = new MatierePremiereNaturelleDAO();
    private final MatiereSynthetiqueDAO matiereSynthetiqueDAO = new MatiereSynthetiqueDAO();
    private final SolvantSupportDAO solvantSupportDAO = new SolvantSupportDAO();

    /**
     * Loads every ingredient line attached to a given order id,
     * resolving each one to its real Ingredient subclass with the
     * correct per-order quantite already applied.
     */
    public List<Ingredient> findByCommandeId(int idCmd) {
        List<Ingredient> result = new ArrayList<>();
        String sql = "SELECT id_ing, quantite, type_ingredient FROM ingredients_cmd WHERE id_cmd = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCmd);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idIng = rs.getInt("id_ing");
                    double quantite = rs.getDouble("quantite");
                    String typeIngredient = rs.getString("type_ingredient");

                    Ingredient ingredient = resolveIngredient(idIng, typeIngredient);
                    if (ingredient != null) {
                        ingredient.setQuantite(quantite);
                        result.add(ingredient);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients de la commande id=" + idCmd, e);
        }

        return result;
    }

    /**
     * Inserts one ingredient line for an order. typeIngredient must be
     * one of "fixateur", "naturelle", "synthetique", "solvant" — pass
     * the right one based on which concrete Ingredient subclass you're
     * saving (see Commande.addIngredient / CommandeDAO.save once that's
     * wired up).
     */
    public boolean insert(int idCmd, int idIng, double quantite, String typeIngredient) {
        String sql = "INSERT INTO ingredients_cmd (id_cmd, id_ing, quantite, type_ingredient) VALUES (?, ?, ?, ?)";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCmd);
            stmt.setInt(2, idIng);
            stmt.setDouble(3, quantite);
            stmt.setString(4, typeIngredient);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de l'ingrédient dans la commande id=" + idCmd, e);
        }
    }

    public boolean deleteByCommandeId(int idCmd) {
        String sql = "DELETE FROM ingredients_cmd WHERE id_cmd = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCmd);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression des ingrédients de la commande id=" + idCmd, e);
        }
    }

    /**
     * Resolves an (id_ing, type_ingredient) pair to the actual
     * Ingredient subclass instance, by delegating to the matching DAO.
     */
    private Ingredient resolveIngredient(int idIng, String typeIngredient) {
        switch (typeIngredient) {
            case "fixateur":
                return fixateurDAO.findById(idIng);
            case "naturelle":
                return matierePremiereNaturelleDAO.findById(idIng);
            case "synthetique":
                return matiereSynthetiqueDAO.findById(idIng);
            case "solvant":
                return solvantSupportDAO.findById(idIng);
            default:
                throw new IllegalArgumentException("type_ingredient inconnu: " + typeIngredient);
        }
    }
}
