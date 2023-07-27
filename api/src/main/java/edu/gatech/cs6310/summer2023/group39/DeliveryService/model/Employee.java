package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

public class Employee extends User {
    private String taxId;
    private int monthsWorking;
    private int salary;
    public Employee(String account, String firstName, String lastName, String phoneNumber, String taxId, int monthsWorking, int salary) {
        super(account, firstName, lastName, phoneNumber);
        this.taxId = taxId;
        this.monthsWorking = monthsWorking;
        this.salary = salary;
    }
    public String getTaxId() { return taxId; }
    public int getMonthsWorking() { return monthsWorking; }
    public int getSalary() { return salary; }
}
