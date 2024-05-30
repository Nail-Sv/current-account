package com.currentaccount.currentaccount.service;

import com.currentaccount.currentaccount.exception.NotFoundException;
import com.currentaccount.currentaccount.model.Customer;
import com.currentaccount.currentaccount.repository.CustomerRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {


    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customer = Customer.builder()
                .id(customerId)
                .name("John")
                .surname("Doe")
                .balance(500.0)
                .build();
    }

    @Test
    void testGetCustomerById_shouldReturnCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomerById(customerId);

        assertNotNull(foundCustomer);
        assertEquals(customerId, foundCustomer.getId());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testGetCustomerById_ShouldReturnNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            customerService.getCustomerById(customerId);
        });

        String expectedMessage = "Customer with id '%s' does not exist".formatted(customerId);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testGetCustomers_ShouldReturnAllCustomers() {
        val anotherCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .name("Jane")
                .surname("Smith")
                .balance(300.0)
                .build();

        List<Customer> customers = List.of(customer, anotherCustomer);

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> foundCustomers = customerService.getCustomers();

        assertEquals(2, foundCustomers.size());
        assertTrue(foundCustomers.contains(customer));
        assertTrue(foundCustomers.contains(anotherCustomer));
        verify(customerRepository).findAll();
    }

    @Test
    void testUpdateCustomerBalance_shouldUpdateCustomerBalance() {
        double additionalBalance = 100.0;
        double expectedBalance = customer.getBalance() + additionalBalance;

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.updateCustomerBalance(customer, additionalBalance);

        assertEquals(expectedBalance, customer.getBalance());
        verify(customerRepository).save(customer);
    }
}
