package dao;

import model.Client;
import model.Paiement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `client` table.
 *
 * Columns: id, nom, prenom, adresse, email, pwd, paiement
 */
public class ClientDAO {

    public List<Client> findAll() {
        List<Client> result = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, adresse, email, pwd, paiement FROM client";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des clients", e);
        }

        return result;
    }

    public Client findById(int id) {
        String sql = "SELECT id, nom, prenom, adresse, email, pwd, paiement FROM client WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du client id=" + id, e);
        }

        return null;
    }

    /**
     * Used by LoginController: looks up a client by email, to be
     * paired with a password check (see PasswordVerifier note in
     * LoginController once we wire it up — never compare plain-text
     * passwords without at least discussing hashing first).
     */
    public Client findByEmail(String email) {
        String sql = "SELECT id, nom, prenom, adresse, email, pwd, paiement FROM client WHERE email = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du client email=" + email, e);
        }

        return null;
    }

    public int insert(Client client) {
        String sql = "INSERT INTO client (nom, prenom, adresse, email, pwd, paiement) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getAdresse());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getPwd());
            stmt.setString(6, client.getPaiement() != null ? client.getPaiement().name() : null);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    client.setID(generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du client", e);
        }

        return -1;
    }

    public boolean update(Client client) {
        String sql = "UPDATE client SET nom = ?, prenom = ?, adresse = ?, email = ?, pwd = ?, paiement = ? WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getAdresse());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getPwd());
            stmt.setString(6, client.getPaiement() != null ? client.getPaiement().name() : null);
            stmt.setInt(7, client.getID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du client id=" + client.getID(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM client WHERE id = ?";

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du client id=" + id, e);
        }
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client client = new Client(
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("pwd")
        );
        client.setAdresse(rs.getString("adresse"));

        String paiementStr = rs.getString("paiement");
        if (paiementStr != null) {
            client.setPaiement(Paiement.valueOf(paiementStr));
        }

        client.setID(rs.getInt("id"));

        return client;
    }
}
