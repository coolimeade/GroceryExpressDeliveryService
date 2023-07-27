package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.Customer;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.CustomerExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.CustomerService;

@CrossOrigin(origins = "*")
@RestController
public class CustomerApiController {

    private CustomerService customerService;

    @Autowired
    public CustomerApiController(CustomerService deliveryService) {
        this.customerService = deliveryService;
    }

    public static class NewCustomerRequest {
        private String account;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private int rating;
        private int credit;
        private int lat;
        private int lon;
        public String getAccount() { return account; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhoneNumber() { return phoneNumber; }
        public int getRating() { return rating; }
        public int getCredit() { return credit; }
        public int getLat() { return lat; }
        public int getLon() { return lon; }
    }

    @PostMapping("/customers")
    public void makeCustomer(@RequestBody NewCustomerRequest request)
            throws CustomerExistsException
    {
        customerService.makeCustomer(request.getAccount(), request.getFirstName(),
                request.getLastName(), request.getPhoneNumber(), request.getRating(),
                request.getCredit(), request.getLat(), request.getLon());
    }

    @GetMapping("/customers")
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        customerService.forEachCustomer((customer) -> {
            customers.add(customer);
        });
        return customers;
    }
}