package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.example.demo.dao.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private Label avaRoom;

    @FXML
    private Label bookedRoom;

    @FXML
    private Label earning;

    @FXML
    private Label pending;

    @FXML
    private Label totalRoom;

    private Connection connection;

    private DBConnection dbConnection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        connection = dbConnection.getInstance();

        // Requêtes SQL
        String roomQuery = "SELECT COUNT(roomNumber) AS totalRooms, " +
                "(SELECT COUNT(roomNumber) FROM rooms WHERE status = 'Booked') AS totalBooked, " +
                "(SELECT COUNT(roomNumber) FROM rooms WHERE status = 'Not Booked') AS totalNotBooked " +
                "FROM rooms";

        String financeQuery = "SELECT SUM(b.billAmount) AS totalEarnings, " +
                "(SELECT SUM((r.price * DATEDIFF(res.checkOutDate, res.checkInDate))) " +
                "FROM reservations res INNER JOIN rooms r ON r.roomNumber = res.roomNumber " +
                "WHERE res.status = 'Checked In') AS totalPendings " +
                "FROM bills b INNER JOIN reservations res ON res.reservationID = b.reservationID";

        // Exécution des requêtes
        executeQuery(roomQuery, rs -> {
            totalRoom.setText(rs.getString("totalRooms"));
            bookedRoom.setText(rs.getString("totalBooked"));
            avaRoom.setText(rs.getString("totalNotBooked"));
        });

        executeQuery(financeQuery, rs -> {
            earning.setText(rs.getString("totalEarnings"));
            pending.setText(rs.getString("totalPendings"));
        });
    }

    /**
     * Méthode générique pour exécuter une requête SQL et traiter les résultats via une lambda.
     *
     * @param query     La requête SQL à exécuter.
     * @param processor Fonction lambda pour traiter chaque ligne du ResultSet.
     */
    private void executeQuery(String query, ResultSetProcessor processor) {
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                processor.process(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Interface fonctionnelle permettant de définir une logique de traitement pour un ResultSet.
     */
    @FunctionalInterface
    private interface ResultSetProcessor {
        void process(ResultSet resultSet) throws SQLException;
    }
}
