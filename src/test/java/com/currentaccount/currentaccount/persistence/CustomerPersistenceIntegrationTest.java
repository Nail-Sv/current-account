package com.currentaccount.currentaccount.persistence;

import com.currentaccount.currentaccount.model.Customer;
import com.currentaccount.currentaccount.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest()
@ActiveProfiles("test")
public class CustomerPersistenceIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void getCustomerById_shouldReturnCustomer() {

        UUID customerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        Customer actualCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AssertionError("Customer not found"));

        assertThat(actualCustomer.getId()).isEqualTo(customerId);
    }

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void getCustomers_shouldReturnAllCustomers() {

        List<Customer> actualCustomers = customerRepository.findAll();

        assertThat(actualCustomers).isNotEmpty();
    }

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void updateCustomerBalance_shouldUpdateBalance() {

        UUID customerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AssertionError("Customer not found"));

        double initialBalance = customer.getBalance();

        double balanceToAdd = 50.0;

        customer.setBalance(initialBalance + balanceToAdd);

        customerRepository.save(customer);

        Customer updatedCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AssertionError("Customer not found after update"));

        assertThat(updatedCustomer.getBalance()).isEqualTo(initialBalance + balanceToAdd);
    }
}
