package org.example.demo.dao;

import org.example.demo.model.Reservation;


import java.sql.SQLException;
import java.util.List;

public interface IReservationDAO {
    List<Reservation> getAllReservations() throws SQLException;
    Reservation getReservationById(int reservationId) throws SQLException;
    void saveReservation(Reservation reservation) throws SQLException;
    void updateReservation(Reservation reservation) throws SQLException;
    void deleteReservation(int reservationId) throws SQLException;
    void insertReservation(Reservation reservation) throws SQLException;
    void updateRoomStatus(String roomNumber) throws SQLException;

}
