package org.example.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.util.Callback;
import org.example.demo.dao.DBConnection;
import org.example.demo.model.Room;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoomController implements Initializable {

    @FXML
    private TableColumn<Room, String> price;

    @FXML
    private TableColumn<Room, String> roomNumber;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, String> roomType;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Room, String> status;

    private Connection connection;

    private DBConnection dbConnection;

    private PreparedStatement pst;

    public static final ObservableList<Room> rooms = FXCollections.observableArrayList();

    public static final List<Room> roomList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        connection = dbConnection.getInstance();
        roomNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomType.setCellValueFactory(new PropertyValueFactory<>("type"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        status.setCellValueFactory(new PropertyValueFactory<Room, String>("status"));
        try {
            initRoomList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        roomTable.setItems(rooms);
    }

    public void handleAddAction(javafx.event.ActionEvent actionEvent) throws IOException {
        Stage add = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/addroom.fxml"));
        Scene scene = new Scene(root);
        add.setScene(scene);
        add.show();
    }

    public void handleViewAction(javafx.event.ActionEvent actionEvent) throws IOException {

    }

    public void clickItem(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2 && roomTable.getSelectionModel().getSelectedItem() != null) {
            Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();
            if ("Booked".equals(selectedRoom.getStatus())) {
                CustomerController.setSelectedRoomNumber(selectedRoom.getNumber());
                Stage add = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/customerinfo.fxml"));
                Scene scene = new Scene(root);
                add.setScene(scene);
                add.show();
            }
        }
    }


    public void initRoomList() throws IOException {
        roomList.clear();
        rooms.clear();
        String query = "SELECT * FROM rooms";
        try {
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int room_price = Integer.parseInt(rs.getString("price"));
                String room_type = rs.getString("roomType");
                String room_status = rs.getString("status");
                int room_num = Integer.parseInt(rs.getString("roomNumber"));
                roomList.add(new Room(room_num, room_price, room_type, room_status));
                rooms.add(new Room(room_num, room_price, room_type, room_status));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Search(ObservableList<Room> rooms, String s) {
        rooms.clear();
        roomList.stream()  // Create a stream from roomList
                .filter(room -> Integer.toString(room.getNumber()).startsWith(s))  // Filter rooms based on search text
                .forEach(rooms::add);  // Add the filtered rooms to the ObservableList
    }



    public void handleSearchKey(KeyEvent event) {
        String s = search.getText();
        if (s.isEmpty()) {
            roomTable.setItems(rooms);
        } else {
            Search(rooms, s);
        }
    }
    public void handleUpdateAction(javafx.event.ActionEvent actionEvent) throws IOException {
        Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            Stage updateStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/updateroom.fxml"));
            Scene scene = new Scene(root);
            updateStage.setScene(scene);
            updateStage.show();
        } else {
            System.out.println("No room selected for update");
        }
    }
    public void handleDeleteAction(javafx.event.ActionEvent actionEvent) {
        Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            String deleteQuery = "DELETE FROM rooms WHERE roomNumber = ?";
            try (PreparedStatement pst = connection.prepareStatement(deleteQuery)) {
                pst.setInt(1, selectedRoom.getNumber());
                pst.executeUpdate();
                initRoomList();
                roomTable.refresh();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No room selected for deletion");
        }
    }

}
