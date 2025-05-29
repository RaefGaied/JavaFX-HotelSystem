package org.example.demo.controller;


import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import com.itextpdf.text.Document;
import org.example.demo.dao.DBConnection;
import org.example.demo.model.Reservation;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BillInfoController implements Initializable {

    public static int selectedResID;
    public static Reservation selectedReservation;

    @FXML
    private TextField Amount;
    @FXML
    private Button print;
    @FXML
    private TextField customerIDNumber;
    @FXML
    private TextField customerName;
    @FXML
    private TextField roomNumber;

    private Connection connection;
    private DBConnection dbConnection;
    private PreparedStatement pst;

    public static void setSelectedReservationID(int selectedReservationID) {
        selectedResID = selectedReservationID;
    }

    public static void setSelectedReservation(Reservation reservation) {
        selectedReservation = reservation;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        connection = dbConnection.getInstance();
        if (selectedResID != 0) {
            String query = "SELECT res.reservationID, res.roomNumber, c.customerIDNumber, c.customerName, " +
                    "(r.price * DATEDIFF(res.checkOutDate, res.checkInDate)) AS totalPrice " +
                    "FROM customers c " +
                    "INNER JOIN reservations res ON c.customerIDNumber = res.customerIDNumber " +
                    "INNER JOIN rooms r ON r.roomNumber = res.roomNumber " +
                    "WHERE res.reservationID=?";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setInt(1, selectedResID);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        roomNumber.setText(rs.getString("roomNumber"));
                        customerIDNumber.setText(rs.getString("customerIDNumber"));
                        customerName.setText(rs.getString("customerName"));
                        Amount.setText(rs.getString("totalPrice"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            roomNumber.setEditable(false);
            customerIDNumber.setEditable(false);
            customerName.setEditable(false);
            Amount.setEditable(false);
        }
    }

    public void handlePrintAction(javafx.event.ActionEvent actionEvent) throws IOException {
        String id = "";
        String insertBills = "INSERT INTO bills(reservationID, billDate, billAmount) VALUES (?, ?, ?)";
        String updateRoom = "UPDATE rooms SET status='Not Booked' WHERE roomNumber=?";
        String updateReservation = "UPDATE reservations SET status='Checked Out' WHERE reservationID=?";
        String selectBill = "SELECT billID FROM bills WHERE reservationID=?";

        if (!selectedReservation.getStatus().equals("Checked Out")) {
            try {
                pst = connection.prepareStatement(insertBills);
                pst.setInt(1, selectedReservation.getReservationID());
                LocalDate checkOutDate = selectedReservation.getCheckOutDate();
                pst.setDate(2, Date.valueOf(checkOutDate));
                pst.setDouble(3, selectedReservation.getTotalPrice());
                pst.executeUpdate();

                pst = connection.prepareStatement(updateRoom);
                pst.setInt(1, selectedReservation.getRoomNumber());
                pst.executeUpdate();

                pst = connection.prepareStatement(updateReservation);
                pst.setInt(1, selectedReservation.getReservationID());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try (PreparedStatement pst = connection.prepareStatement(selectBill)) {
            pst.setInt(1, selectedReservation.getReservationID());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    id = rs.getString("billID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createBill(id);
    }

    private void createBill(String id) throws IOException {
        String path = "C:\\Users\\raefg\\OneDrive\\Bureau\\intellig workspace";
        String billQuery = "SELECT b.billID, c.customerIDNumber, c.customerName, c.customerPhoneNo, " +
                "r.roomNumber, r.roomType, r.price, res.checkInDate, res.checkOutDate, " +
                "(r.price * DATEDIFF(res.checkOutDate, res.checkInDate)) AS totalPrice, " +
                "DATEDIFF(res.checkOutDate, res.checkInDate) AS totalDay " +
                "FROM bills b " +
                "INNER JOIN reservations res ON b.reservationID = res.reservationID " +
                "INNER JOIN rooms r ON r.roomNumber = res.roomNumber " +
                "INNER JOIN customers c ON c.customerIDNumber = res.customerIDNumber " +
                "WHERE b.billID=?";

        try (PreparedStatement pst = connection.prepareStatement(billQuery)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String billID = rs.getString("billID");
                    String customerName = rs.getString("customerName");
                    String customerIDNumber = rs.getString("customerIDNumber");
                    String customerPhoneNo = rs.getString("customerPhoneNo");
                    String roomNumber = rs.getString("roomNumber");
                    String roomType = rs.getString("roomType");
                    String priceRoom = rs.getString("price");
                    String checkIn = rs.getString("checkInDate");
                    String checkOut = rs.getString("checkOutDate");
                    String totalDay = rs.getString("totalDay");
                    String totalPrice = rs.getString("totalPrice");

                    createPdfBill(billID, customerName, customerIDNumber, customerPhoneNo, roomNumber,
                            roomType, priceRoom, checkIn, checkOut, totalDay, totalPrice, path);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createPdfBill(String billID, String customerName, String customerIDNumber, String customerPhoneNo,
                               String roomNumber, String roomType, String priceRoom, String checkIn, String checkOut,
                               String totalDay, String totalPrice, String path) throws IOException {

        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path + "bill" + billID + ".pdf"));
            doc.open();

            doc.add(new Paragraph("Bill ID: " + billID + "\nCustomer Details:\nName: " + customerName +
                    "\nID Number: " + customerIDNumber + "\nMobile Number: " + customerPhoneNo + "\n"));

            // Room information
            doc.add(new Paragraph("\nRoom Details:\nRoom Number: " + roomNumber + "\nRoom Type: " + roomType +
                    "\nPrice Per Day: " + priceRoom + "\n"));

            PdfPTable table = new PdfPTable(4);
            table.addCell("Check In Date: " + checkIn);
            table.addCell("Check Out Date: " + checkOut);
            table.addCell("Number of Days Stay: " + totalDay);
            table.addCell("Total Amount Paid: " + totalPrice);
            doc.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
        doc.close();

        // Open the generated PDF
        File file = new File(path + "bill" + billID + ".pdf");
        if (file.toString().endsWith(".pdf"))
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file);
        else {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        }
    }
}
