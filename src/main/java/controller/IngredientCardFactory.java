package controller;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Fixateur;
import model.Ingredient;
import model.Matiere_Premiere_naturelle;
import model.Matiere_synthetique;
import model.Solvant_Support;

/**
 * Builds the small reusable UI pieces shared by CatalogController
 * (product grid cards) and CartController / CompositionSummaryController
 * (cart/summary rows), since all three need to turn an Ingredient into
 * a visual node and none of that markup exists statically in the FXML
 * anymore.
 */
final class IngredientCardFactory {

    private IngredientCardFactory() {
    }

    /** A reasonable single emoji per ingredient family, purely decorative. */
    static String emojiFor(Ingredient ingredient) {
        if (ingredient instanceof Fixateur) {
            return "🧪";
        }
        if (ingredient instanceof Matiere_Premiere_naturelle) {
            return "🌸";
        }
        if (ingredient instanceof Matiere_synthetique) {
            return "✨";
        }
        if (ingredient instanceof Solvant_Support) {
            return "💧";
        }
        return "🌿";
    }

    /** Human-readable family label, e.g. for the product-detail "category" line. */
    static String familyLabelFor(Ingredient ingredient) {
        if (ingredient instanceof Fixateur) {
            return "Fixateur";
        }
        if (ingredient instanceof Matiere_Premiere_naturelle) {
            return "Matière première naturelle";
        }
        if (ingredient instanceof Matiere_synthetique) {
            return "Matière synthétique";
        }
        if (ingredient instanceof Solvant_Support) {
            return "Solvant / Support";
        }
        return "Ingrédient";
    }

    /** The enum constant's name as a friendly "type" label (e.g. "FLORAL" -> "Floral"). */
    static String typeLabelFor(Ingredient ingredient) {
        String raw;
        if (ingredient instanceof Fixateur f) {
            raw = f.getType().name();
        } else if (ingredient instanceof Matiere_Premiere_naturelle m) {
            raw = m.getType().name();
        } else if (ingredient instanceof Matiere_synthetique m) {
            raw = m.getType().name();
        } else if (ingredient instanceof Solvant_Support s) {
            raw = s.getType().name();
        } else {
            return "";
        }
        String lower = raw.toLowerCase().replace('_', ' ');
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }

    /**
     * Builds one product card for the Catalog grid. onOpenDetail is
     * called with the ingredient when the card is clicked.
     */
    static VBox buildProductCard(Ingredient ingredient, java.util.function.Consumer<Ingredient> onOpenDetail) {
        VBox card = new VBox();
        card.getStyleClass().add("product-card");

        StackPane imageFrame = new StackPane();
        imageFrame.getStyleClass().add("product-image-frame");
        imageFrame.setPrefHeight(150.0);
        Text emoji = new Text(emojiFor(ingredient));
        emoji.setStyle("-fx-font-size: 40px;");
        imageFrame.getChildren().add(emoji);

        VBox info = new VBox(6.0);
        info.setPadding(new Insets(14.0));

        Text name = new Text(ingredient.getNom());
        name.getStyleClass().add("product-name");

        Text type = new Text(typeLabelFor(ingredient));
        type.getStyleClass().add("caption-text");

        Text availability = new Text(ingredient.getDisponibilite() ? "Disponible" : "Indisponible");
        availability.getStyleClass().add(ingredient.getDisponibilite() ? "caption-text" : "error-text");

        info.getChildren().addAll(name, type, availability);
        card.getChildren().addAll(imageFrame, info);

        card.setOnMouseClicked(e -> onOpenDetail.accept(ingredient));

        return card;
    }

    /**
     * Builds one cart row (used by CartView) with a remove button.
     * onRemove is called with the ingredient when "✕" is clicked.
     */
    static HBox buildCartRow(Ingredient ingredient, java.util.function.Consumer<Ingredient> onRemove) {
        HBox row = new HBox(14.0);
        row.getStyleClass().add("cart-row");
        row.setPadding(new Insets(14.0, 16.0, 14.0, 16.0));
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        StackPane imageFrame = new StackPane();
        imageFrame.getStyleClass().add("product-image-frame");
        imageFrame.setPrefSize(52.0, 52.0);
        imageFrame.setMaxSize(52.0, 52.0);
        Text emoji = new Text(emojiFor(ingredient));
        emoji.setStyle("-fx-font-size: 20px;");
        imageFrame.getChildren().add(emoji);

        VBox texts = new VBox(3.0);
        Text name = new Text(ingredient.getNom());
        name.getStyleClass().add("cart-row-name");
        Text meta = new Text(typeLabelFor(ingredient) + " · " + ingredient.getQuantite() + " g");
        meta.getStyleClass().add("cart-row-meta");
        texts.getChildren().addAll(name, meta);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text price = new Text(String.format("%.2f€", ingredient.calculer_prix()));
        price.getStyleClass().add("product-price");

        Button remove = new Button("✕");
        remove.getStyleClass().add("btn-danger-mini");
        remove.setOnAction(e -> onRemove.accept(ingredient));

        row.getChildren().addAll(imageFrame, texts, spacer, price, remove);

        return row;
    }

    /**
     * Builds one row for the CompositionSummary list — simpler than the
     * cart row (no price shown there, just name/family + a remove button).
     */
    static HBox buildSummaryRow(Ingredient ingredient, java.util.function.Consumer<Ingredient> onRemove) {
        HBox row = new HBox(14.0);
        row.getStyleClass().add("cart-row");
        row.setPadding(new Insets(13.0, 16.0, 13.0, 16.0));
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text emoji = new Text(emojiFor(ingredient));
        emoji.setStyle("-fx-font-size: 18px;");

        VBox texts = new VBox(2.0);
        Text name = new Text(ingredient.getNom());
        name.getStyleClass().add("cart-row-name");
        Text meta = new Text(familyLabelFor(ingredient));
        meta.getStyleClass().add("cart-row-meta");
        texts.getChildren().addAll(name, meta);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region qtyTrack = new Region();
        qtyTrack.getStyleClass().add("qty-dot-track");
        qtyTrack.setPrefSize(60.0, 6.0);

        Button remove = new Button("✕");
        remove.getStyleClass().add("btn-danger-mini");
        remove.setOnAction(e -> onRemove.accept(ingredient));

        row.getChildren().addAll(emoji, texts, spacer, qtyTrack, remove);

        return row;
    }
}
