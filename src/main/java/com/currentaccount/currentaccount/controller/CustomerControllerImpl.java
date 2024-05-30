package com.currentaccount.currentaccount.controller;

import com.currentaccount.currentaccount.dto.CustomerDTO;
import com.currentaccount.currentaccount.mapper.CustomerMapper;
import com.currentaccount.currentaccount.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerControllerImpl implements CustomerController{

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Override
    public ResponseEntity<CustomerDTO> getCustomer(UUID customerId) {

        log.debug("Received request to get customer with id {}", customerId);
        val customer = customerService.getCustomerById(customerId);

        return new ResponseEntity<>(customerMapper.toDto(customer), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        log.info("Received request to get all customers" );
        return new ResponseEntity<>(customerMapper.toDtos(customerService.getCustomers()), HttpStatus.OK);
    }
}
