package com.example.hotel;

public class Main {
    public static void main(String[] args) {
        HotelManager hotel = HotelManager.getInstance();
        System.out.println("Welcome to " + hotel.getHotelName());
        if (args.length > 0 && args[0].equals("--demo")) {
            Guest g = hotel.register("Amaya Perera", "199912345678", "0771234567", "amaya@example.com", true);
            Booking b = hotel.book("SUITE", g, 3, 2, true);
            System.out.println(b);
            return;
        }
        hotel.runMenu();
    }
}
