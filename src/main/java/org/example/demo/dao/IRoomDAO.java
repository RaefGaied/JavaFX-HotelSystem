package org.example.demo.dao;

import org.example.demo.model.Room;

import java.util.List;

public interface IRoomDAO {
    void addRoom(Room room);
    List<Room> getAllRooms();
    Room getRoomByNumber(int number);
    boolean updateRoom(Room room);
    void deleteRoom(int number);
}
