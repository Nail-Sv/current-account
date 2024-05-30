package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer getCustomerById(UUID customerId);
    List<Customer> getCustomers();
    void updateCustomerBalance(Customer customer, double balance);

}
