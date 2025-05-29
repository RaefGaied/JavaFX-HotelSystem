package org.example.demo.model;

import java.util.Objects;

public class Customer {
    private String customerIDNumber;
    private String customerName;
    private String customerNationality;
    private String customerGender;
    private String customerPhoneNo;
    private String customerEmail;

    public Customer() {
    }

    public Customer(String customerIDNumber, String customerName, String customerNationality, String customerGender, String customerPhoneNo, String customerEmail) {
        this.customerIDNumber = customerIDNumber;
        this.customerName = customerName;
        this.customerNationality = customerNationality;
        this.customerGender = customerGender;
        this.customerPhoneNo = customerPhoneNo;
        this.customerEmail = customerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(customerIDNumber, customer.customerIDNumber) && Objects.equals(customerName, customer.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerIDNumber, customerName);
    }

    public String getCustomerIDNumber() {
        return customerIDNumber;
    }

    public void setCustomerIDNumber(String customerIDNumber) {
        this.customerIDNumber = customerIDNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
