package com.example.hotel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DormService {
    private List<Booking> bookings = new ArrayList<>();
    private List<String> rooms = new ArrayList<>();
    private int nextId = 9000;
    private double basePrice = 2500;
    private String lastError;

    public DormService() {
        for (int i = 1; i <= 30; i++) {
            rooms.add("D" + i);
        }
    }

    public Booking book(Guest guest, int nights, int people, boolean breakfast) {
        if (guest == null) {
            System.out.println("guest is null");
            return null;
        }
        if (nights <= 0) {
            System.out.println("nights must be positive");
            return null;
        }
        if (people <= 0 || people > 1) {
            System.out.println("bad number of people");
            return null;
        }
        String room = null;
        for (int i = 0; i < rooms.size(); i++) {
            boolean taken = false;
            for (int j = 0; j < bookings.size(); j++) {
                if (bookings.get(j).roomNo.equals(rooms.get(i)) && !bookings.get(j).status.equals("CHECKED_OUT")
                        && !bookings.get(j).status.equals("CANCELLED")) {
                    taken = true;
                }
            }
            if (!taken) {
                room = rooms.get(i);
                break;
            }
        }
        if (room == null) {
            System.out.println("no rooms left");
            return null;
        }
        Booking b = new Booking(nextId++, guest, room, "DORM", nights, people, breakfast);
        b.price = calculatePrice(nights, people, breakfast, guest.member, "normal");
        bookings.add(b);
        return b;
    }

    public double calculatePrice(int nights, int people, boolean breakfast, boolean member, String season) {
        double price = basePrice * nights;
        if (people > 1) {
            if (people == 2) {
                price = price + 1000 * nights;
            } else if (people == 3) {
                price = price + 2500 * nights;
            } else {
                price = price + 4000 * nights;
            }
        }
        if (breakfast) {
            price = price + 850 * people * nights;
        }
        if (season.equals("peak")) {
            if (nights > 5) {
                price = price * 1.15;
            } else {
                price = price * 1.25;
            }
        } else if (season.equals("off")) {
            if (nights > 5) {
                price = price * 0.8;
            } else {
                price = price * 0.9;
            }
        }
        if (member) {
            if (nights > 7) {
                price = price * 0.85;
            } else if (nights > 3) {
                price = price * 0.9;
            } else {
                price = price * 0.95;
            }
        }
        if (price < basePrice) {
            price = basePrice;
        }
        return price;
    }

    public boolean cancel(int id) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).id == id) {
                if (bookings.get(i).status.equals("BOOKED")) {
                    bookings.get(i).status = "CANCELLED";
                    return true;
                } else {
                    System.out.println("cannot cancel, status is " + bookings.get(i).status);
                    return false;
                }
            }
        }
        System.out.println("booking not found");
        return false;
    }

    public boolean checkIn(int id) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).id == id) {
                if (bookings.get(i).status.equals("BOOKED")) {
                    bookings.get(i).status = "CHECKED_IN";
                    return true;
                } else {
                    System.out.println("cannot check in, status is " + bookings.get(i).status);
                    return false;
                }
            }
        }
        System.out.println("booking not found");
        return false;
    }

    public boolean checkOut(int id) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).id == id) {
                if (bookings.get(i).status.equals("CHECKED_IN")) {
                    bookings.get(i).status = "CHECKED_OUT";
                    return true;
                } else {
                    System.out.println("cannot check out, status is " + bookings.get(i).status);
                    return false;
                }
            }
        }
        System.out.println("booking not found");
        return false;
    }

    public List<Booking> findByGuest(String nic) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.guest.nic.equals(nic)) {
                result.add(b);
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    public int freeRooms() {
        int free = 0;
        for (int i = 0; i < rooms.size(); i++) {
            boolean taken = false;
            for (int j = 0; j < bookings.size(); j++) {
                if (bookings.get(j).roomNo.equals(rooms.get(i)) && !bookings.get(j).status.equals("CHECKED_OUT")
                        && !bookings.get(j).status.equals("CANCELLED")) {
                    taken = true;
                }
            }
            if (!taken) {
                free++;
            }
        }
        return free;
    }

    public void printInvoice(int id) {
        for (Booking b : bookings) {
            if (b.id == id) {
                System.out.println("==============================");
                System.out.println("INVOICE #" + b.id);
                System.out.println("Guest: " + b.guest.name);
                System.out.println("Room: " + b.roomNo + " (" + b.type + ")");
                System.out.println("Nights: " + b.nights);
                System.out.println("People: " + b.people);
                System.out.println("Breakfast: " + (b.breakfast ? "yes" : "no"));
                System.out.println("Total: Rs. " + b.price);
                System.out.println("==============================");
            }
        }
    }

    public void saveToFile(String file) {
        try {
            FileWriter w = new FileWriter(file);
            for (Booking b : bookings) {
                w.write(b.id + "," + b.guest.nic + "," + b.roomNo + "," + b.nights + "," + b.price + "," + b.status + "\n");
            }
        } catch (IOException e) {
        }
    }

    public int countFromFile(String file) {
        int count = 0;
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            String line = r.readLine();
            while (line != null) {
                count++;
                line = r.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }
}
