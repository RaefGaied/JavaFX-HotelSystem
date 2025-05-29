package org.example.demo.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.demo.dao.DBConnection;
import org.example.demo.model.Reservation;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CheckOutController implements Initializable {


    @FXML
    private TableColumn<Reservation, String> checkIn;

    @FXML
    private TableColumn<Reservation, String> checkOut;

    @FXML
    private TableColumn<Reservation, String> customerName;

    @FXML
    private TableColumn<Reservation, String> roomNumber;

    @FXML
    private TableView<Reservation> roomTable;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Reservation, String> totalDays;

    @FXML
    private TableColumn<Reservation, String> totalPrice;

    @FXML
    private TableColumn<?, ?> status;

    @FXML
    private ComboBox<String> sort;

    private Connection connection;

    private DBConnection dbConnection;

    private PreparedStatement pst;

    public static final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    public static final List<Reservation> reservationList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        connection = dbConnection.getInstance();

        sort.getItems().addAll("Today", "Checked In", "Checked Out");

        // Setting up the TableView columns
        roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        checkIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        totalDays.setCellValueFactory(new PropertyValueFactory<>("totalDays"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            initReservationList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        roomTable.setItems(reservations);
    }

    public void initReservationList() throws IOException {
        reservationList.clear();
        reservations.clear();

        String query = "SELECT res.status, res.reservationID, res.roomNumber, c.customerName, res.checkInDate, res.checkOutDate, " +
                "DATEDIFF(res.checkOutDate, res.checkInDate) AS totalDays, " +
                "(r.price * DATEDIFF(res.checkOutDate, res.checkInDate)) AS totalPrice " +
                "FROM customers c " +
                "INNER JOIN reservations res ON c.customerIDNumber = res.customerIDNumber " +
                "INNER JOIN rooms r ON r.roomNumber = res.roomNumber";

        try {
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int res_id = Integer.parseInt(rs.getString("reservationID"));
                int room_no = Integer.parseInt(rs.getString("roomNumber"));
                String cus_name = rs.getString("customerName");

                String check_in_str = rs.getString("checkInDate");
                String check_out_str = rs.getString("checkOutDate");
                LocalDate check_in_date = LocalDate.parse(check_in_str);
                LocalDate check_out_date = LocalDate.parse(check_out_str);

                int total_price = Integer.parseInt(rs.getString("totalPrice"));
                int total_days = Integer.parseInt(rs.getString("totalDays"));
                String res_status = rs.getString("status");

                Reservation reservation = new Reservation(res_id, room_no, cus_name, check_in_date, check_out_date, total_days, total_price, res_status);

                reservationList.add(reservation);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchByRoomNumber(String searchText) {
        List<Reservation> filteredList = reservationList.stream()
                .filter(reservation -> Integer.toString(reservation.getRoomNumber()).startsWith(searchText))
                .collect(Collectors.toList());

        reservations.setAll(filteredList);
    }

    public void handleSearchKey(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            searchByRoomNumber(search.getText());
        }
    }

    public void updateTable(Reservation updatedReservation) {
        reservations.stream()
                .filter(reservation -> reservation.equals(updatedReservation))
                .findFirst()
                .ifPresent(reservation -> reservation.setStatus("Checked Out"));
        roomTable.setItems(reservations);
    }

    public void clickItem(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2 && roomTable.getSelectionModel().getSelectedItem() != null) {
            Reservation selectedReservation = roomTable.getSelectionModel().getSelectedItem();
            BillInfoController.setSelectedReservationID(selectedReservation.getReservationID());
            BillInfoController.setSelectedReservation(selectedReservation);
            Stage add = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/billinfo.fxml"));
            Scene scene = new Scene(root);
            add.setScene(scene);
            add.show();
        }
    }

    public void handleComboboxSelection(javafx.event.ActionEvent actionEvent) {
        String selectedSort = sort.getSelectionModel().getSelectedItem();
        if (selectedSort != null) {
            List<Reservation> filteredReservations = switch (selectedSort) {
                case "Today" -> reservationList.stream()
                        .filter(reservation -> reservation.getCheckOutDate().equals(java.time.LocalDate.now().toString()) && reservation.getStatus().equals("Checked In"))
                        .collect(Collectors.toList());
                case "Checked In" -> reservationList.stream()
                        .filter(reservation -> reservation.getStatus().equals("Checked In"))
                        .collect(Collectors.toList());
                case "Checked Out" -> reservationList.stream()
                        .filter(reservation -> reservation.getStatus().equals("Checked Out"))
                        .collect(Collectors.toList());
                default -> reservationList;
            };

            reservations.setAll(filteredReservations);
        }
    }
}
