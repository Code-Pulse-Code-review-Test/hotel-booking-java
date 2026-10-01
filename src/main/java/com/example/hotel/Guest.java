package com.example.hotel;

public class Guest {
    public String name;
    public String nic;
    public String phone;
    public String email;
    public boolean member;
    public String address;

    public Guest(String name, String nic, String phone, String email, boolean member) {
        this.name = name;
        this.nic = nic;
        this.phone = phone;
        this.email = email;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Guest other && nic.equals(other.nic);
    }

    @Override
    public int hashCode() {
        return nic.hashCode();
    }
}
