package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.exception.NotFoundException;
import com.currentaccount.currentaccount.model.Customer;
import com.currentaccount.currentaccount.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    @Override
    public Customer getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer with id '%s' does not exist".formatted(customerId)));
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void updateCustomerBalance(Customer customer, double balance) {
        val updatedCustomer = customer;
        updatedCustomer.setBalance(updatedCustomer.getBalance() + balance);
        customerRepository.save(updatedCustomer);
    }

}
