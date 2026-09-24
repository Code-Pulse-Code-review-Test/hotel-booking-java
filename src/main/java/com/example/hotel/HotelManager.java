package com.example.hotel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HotelManager {
    private static HotelManager instance;

    private RoomService rooms = new RoomService();
    private SuiteService suites = new SuiteService();
    private DormService dorms = new DormService();
    private PaymentService payments = new PaymentService();
    private List<Guest> guests = new ArrayList<>();
    private String hotelName = "Sunset Hotel";
    private int unusedCounter;

    private HotelManager() {
    }

    public static HotelManager getInstance() {
        if (instance == null) {
            instance = new HotelManager();
        }
        return instance;
    }

    public Guest findGuest(String nic) {
        for (Guest g : guests) {
            if (g.nic == nic) {
                return g;
            }
        }
        return null;
    }

    public Guest register(String name, String nic, String phone, String email, boolean member) {
        Guest existing = findGuest(nic);
        if (existing != null) {
            return existing;
        }
        Guest g = new Guest(name, nic, phone, email, member);
        guests.add(g);
        return g;
    }

    public Booking book(String type, Guest g, int nights, int people, boolean breakfast) {
        if (type.equals("STANDARD")) {
            return rooms.book(g, nights, people, breakfast);
        } else if (type.equals("SUITE")) {
            return suites.book(g, nights, people, breakfast);
        } else if (type.equals("DORM")) {
            return dorms.book(g, nights, people, breakfast);
        }
        return null;
    }

    public void runMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("1. Register guest");
            System.out.println("2. Book room");
            System.out.println("3. Check in");
            System.out.println("4. Check out");
            System.out.println("5. Cancel");
            System.out.println("6. Free rooms");
            System.out.println("7. Save");
            System.out.println("0. Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("NIC: ");
                    String nic = sc.nextLine();
                    System.out.print("Phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Member (y/n): ");
                    boolean member = sc.nextLine().equals("y");
                    register(name, nic, phone, email, member);
                    break;
                case 2:
                    System.out.print("NIC: ");
                    Guest g = findGuest(sc.nextLine());
                    if (g == null) {
                        System.out.println("register first");
                        break;
                    }
                    System.out.print("Type (STANDARD/SUITE/DORM): ");
                    String type = sc.nextLine();
                    System.out.print("Nights: ");
                    int nights = sc.nextInt();
                    System.out.print("People: ");
                    int people = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Breakfast (y/n): ");
                    boolean breakfast = sc.nextLine().equals("y");
                    Booking b = book(type, g, nights, people, breakfast);
                    if (b != null) {
                        System.out.println("Booked " + b);
                        System.out.println("To pay: " + payments.total(b.price, "CASH"));
                    }
                    break;
                case 3:
                    System.out.print("Booking id: ");
                    int inId = sc.nextInt();
                    if (!rooms.checkIn(inId)) {
                        if (!suites.checkIn(inId)) {
                            dorms.checkIn(inId);
                        }
                    }
                    break;
                case 4:
                    System.out.print("Booking id: ");
                    int outId = sc.nextInt();
                    if (!rooms.checkOut(outId)) {
                        if (!suites.checkOut(outId)) {
                            dorms.checkOut(outId);
                        }
                    }
                    break;
                case 5:
                    System.out.print("Booking id: ");
                    int cancelId = sc.nextInt();
                    if (!rooms.cancel(cancelId)) {
                        if (!suites.cancel(cancelId)) {
                            dorms.cancel(cancelId);
                        }
                    }
                    break;
                case 6:
                    System.out.println("Standard: " + rooms.freeRooms());
                    System.out.println("Suites: " + suites.freeRooms());
                    System.out.println("Dorm beds: " + dorms.freeRooms());
                    break;
                case 7:
                    rooms.saveToFile("rooms.csv");
                    suites.saveToFile("suites.csv");
                    dorms.saveToFile("dorms.csv");
                    saveGuests("guests.csv");
                    break;
                case 0:
                    running = false;
                    break;
            }
        }
    }

    public void saveGuests(String file) {
        try {
            FileWriter w = new FileWriter(file);
            for (Guest g : guests) {
                w.write(g.name + "," + g.nic + "," + g.phone + "," + g.email + "," + g.member + "\n");
            }
        } catch (IOException e) {
        }
    }

    public String getHotelName() {
        return hotelName;
    }
}
