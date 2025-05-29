package org.example.demo.dao;

import org.example.demo.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO implements IRoomDAO {
    private final Connection connection;

    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRoom(Room room) {
        String query = "INSERT INTO rooms (roomNumber, roomType, price, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, room.getNumber());
            pst.setString(2, room.getType());
            pst.setInt(3, room.getPrice());
            pst.setString(4, room.getStatus());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
        }
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("roomNumber"),
                        rs.getInt("price"),
                        rs.getString("roomType"),
                        rs.getString("status")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public Room getRoomByNumber(int number) {
        String query = "SELECT * FROM rooms WHERE roomNumber = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, number);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Room(
                            rs.getInt("roomNumber"),
                            rs.getInt("price"),
                            rs.getString("roomType"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching room: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateRoom(Room room) {
        String query = "UPDATE rooms SET roomType = ?, price = ?, status = ? WHERE roomNumber = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            System.out.println("Updating room: " + room.getNumber()); // Debug: Print room details
            pst.setString(1, room.getType());
            pst.setInt(2, room.getPrice());
            pst.setString(3, room.getStatus());
            pst.setInt(4, room.getNumber());

            // Execute the update query and check the number of affected rows
            int rowsAffected = pst.executeUpdate();

            // Log the number of affected rows
            System.out.println("Rows affected: " + rowsAffected);

            // If no rows were affected, that means the room number doesn't exist
            if (rowsAffected == 0) {
                System.err.println("Room number " + room.getNumber() + " does not exist.");
                return false;
            }

            // Return true if the update was successful
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }



    @Override
    public void deleteRoom(int number) {
        String query = "DELETE FROM rooms WHERE roomNumber = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, number);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
        }
    }
}
