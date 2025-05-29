package org.example.demo.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Bill implements Comparable<Bill> {
    private int billID;
    private String customerName;
    private int customerID;
    private String date;
    private int amount;
    private int roomNumber;

    public Bill(int billID, String customerName, int customerID, String date, int amount, int roomNumber) {
        this.billID = billID;
        this.customerName = customerName;
        this.customerID = customerID;
        this.date = date;
        this.amount = amount;
        this.roomNumber = roomNumber;
    }


    public int getBillID() { return billID; }
    public String getCustomerName() { return customerName; }
    public int getCustomerID() { return customerID; }
    public String getDate() { return date; }
    public int getAmount() { return amount; }
    public int getRoomNumber() { return roomNumber; }


    public void setBillID(int billID) { this.billID = billID; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    public void setDate(String date) { this.date = date; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }


    @Override
    public int compareTo(Bill other) {
        return Integer.compare(this.billID, other.billID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return billID == bill.billID && Objects.equals(date, bill.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billID, date);
    }

    @Override
    public String toString() {
        return String.format("Bill[ID=%d, Customer='%s', Date='%s', Amount=%d, Room=%d]",
                billID, customerName, date, amount, roomNumber);
    }
}
