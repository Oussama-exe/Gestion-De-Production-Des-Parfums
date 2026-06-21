package dao;

import model.Fixateur;
import model.Ingredient;
import model.Matiere_Premiere_naturelle;
import model.Matiere_synthetique;
import model.Solvant_Support;

/**
 * Bridges an Ingredient subclass instance to the type_ingredient
 * string stored in the ingredients_cmd table ('fixateur', 'naturelle',
 * 'synthetique', 'solvant').
 *
 * This exists because Ingredient.java itself doesn't carry that label
 * — it's derived purely from the Java class of the object. Used by
 * CommandeDAO.insertWithIngredients() when saving an order's
 * Set<Ingredient> to the database.
 */
final class IngredientTypeResolver {

    private IngredientTypeResolver() {
    }

    static String resolve(Ingredient ingredient) {
        if (ingredient instanceof Fixateur) {
            return "fixateur";
        }
        if (ingredient instanceof Matiere_Premiere_naturelle) {
            return "naturelle";
        }
        if (ingredient instanceof Matiere_synthetique) {
            return "synthetique";
        }
        if (ingredient instanceof Solvant_Support) {
            return "solvant";
        }
        throw new IllegalArgumentException(
                "Type d'ingrédient inconnu pour la classe: " + ingredient.getClass().getName());
    }
}
