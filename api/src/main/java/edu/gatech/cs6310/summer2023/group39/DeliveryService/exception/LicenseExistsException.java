package edu.gatech.cs6310.summer2023.group39.DeliveryService.exception;

public  class LicenseExistsException extends DeliveryServiceException {
    public String licenseNumber;
    public LicenseExistsException(String licenseNumber) {
        super(String.format("A pilot with the license number %s already exists", licenseNumber));
        this.licenseNumber = licenseNumber;
    }
}
