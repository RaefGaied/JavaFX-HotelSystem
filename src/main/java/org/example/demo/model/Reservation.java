package org.example.demo.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private int reservationID;
    private int roomNumber;
    private String customerName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int totalDays;
    private int totalPrice;
    private String status;



    // Constructor using all fields
    public Reservation(int reservationID, int roomNumber, String customerName, LocalDate checkInDate, LocalDate checkOutDate, int totalDays, int totalPrice, String status) {
        this.reservationID = reservationID;
        this.roomNumber = roomNumber;
        this.customerName = customerName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalDays = totalDays;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Reservation(String customerName, String checkInDate, String checkOutDate, String status) {
        this.customerName = customerName;
        this.checkInDate = LocalDate.parse(checkInDate);  // Convert String to LocalDate
        this.checkOutDate = LocalDate.parse(checkOutDate);  // Convert String to LocalDate
        this.status = status;
    }



    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Calculate total price based on days and room rate (for example, 100 per day)
    public void calculateTotalPrice(int roomRatePerDay) {
        this.totalPrice = this.totalDays * roomRatePerDay;
    }

    // Override equals and hashCode for comparing Reservation objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return reservationID == that.reservationID && roomNumber == that.roomNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationID, roomNumber);
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return String.format("Reservation[ID=%d, Room=%d, Customer='%s', Status='%s', TotalPrice=%d]",
                reservationID, roomNumber, customerName, status, totalPrice);
    }

    // Static method to print reservations for debugging or listing
    public static void printReservations(Iterable<Reservation> reservations) {
        reservations.forEach(reservation -> System.out.println(reservation.toString()));
    }

    // Method to retrieve Customer ID (if it exists, otherwise, implement as needed)
    public String getCustomerIDNumber() {
        // Assuming customer name or other identifier is used. Adjust accordingly.
        // If you have a customer ID, implement logic to return it
        return "CustomerID: " + customerName.hashCode(); // Placeholder logic
    }
}
