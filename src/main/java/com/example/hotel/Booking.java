package com.example.hotel;

import java.util.*;
import java.io.*;

public class Booking {
    public int id;
    public Guest guest;
    public String roomNo;
    public String type;
    public int nights;
    public int people;
    public double price;
    public String status;
    public boolean breakfast;

    public Booking(int id, Guest guest, String roomNo, String type, int nights, int people, boolean breakfast) {
        this.id = id;
        this.guest = guest;
        this.roomNo = roomNo;
        this.type = type;
        this.nights = nights;
        this.people = people;
        this.breakfast = breakfast;
        this.status = "BOOKED";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        Booking b = (Booking) o;
        return b.id == id;
    }

    @Override
    public String toString() {
        return id + " " + guest.name + " " + roomNo + " " + type + " " + nights + " nights " + status;
    }
}
