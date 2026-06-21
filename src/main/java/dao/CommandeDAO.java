package dao;

import model.Commande;
import model.Ingredient;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `commande` table.
 *
 * Columns: id, id_cl, montant_tt, quantite, date_cmd, date_livraison
 * Note the column is `montant_tt`, while the Java field/getter is
 * `montant_total` / getMontantTotal() — just a naming difference,
 * mapped explicitly below.
 *
 * `quantite` here is the bottle size in ml for the WHOLE order (set
 * via QuantityController) — distinct from each Ingredient's own
 * quantite (grams of that specific ingredient in the recipe), which
 * lives in the separate ingredients_cmd junction table.
 *
 * elements_cmd (the Set<Ingredient>) is NOT stored on this table —
 * it lives in ingredients_cmd, so this DAO delegates to
 * IngredientsCmdDAO to load/save it alongside the order itself.
 */
public class CommandeDAO {

    private final IngredientsCmdDAO ingredientsCmdDAO = new IngredientsCmdDAO();

    public List<Commande> findAll() {
        List<Commande> result = new ArrayList<>();
        String sql = "SELECT id, id_cl, montant_tt, quantite, date_cmd, date_livraison FROM commande";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commandes", e);
        }

        return result;
    }

    /**
     * Loads every order placed by a given client, WITH each order's
     * ingredients populated — this is what OrderHistoryController
     * needs to show an accurate ingredient count/total per row.
     */
    public List<Commande> findByClientId(int idClient) {
        List<Commande> result = new ArrayList<>();
        String sql = "SELECT id, id_cl, montant_tt, quantite, date_cmd, date_livraison FROM commande WHERE id_cl = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClient);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Commande commande = mapRow(rs);
                    List<Ingredient> ingredients = ingredientsCmdDAO.findByCommandeId(commande.getID());
                    commande.setElementsCmd(new java.util.HashSet<>(ingredients));
                    result.add(commande);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commandes du client id=" + idClient, e);
        }

        return result;
    }

    /**
     * Loads a single order WITH its ingredients populated
     * (elements_cmd), by additionally querying ingredients_cmd.
     */
    public Commande findById(int id) {
        String sql = "SELECT id, id_cl, montant_tt, quantite, date_cmd, date_livraison FROM commande WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Commande commande = mapRow(rs);
                    List<Ingredient> ingredients = ingredientsCmdDAO.findByCommandeId(id);
                    commande.setElementsCmd(new java.util.HashSet<>(ingredients));
                    return commande;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la commande id=" + id, e);
        }

        return null;
    }

    /**
     * Inserts a new order row only (id_cl, montant_tt, quantite,
     * date_cmd, date_livraison) — it does NOT insert the order's
     * ingredients. Call insertWithIngredients(...) instead if you
     * also need to save the Set<Ingredient> in the same step, which
     * is what the checkout flow (LocationController) uses.
     */
    public int insert(Commande commande) {
        String sql = "INSERT INTO commande (id_cl, montant_tt, quantite, date_cmd, date_livraison) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, commande.getIdClient());
            stmt.setDouble(2, commande.getMontantTotal());
            stmt.setDouble(3, commande.getQuantite());
            stmt.setDate(4, commande.getDateCmd() != null ? Date.valueOf(commande.getDateCmd()) : null);
            stmt.setDate(5, commande.getDateLivraison() != null ? Date.valueOf(commande.getDateLivraison()) : null);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    commande.setID(generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de la commande", e);
        }

        return -1;
    }

    /**
     * Inserts the order row, then inserts one ingredients_cmd row per
     * ingredient in commande.getElementsCmd(). Each ingredient's own
     * Java class tells IngredientTypeResolver which type_ingredient
     * string ('fixateur', 'naturelle', 'synthetique', 'solvant') to
     * use, since Ingredient itself doesn't carry that label.
     */
    public int insertWithIngredients(Commande commande) {
        int idCmd = insert(commande);
        if (idCmd == -1) {
            return -1;
        }

        for (Ingredient ingredient : commande.getElementsCmd()) {
            String typeIngredient = IngredientTypeResolver.resolve(ingredient);
            ingredientsCmdDAO.insert(idCmd, ingredient.getID(), ingredient.getQuantite(), typeIngredient);
        }

        return idCmd;
    }

    public boolean update(Commande commande) {
        String sql = "UPDATE commande SET id_cl = ?, montant_tt = ?, quantite = ?, date_cmd = ?, date_livraison = ? WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, commande.getIdClient());
            stmt.setDouble(2, commande.getMontantTotal());
            stmt.setDouble(3, commande.getQuantite());
            stmt.setDate(4, commande.getDateCmd() != null ? Date.valueOf(commande.getDateCmd()) : null);
            stmt.setDate(5, commande.getDateLivraison() != null ? Date.valueOf(commande.getDateLivraison()) : null);
            stmt.setInt(6, commande.getID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la commande id=" + commande.getID(), e);
        }
    }

    public boolean delete(int id) {
        // Delete the junction rows first to respect the foreign key,
        // then the order itself.
        ingredientsCmdDAO.deleteByCommandeId(id);

        String sql = "DELETE FROM commande WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la commande id=" + id, e);
        }
    }

    private Commande mapRow(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setID(rs.getInt("id"));
        commande.setIdClient(rs.getInt("id_cl"));
        commande.setMontantTotal(rs.getDouble("montant_tt"));
        commande.setQuantite(rs.getDouble("quantite"));

        Date dateCmd = rs.getDate("date_cmd");
        if (dateCmd != null) {
            commande.setDateCmd(dateCmd.toLocalDate());
        }

        Date dateLivraison = rs.getDate("date_livraison");
        if (dateLivraison != null) {
            commande.setDateLivraison(dateLivraison.toLocalDate());
        }

        return commande;
    }
}
