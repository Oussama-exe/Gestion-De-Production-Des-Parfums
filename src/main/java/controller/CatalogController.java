package controller;

import dao.FixateurDAO;
import dao.MatierePremiereNaturelleDAO;
import dao.MatiereSynthetiqueDAO;
import dao.SolvantSupportDAO;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;
import model.Ingredient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for CatalogView.fxml.
 *
 * Loads every ingredient from the 4 DAOs (Fixateur, Matiere première
 * naturelle, Matiere synthétique, Solvant/Support), renders them as
 * product cards grouped by family, and supports filtering by family
 * (category chips) and free-text search on the name.
 */
public class CatalogController implements Initializable {

    private static final int CARDS_PER_ROW = 4;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private Button cartButton;

    @FXML
    private Button catAllButton;

    @FXML
    private Button catNaturelleButton;

    @FXML
    private Button catSynthetiqueButton;

    @FXML
    private Button catFixateurButton;

    @FXML
    private Button catSolvantButton;

    @FXML
    private VBox productGridContainer;

    private final FixateurDAO fixateurDAO = new FixateurDAO();
    private final MatierePremiereNaturelleDAO matierePremiereNaturelleDAO = new MatierePremiereNaturelleDAO();
    private final MatiereSynthetiqueDAO matiereSynthetiqueDAO = new MatiereSynthetiqueDAO();
    private final SolvantSupportDAO solvantSupportDAO = new SolvantSupportDAO();

    private List<Ingredient> allIngredients = new ArrayList<>();
    private String activeFamilyFilter = "ALL"; // ALL | NATURELLE | SYNTHETIQUE | FIXATEUR | SOLVANT

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllIngredients();

        catAllButton.setOnAction(e -> setActiveFilter("ALL", catAllButton));
        catNaturelleButton.setOnAction(e -> setActiveFilter("NATURELLE", catNaturelleButton));
        catSynthetiqueButton.setOnAction(e -> setActiveFilter("SYNTHETIQUE", catSynthetiqueButton));
        catFixateurButton.setOnAction(e -> setActiveFilter("FIXATEUR", catFixateurButton));
        catSolvantButton.setOnAction(e -> setActiveFilter("SOLVANT", catSolvantButton));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> renderProducts());

        cartButton.setOnAction(e -> SceneManager.switchTo("/view/fxml/CartView.fxml"));

        renderProducts();
    }

    private void loadAllIngredients() {
        allIngredients = new ArrayList<>();
        allIngredients.addAll(matierePremiereNaturelleDAO.findAll());
        allIngredients.addAll(matiereSynthetiqueDAO.findAll());
        allIngredients.addAll(fixateurDAO.findAll());
        allIngredients.addAll(solvantSupportDAO.findAll());
    }

    private void setActiveFilter(String family, Button activeButton) {
        activeFamilyFilter = family;

        for (Button b : List.of(catAllButton, catNaturelleButton, catSynthetiqueButton, catFixateurButton, catSolvantButton)) {
            b.getStyleClass().setAll(b == activeButton ? "category-chip-active" : "category-chip");
        }

        renderProducts();
    }

    /**
     * Rebuilds productGridContainer from scratch based on the current
     * search text + active family filter. Simple and not the most
     * efficient approach for a huge catalog, but perfectly fine at
     * this scale and much easier to reason about than incremental
     * diffing.
     */
    private void renderProducts() {
        productGridContainer.getChildren().clear();

        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

        List<Ingredient> filtered = allIngredients.stream()
                .filter(this::matchesFamilyFilter)
                .filter(ing -> query.isEmpty() || ing.getNom().toLowerCase().contains(query))
                .toList();

        if (filtered.isEmpty()) {
            Text empty = new Text("Aucun ingrédient ne correspond à votre recherche.");
            empty.getStyleClass().add("caption-text");
            productGridContainer.getChildren().add(empty);
            return;
        }

        GridPane grid = new GridPane();
        grid.setHgap(22.0);
        grid.setVgap(22.0);
        for (int i = 0; i < CARDS_PER_ROW; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / CARDS_PER_ROW);
            grid.getColumnConstraints().add(cc);
        }

        int row = 0;
        int col = 0;
        for (Ingredient ingredient : filtered) {
            VBox card = IngredientCardFactory.buildProductCard(ingredient, this::openProductDetail);
            grid.add(card, col, row);
            col++;
            if (col == CARDS_PER_ROW) {
                col = 0;
                row++;
            }
        }

        VBox.setMargin(grid, new Insets(16.0, 0, 0, 0));
        productGridContainer.getChildren().add(grid);
    }

    private boolean matchesFamilyFilter(Ingredient ingredient) {
        return switch (activeFamilyFilter) {
            case "NATURELLE" -> ingredient instanceof model.Matiere_Premiere_naturelle;
            case "SYNTHETIQUE" -> ingredient instanceof model.Matiere_synthetique;
            case "FIXATEUR" -> ingredient instanceof model.Fixateur;
            case "SOLVANT" -> ingredient instanceof model.Solvant_Support;
            default -> true;
        };
    }

    private void openProductDetail(Ingredient ingredient) {
        Session.setSelectedIngredient(ingredient);
        SceneManager.switchTo("/view/fxml/ProductDetailView.fxml");
    }
}
