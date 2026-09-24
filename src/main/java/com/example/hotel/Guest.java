package com.example.hotel;

public class Guest {
    public String name;
    public String nic;
    public String phone;
    public String email;
    public boolean member;

    public Guest(String name, String nic, String phone, String email, boolean member) {
        this.name = name;
        this.nic = nic;
        this.phone = phone;
        this.email = email;
        this.member = member;
    }

    public boolean equals(Guest other) {
        return other != null && nic.equals(other.nic);
    }
}
