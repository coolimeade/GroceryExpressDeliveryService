package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

public class DronePilot extends Employee {
    private String licenseNumber;
    private int deliveries;
    private String droneId = null;
    public DronePilot(String account, String firstName, String lastName, String phoneNumber, String taxId, int monthsWorking, int salary, String licenseNumber, int deliveries) {
        super(account, firstName, lastName, phoneNumber, taxId, monthsWorking, salary);
        this.licenseNumber = licenseNumber;
        this.deliveries = deliveries;
    }
    public String getLicenseNumber() { return licenseNumber; }
    public int getDeliveries() { return deliveries; }
    public String getDroneId() { return droneId; }
    public void setDroneId(String droneId) { this.droneId = droneId; }
    public void incrementDeliveries() { deliveries += 1; }
}
