package org.example.demo.dao;

import org.example.demo.model.Bill;

import java.sql.SQLException;
import java.util.List;

public interface IBillDAO {
    void createBill(Bill bill) throws SQLException;
    List<Bill> getAllBills() throws SQLException;
    Bill getBillById(int billId) throws SQLException;
    void updateBill(Bill bill) throws SQLException;
    void deleteBill(int billId) throws SQLException;
}
