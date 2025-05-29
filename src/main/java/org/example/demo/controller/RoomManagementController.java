package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import org.example.demo.dao.DBConnection;
import org.example.demo.dao.RoomDAO;
import org.example.demo.model.Room;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class RoomManagementController implements Initializable {

    @FXML
    private Button add;

    @FXML
    private Button update;

    @FXML
    private Button delete;

    @FXML
    private TextField number;

    @FXML
    private TextField price;

    @FXML
    private TextField type;

    private RoomDAO roomDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = new DBConnection().getInstance();
        roomDAO = new RoomDAO(connection);
    }


    @FXML
    public void handleAddAction() {
        String roomNumberText = number.getText();
        String roomPriceText = price.getText();
        String roomTypeText = type.getText();

        if (isValidInput(roomNumberText, roomPriceText, roomTypeText)) {
            Room newRoom = new Room(Integer.parseInt(roomNumberText), Integer.parseInt(roomPriceText), roomTypeText, "Not Booked");

            try {
                roomDAO.addRoom(newRoom);
                showAlert(AlertType.INFORMATION, "Success", "Room added successfully!");
                clearFields();
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to add room: " + e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Invalid Input", "Please provide valid inputs.");
        }
    }



    @FXML
    public void handleDeleteAction() {
        String roomNumberText = number.getText();

        if (isValidNumber(roomNumberText)) {
            int roomNumber = Integer.parseInt(roomNumberText);

            try {
                roomDAO.deleteRoom(roomNumber);
                showAlert(AlertType.INFORMATION, "Success", "Room deleted successfully!");
                clearFields();
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete room: " + e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Invalid Input", "Please provide a valid room number.");
        }
    }


    private boolean isValidInput(String roomNumberText, String roomPriceText, String roomTypeText) {
        try {
            int roomNumber = Integer.parseInt(roomNumberText);
            int price = Integer.parseInt(roomPriceText);
            return !roomTypeText.trim().isEmpty() && roomNumber > 0 && price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidNumber(String roomNumberText) {
        try {
            int roomNumber = Integer.parseInt(roomNumberText);
            return roomNumber > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        number.clear();
        price.clear();
        type.clear();
    }
}
