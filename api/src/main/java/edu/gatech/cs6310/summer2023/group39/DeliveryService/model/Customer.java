package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

public class Customer extends User {
    private int rating;
    private int credit;
    private Location location;

    public Customer(String account, String firstName, String lastName, String phoneNumber, int rating, int credit, int lat, int lon) {
        super(account, firstName, lastName, phoneNumber);
        this.rating = rating;
        this.credit = credit;
        this.location = new CustomerLocation(lat, lon, this);
    }

    public int getRating() { return rating; }
    public int getCredit() { return credit; }
    public Location getLocation() { return location; }

    public void deductCredit(int amount) {
        credit -= amount;
    }
}
