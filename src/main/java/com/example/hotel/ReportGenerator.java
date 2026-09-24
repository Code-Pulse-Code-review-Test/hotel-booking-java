package com.example.hotel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public String occupancyReport(List<Booking> bookings, int totalRooms, String month) {
        int booked = 0;
        int checkedIn = 0;
        int checkedOut = 0;
        int cancelled = 0;
        int standard = 0;
        int suite = 0;
        int dorm = 0;
        int nights = 0;
        double income = 0;
        int unused = 0;
        for (Booking b : bookings) {
            if (b.status.equals("BOOKED")) {
                booked++;
            } else if (b.status.equals("CHECKED_IN")) {
                checkedIn++;
            } else if (b.status.equals("CHECKED_OUT")) {
                checkedOut++;
                income = income + b.price;
            } else if (b.status.equals("CANCELLED")) {
                cancelled++;
            }
            if (b.type.equals("STANDARD")) {
                standard++;
            } else if (b.type.equals("SUITE")) {
                suite++;
            } else if (b.type.equals("DORM")) {
                dorm++;
            }
            if (!b.status.equals("CANCELLED")) {
                nights = nights + b.nights;
            }
        }
        double occupancy = 0;
        if (totalRooms > 0) {
            occupancy = (double) nights / (totalRooms * 30) * 100;
        }
        String result = "";
        result = result + "Occupancy report for " + month + "\n";
        result = result + "------------------------------\n";
        result = result + "Booked: " + booked + "\n";
        result = result + "Checked in: " + checkedIn + "\n";
        result = result + "Checked out: " + checkedOut + "\n";
        result = result + "Cancelled: " + cancelled + "\n";
        result = result + "Standard: " + standard + "\n";
        result = result + "Suite: " + suite + "\n";
        result = result + "Dorm: " + dorm + "\n";
        result = result + "Room nights: " + nights + "\n";
        result = result + "Occupancy: " + Math.round(occupancy) + "%\n";
        result = result + "Income: Rs. " + income + "\n";
        if (occupancy < 30) {
            result = result + "Low month, think about promotions\n";
        } else if (occupancy > 85) {
            result = result + "Very busy month\n";
        }
        if (cancelled > booked / 4) {
            result = result + "A lot of cancellations\n";
        }
        return result;
    }

    public Map<String, Double> incomeByGuest(List<Booking> bookings) {
        Map<String, Double> income = new HashMap<>();
        for (Booking b : bookings) {
            if (b.status.equals("CANCELLED")) {
                continue;
            }
            String key = b.guest.name + " (" + b.guest.nic + ")";
            if (income.containsKey(key)) {
                income.put(key, income.get(key) + b.price);
            } else {
                income.put(key, b.price);
            }
        }
        return income;
    }

    public String topGuests(List<Booking> bookings, int limit) {
        Map<String, Double> income = incomeByGuest(bookings);
        String result = "";
        int shown = 0;
        while (shown < limit && !income.isEmpty()) {
            String best = null;
            double bestValue = -1;
            for (Map.Entry<String, Double> e : income.entrySet()) {
                if (e.getValue() > bestValue) {
                    best = e.getKey();
                    bestValue = e.getValue();
                }
            }
            result = result + (shown + 1) + ". " + best + " - Rs. " + bestValue + "\n";
            income.remove(best);
            shown++;
        }
        return result;
    }

    public String guestHistory(List<Booking> bookings, String nic) {
        String result = "";
        double spent = 0;
        int stays = 0;
        for (Booking b : bookings) {
            if (b.guest.nic.equals(nic)) {
                result = result + b.toString() + "\n";
                if (!b.status.equals("CANCELLED")) {
                    spent = spent + b.price;
                    stays++;
                }
            }
        }
        if (stays == 0) {
            return "No stays for " + nic;
        }
        result = result + "Stays: " + stays + ", spent Rs. " + spent + "\n";
        if (stays > 5) {
            result = result + "Regular guest, offer membership\n";
        }
        return result;
    }

    public void export(List<Booking> bookings, String file) {
        try {
            FileWriter w = new FileWriter(file);
            w.write("id,guest,room,type,nights,price,status\n");
            for (Booking b : bookings) {
                w.write(b.id + "," + b.guest.name + "," + b.roomNo + "," + b.type + "," + b.nights + ","
                        + b.price + "," + b.status + "\n");
            }
            w.close();
        } catch (IOException e) {
            System.out.println("export failed");
        }
    }

    public void exportCancelled(List<Booking> bookings, String file) {
        try {
            FileWriter w = new FileWriter(file);
            w.write("id,guest,room,type,nights,price,status\n");
            for (Booking b : bookings) {
                if (!b.status.equals("CANCELLED")) {
                    continue;
                }
                w.write(b.id + "," + b.guest.name + "," + b.roomNo + "," + b.type + "," + b.nights + ","
                        + b.price + "," + b.status + "\n");
            }
            w.close();
        } catch (IOException e) {
            System.out.println("export failed");
        }
    }
}
