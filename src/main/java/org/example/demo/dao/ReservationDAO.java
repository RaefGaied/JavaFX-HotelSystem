package org.example.demo.dao;

import org.example.demo.model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO implements IReservationDAO {
    private Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;  // Inject the connection via constructor
    }

    @Override
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String query = "SELECT * FROM reservations";  // Adjust based on your schema

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("reservationId"),
                        rs.getInt("roomNumber"), // Ensure the column name is correct
                        rs.getString("customerName"),
                        rs.getDate("checkInDate").toLocalDate(), // Convert to LocalDate
                        rs.getDate("checkOutDate").toLocalDate(), // Convert to LocalDate
                        rs.getInt("totalDays"),
                        rs.getInt("totalPrice"),
                        rs.getString("status")
                );
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    @Override
    public Reservation getReservationById(int reservationId) throws SQLException {
        String query = "SELECT * FROM reservations WHERE reservationId = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, reservationId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                            rs.getInt("reservationId"),
                            rs.getInt("roomId"),
                            rs.getString("customerName"),
                            rs.getDate("checkInDate").toLocalDate(), // Convert java.sql.Date to LocalDate
                            rs.getDate("checkOutDate").toLocalDate(), // Convert java.sql.Date to LocalDate
                            rs.getInt("totalDays"),
                            rs.getInt("totalPrice"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;  // No reservation found
    }


    @Override
    public void saveReservation(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservations (roomId, customerName, checkInDate, checkOutDate, totalAmount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, reservation.getRoomNumber()); // Use roomNumber as the database expects
            pst.setString(2, reservation.getCustomerName());
            pst.setDate(3, java.sql.Date.valueOf(reservation.getCheckInDate())); // Convert LocalDate to java.sql.Date
            pst.setDate(4, java.sql.Date.valueOf(reservation.getCheckOutDate())); // Convert LocalDate to java.sql.Date
            pst.setDouble(5, reservation.getTotalPrice());
            pst.executeUpdate();
        }
    }



    @Override
    public void deleteReservation(int reservationId) throws SQLException {
        String query = "DELETE FROM reservations WHERE reservationId = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, reservationId);
            pst.executeUpdate();
        }
    }

    @Override
    public void insertReservation(Reservation reservation) throws SQLException {
        String insertReservation = "INSERT INTO reservations (customerName, roomNumber, checkInDate, checkOutDate, totalDays, totalPrice, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(insertReservation)) {
            pst.setString(1, reservation.getCustomerName());
            pst.setInt(2, reservation.getRoomNumber());
            pst.setDate(3, Date.valueOf(reservation.getCheckInDate()));
            pst.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
            pst.setInt(5, reservation.getTotalDays());
            pst.setInt(6, reservation.getTotalPrice());
            pst.setString(7, reservation.getStatus());
            pst.executeUpdate();
        }
    }

    @Override
    public void updateRoomStatus(String roomNumber) throws SQLException {
        String updateRoom = "UPDATE rooms SET status = 'Booked' WHERE roomNumber = ?";
        try (PreparedStatement pst = connection.prepareStatement(updateRoom)) {
            pst.setString(1, roomNumber);
            pst.executeUpdate();
        }
    }

    @Override
    public void updateReservation(Reservation reservation) throws SQLException {
        String query = "UPDATE reservations SET roomId = ?, customerName = ?, checkInDate = ?, checkOutDate = ?, totalAmount = ? WHERE reservationId = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, reservation.getRoomNumber()); // Use roomNumber as the database expects
            pst.setString(2, reservation.getCustomerName());
            pst.setDate(3, java.sql.Date.valueOf(reservation.getCheckInDate())); // Convert LocalDate to java.sql.Date
            pst.setDate(4, java.sql.Date.valueOf(reservation.getCheckOutDate())); // Convert LocalDate to java.sql.Date
            pst.setDouble(5, reservation.getTotalPrice());
            pst.setInt(6, reservation.getReservationID());
            pst.executeUpdate();
        }
    }

}
