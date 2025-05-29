package org.example.demo.model;

import java.util.Objects;

public class Room {

    private int number;
    private int price;
    private String type;
    private String status;

    // Constructeur
    public Room(int number, int price, String type, String status) {
        this.number = number;
        this.price = price;
        this.type = type;
        this.status = status;
    }

    // Getters
    public int getNumber() {
        return number;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setNumber(int number) {
        this.number = number;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Redéfinition de equals() et hashCode() pour faciliter la gestion dans des collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return number == room.number;  // Comparaison basée sur le numéro de chambre
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    // toString() pour faciliter l'affichage
    @Override
    public String toString() {
        return String.format("Room[Number=%d, Type='%s', Price=%d, Status='%s']",
                number, type, price, status);
    }

    // Utilisation d'un flux pour filtrer et afficher les chambres disponibles, par exemple
    public static void printAvailableRooms(Iterable<Room> rooms) {
        rooms.forEach(room -> {
            if ("available".equalsIgnoreCase(room.getStatus())) {
                System.out.println(room);
            }
        });
    }

    // Méthode pour filtrer les chambres par prix et type
    public static void filterRoomsByPriceAndType(Iterable<Room> rooms, int minPrice, String type) {
        rooms.forEach(room -> {
            if (room.getPrice() >= minPrice && room.getType().equalsIgnoreCase(type)) {
                System.out.println(room);
            }
        });
    }

    // Méthode pour manipuler les chambres en utilisant les Streams
    public static void filterRoomsWithStream(Iterable<Room> rooms, int minPrice, String type) {
        rooms.forEach(room -> {
            if (room.getPrice() >= minPrice && room.getType().equalsIgnoreCase(type)) {
                System.out.println(room);
            }
        });
    }
}
