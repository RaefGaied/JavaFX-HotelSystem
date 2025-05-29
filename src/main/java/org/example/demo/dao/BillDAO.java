package org.example.demo.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.demo.model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDAO implements IBillDAO {
    private final Connection connection;
    public static final ObservableList<Bill> bills = FXCollections.observableArrayList();

    public BillDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createBill(Bill bill) throws SQLException {
        String query = "INSERT INTO bills (customerName, customerID, billDate, billAmount, roomNumber) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, bill.getCustomerName());
            pst.setInt(2, bill.getCustomerID());  // Corrected method
            pst.setString(3, bill.getDate());
            pst.setInt(4, bill.getAmount());  // Corrected data type
            pst.setInt(5, bill.getRoomNumber());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Bill> getAllBills() throws SQLException {
        List<Bill> billList = new ArrayList<>();
        String query = "SELECT b.*, res.roomNumber, res.customerIDNumber, c.customerName FROM bills b " +
                "INNER JOIN reservations res ON b.reservationID = res.reservationID " +
                "INNER JOIN customers c ON res.customerIDNumber = c.customerIDNumber";

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int roomNumber = rs.getInt("roomNumber");
                int customerIDNumber = rs.getInt("customerIDNumber");
                int billID = rs.getInt("billID");
                String billDate = rs.getString("billDate");
                String customerName = rs.getString("customerName");
                int billAmount = rs.getInt("billAmount");

                Bill bill = new Bill(billID, customerName, customerIDNumber, billDate, billAmount, roomNumber);
                billList.add(bill);
                bills.add(bill); // Adding the bill to the ObservableList
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billList; // Return the list of bills
    }

    @Override
    public Bill getBillById(int billId) throws SQLException {
        Bill bill = null;
        String query = "SELECT * FROM bills WHERE billID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, billId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    bill = new Bill(
                            rs.getInt("billID"),
                            rs.getString("customerName"),
                            rs.getInt("customerID"),
                            rs.getString("billDate"),
                            rs.getInt("billAmount"),
                            rs.getInt("roomNumber")
                    );
                }
            }
        }
        return bill;
    }

    @Override
    public void updateBill(Bill bill) throws SQLException {
        String query = "UPDATE bills SET customerName = ?, customerID = ?, billDate = ?, billAmount = ?, roomNumber = ? WHERE billID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, bill.getCustomerName());
            pst.setInt(2, bill.getCustomerID());  // Corrected method
            pst.setString(3, bill.getDate());
            pst.setInt(4, bill.getAmount());  // Corrected data type
            pst.setInt(5, bill.getRoomNumber());
            pst.setInt(6, bill.getBillID());
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteBill(int billId) throws SQLException {
        String query = "DELETE FROM bills WHERE billID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, billId);
            pst.executeUpdate();
        }
    }
}
