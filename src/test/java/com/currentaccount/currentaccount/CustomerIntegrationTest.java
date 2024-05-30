package com.currentaccount.currentaccount;

import com.currentaccount.currentaccount.controller.CustomerController;
import com.currentaccount.currentaccount.dto.CustomerDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void getCustomer_shouldReturnCustomer() throws IOException {

        // ids are taken from the db init script for tests: '/db/init.sql'
        CustomerDTO actualCustomerDTO = testRestTemplate.getForObject(
                CustomerController.REST_URL+ "/550e8400-e29b-41d4-a716-446655440000",
                CustomerDTO.class);

        CustomerDTO expectedCustomerDTO = objectMapper.readValue(
                new File("src/test/resources/integration_tests/expectedCustomer.json"), CustomerDTO.class);

        AssertionsForClassTypes.assertThat(actualCustomerDTO).isNotNull();
        assertThat(actualCustomerDTO.id()).isNotNull();

        AssertionsForClassTypes.assertThat(actualCustomerDTO)
                .usingRecursiveComparison()
                        .isEqualTo(expectedCustomerDTO);

    }

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void getAllCustomers_shouldReturnAllCustomers() throws IOException {

        String actualCustomerDTO = testRestTemplate.getForObject(
                CustomerController.REST_URL+ "/",
                String.class);

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, CustomerDTO.class);

        List<CustomerDTO> actualCustomersDTO = objectMapper.readValue(actualCustomerDTO, javaType);

        List<CustomerDTO> expectedCustomersDTO = objectMapper.readValue(
                new File("src/test/resources/integration_tests/expectedCustomers.json"), javaType);

        assertThat(actualCustomersDTO).isNotNull();

        assertThat(actualCustomersDTO)
                .usingRecursiveComparison()
                .isEqualTo(expectedCustomersDTO);

    }

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/findAllCustomers.sql"})})
    void getAllCustomers_shouldReturnAllCustomersAndShowTheirCredit() throws IOException {

        String actualCustomerDTO = testRestTemplate.getForObject(
                CustomerController.REST_URL+ "/",
                String.class);

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, CustomerDTO.class);

        List<CustomerDTO> actualCustomersDTO = objectMapper.readValue(actualCustomerDTO, javaType);

        List<CustomerDTO> expectedCustomersDTO = objectMapper.readValue(
                new File("src/test/resources/integration_tests/expectedCustomersWithCredit.json"), javaType);

        assertThat(actualCustomersDTO).isNotNull();

        assertThat(actualCustomersDTO)
                .usingRecursiveComparison()
                .ignoringFields("accounts.transactions.created_at")
                .isEqualTo(expectedCustomersDTO);

    }
}
