package org.example;

public class PaymentRecord {
    private int studentId;
    private double amount;
    private String mode;
    private String date;

    public PaymentRecord(int studentId, double amount, String mode, String date) {
        this.studentId = studentId;
        this.amount = amount;
        this.mode = mode;
        this.date = date;
    }

    // Getters
    public int getStudentId() { return studentId; }
    public double getAmount() { return amount; }
    public String getMode() { return mode; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return String.format("Date: %s, Amount: %.2f, Mode: %s", date, amount, mode);
    }
}