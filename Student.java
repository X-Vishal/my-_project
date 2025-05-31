package org.example;

public class Student {
    private int id;
    private String name;
    private String course;
    private double totalFees;
    private double paidFees;
    private double dueFees;
    private String address;
    private String contact;
    private String email;

    public Student(int id, String name, String course, double totalFees,
                   String address, String contact, String email) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.totalFees = totalFees;
        this.paidFees = 0;
        this.dueFees = totalFees;
        this.address = address;
        this.contact = contact;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public double getTotalFees() { return totalFees; }
    public double getPaidFees() { return paidFees; }
    public double getDueFees() { return dueFees; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }

    public void makePayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount.");
        }
        if (amount > dueFees) {
            amount = dueFees;
        }
        paidFees += amount;
        dueFees -= amount;
    }

    @Override
    public String toString() {
        return String.format(
                "Student ID: %d\nName: %s\nCourse: %s\n\nFees Information:\nTotal Fees: %.2f\nPaid Fees: %.2f\nDue Fees: %.2f\n\nContact Information:\nAddress: %s\nContact: %s\nEmail: %s",
                id, name, course, totalFees, paidFees, dueFees, address, contact, email
        );
    }
}