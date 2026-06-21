package controller;

import dao.CommandeDAO;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;
import model.Commande;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for OrderHistoryView.fxml.
 *
 * Loads every order for the currently logged-in client via
 * CommandeDAO.findByClientId, wraps each in a CommandeRow, and
 * displays them in the TableView.
 */
public class OrderHistoryController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button backButton;

    @FXML
    private TableView<CommandeRow> ordersTable;

    @FXML
    private TableColumn<CommandeRow, String> colOrderId;

    @FXML
    private TableColumn<CommandeRow, String> colDate;

    @FXML
    private TableColumn<CommandeRow, String> colParfum;

    @FXML
    private TableColumn<CommandeRow, String> colQuantite;

    @FXML
    private TableColumn<CommandeRow, String> colTotal;

    @FXML
    private TableColumn<CommandeRow, String> colStatut;

    private final CommandeDAO commandeDAO = new CommandeDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colParfum.setCellValueFactory(new PropertyValueFactory<>("parfum"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        loadOrders();

        backButton.setOnAction(e -> SceneManager.switchTo("/view/fxml/HomeView.fxml"));
    }

    private void loadOrders() {
        if (Session.getCurrentClient() == null) {
            ordersTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Commande> commandes = commandeDAO.findByClientId(Session.getCurrentClient().getID());
        List<CommandeRow> rows = commandes.stream().map(CommandeRow::new).toList();
        ordersTable.setItems(FXCollections.observableArrayList(rows));
    }
}
