package org.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import org.example.demo.dao.*;
import org.example.demo.model.Reservation;

import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CheckInController implements Initializable {
    @FXML
    private Label amount, days, price;

    @FXML
    private TextField cEmail, cGender, cName, cNationality, cNumber, cPhone;

    @FXML
    private Button submit;

    @FXML
    private DatePicker inDate, outDate;

    @FXML
    private ComboBox<String> rNo, rType;

    private Connection connection;
    private DBConnection dbConnection;
    private IReservationDAO reservationDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        connection = dbConnection.getInstance();
        reservationDAO = new ReservationDAO(connection); // Injecting DAO
        insertRoomType();
    }

    private void insertRoomType() {
        rType.getItems().clear();
        String query = "SELECT DISTINCT roomType FROM rooms";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                rType.getItems().add(rs.getString("roomType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertRoomNo() {
        rNo.getItems().clear();
        String type = rType.getSelectionModel().getSelectedItem();
        String query = "SELECT roomNumber FROM rooms WHERE roomType=? AND status='Not Booked'";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, type);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    rNo.getItems().add(rs.getString("roomNumber"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleSelectRoomType(ActionEvent actionEvent) {
        if (!rType.getSelectionModel().isEmpty()) {
            insertRoomNo();
        }
    }

    public void handleSelectRoomNumber(ActionEvent actionEvent) {
        String priceVal = "Price: ";
        String no = rNo.getSelectionModel().getSelectedItem();
        String priceQuery = "SELECT price FROM rooms WHERE roomNumber=?";
        try (PreparedStatement pst = connection.prepareStatement(priceQuery)) {
            pst.setString(1, no);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    priceVal += rs.getString("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        price.setText(priceVal);
    }

    public void handleCheckInPick(ActionEvent actionEvent) {
        if (inDate.getValue() != null) {
            inDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    public void handleCheckOutPick(ActionEvent actionEvent) {
        if (inDate.getValue() != null && outDate.getValue() != null) {
            int x = outDate.getValue().compareTo(inDate.getValue());
            if (x > 0) {
                days.setText("Total days: " + x);
                String priceText = price.getText().replace("Price: ", "").trim();
                int p = Integer.parseInt(priceText.isEmpty() ? "0" : priceText);
                amount.setText("Total Amount: " + (p * x));
            } else {
                days.setText("Invalid dates!");
                amount.setText("Total Amount: 0");
            }
        }
    }

    public void handleSubmitAction(ActionEvent actionEvent) {
        String name = cName.getText();
        String email = cEmail.getText();
        String gender = cGender.getText();
        String nationality = cNationality.getText();
        String number = cNumber.getText();
        String phone = cPhone.getText();
        String roomNo = rNo.getSelectionModel().getSelectedItem();
        String checkIn = inDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String checkOut = outDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (name.equals("") || email.equals("") || gender.equals("") || nationality.equals("")
                || number.equals("") || phone.equals("") || roomNo.equals("") || checkIn.equals("") || checkOut.equals("")) {
            OptionPane("Every Field is required", "Error Message");
        } else {
            String insertCustomer = "INSERT INTO customers(customerIDNumber, customerName, customerNationality, customerGender, customerPhoneNo, customerEmail)"
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            String insertReservation = "INSERT INTO reservations(customerIDNumber, roomNumber, checkInDate, checkOutDate) VALUES (?, ?, ?, ?)";
            String updateRoom = "UPDATE rooms SET status=\"Booked\" WHERE roomNumber=?";
            PreparedStatement pst = null;
            try {
                pst = connection.prepareStatement(insertCustomer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pst.setString(1, number);
                pst.setString(2, name);
                pst.setString(3, nationality);
                pst.setString(4, gender);
                pst.setString(5, phone);
                pst.setString(6, email);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pst = connection.prepareStatement(insertReservation);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pst.setString(1, number);
                pst.setString(2, roomNo);
                pst.setString(3, checkIn);
                pst.setString(4, checkOut);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pst = connection.prepareStatement(updateRoom);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pst.setString(1, roomNo);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            OptionPane("Check In Successful", "Message");
        }
    }
    private void OptionPane(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
