package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;
import org.example.demo.dao.DBConnection;
import org.example.demo.dao.RoomDAO;
import org.example.demo.model.Room;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class UpdateRoomController implements Initializable {

    @FXML
    private Button update;

    @FXML
    private Button cancel;

    @FXML
    private TextField number;

    @FXML
    private TextField price;

    @FXML
    private ComboBox<String> type;

    private RoomDAO roomDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = new DBConnection().getInstance();
        roomDAO = new RoomDAO(connection);
    }

    @FXML
    public void handleUpdateAction() {
        String roomNumberText = number.getText();
        String roomPriceText = price.getText();
        String roomTypeText = type.getValue();

        if (isValidInput(roomNumberText, roomPriceText, roomTypeText)) {
            Room updatedRoom = new Room(Integer.parseInt(roomNumberText), Integer.parseInt(roomPriceText), roomTypeText, "Not Booked");

            try {
                boolean isUpdated = roomDAO.updateRoom(updatedRoom);
                if (isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room updated successfully!");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Not Found", "Room with the specified number does not exist.");
                }
                clearFields();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update room: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please provide valid inputs.");
        }
    }


    private boolean isValidInput(String roomNumberText, String roomPriceText, String roomTypeText) {
        try {
            int roomNumber = Integer.parseInt(roomNumberText);
            int price = Integer.parseInt(roomPriceText);
            return roomTypeText != null && !roomTypeText.trim().isEmpty() && roomNumber > 0 && price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void clearFields() {
        number.clear();
        price.clear();
        type.setValue(null);
    }

    @FXML
    private void handleCancelAction() {
        Window cancelButton = cancel.getScene().getWindow();
        cancelButton.hide();
    }
}
