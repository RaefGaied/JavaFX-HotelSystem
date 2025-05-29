package org.example.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.demo.dao.BillDAO;
import org.example.demo.dao.DBConnection;
import org.example.demo.model.Bill;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BillController implements Initializable {


    @FXML private TableColumn<Bill, String> Amount;
    @FXML private TableColumn<Bill, String> Date;
    @FXML private TableColumn<Bill, String> billID;
    @FXML private TableColumn<Bill, String> cusIDNumber;
    @FXML private TableColumn<Bill, String> customerName;
    @FXML private TableColumn<Bill, String> roomNumber;
    @FXML private TableView<Bill> billTable;
    @FXML private TextField search;

    private BillDAO billDAO;
    public static final ObservableList<Bill> bills = FXCollections.observableArrayList();
    private List<Bill> billList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get a Connection object from DBConnection
        Connection connection = DBConnection.getInstance();

        // Initialize DAO with the Connection
        billDAO = new BillDAO(connection); // Pass the Connection to the BillDAO constructor

        setUpTableColumns();
        loadBills();
    }

    private void setUpTableColumns() {
        roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        cusIDNumber.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        billID.setCellValueFactory(new PropertyValueFactory<>("billID"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
    }

    private void loadBills() {
        try {
            billList = billDAO.getAllBills();
            bills.setAll(billList);
            billTable.setItems(bills);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading bills", e.getMessage());
        }
    }

    private void searchBills(String query) {
        bills.setAll(billList.stream()
                .filter(bill -> bill.getDate().startsWith(query) || bill.getCustomerName().contains(query))
                .collect(Collectors.toList()));
    }

    @FXML
    public void handleSearchKey(KeyEvent event) {
        String query = search.getText();
        searchBills(query);
    }

    public void clickBill(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            if (billTable.getSelectionModel().getSelectedItem() != null) {
                String path = "C:\\Users\\raefg\\OneDrive\\Bureau\\intellig workspace\\";
                Bill selectedBill = billTable.getSelectionModel().getSelectedItem();
                File file = new File(path + "bill" + selectedBill.getBillID() + ".pdf");
                if (file.toString().endsWith(".pdf"))
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file);
                else {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(file);
                }
            }
        }
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    }



