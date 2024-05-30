package com.currentaccount.currentaccount;

import com.currentaccount.currentaccount.controller.AccountController;
import com.currentaccount.currentaccount.dto.AccountDTO;
import com.currentaccount.currentaccount.dto.ErrorGroupDTO;
import com.currentaccount.currentaccount.dto.InitialRequestDTO;
import com.currentaccount.currentaccount.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
public class AccountIntegrationTest {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void createAccount_shouldCreateNewAccount() throws IOException {

        InitialRequestDTO initialRequestDTO = objectMapper.readValue(
                new File("src/test/resources/integration_tests/initialRequest.json"), InitialRequestDTO.class
        );

        ResponseEntity<AccountDTO> responseEntity = testRestTemplate.postForEntity(
                AccountController.REST_URL, initialRequestDTO, AccountDTO.class);

        String locationHeader = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).toString();

        String accountId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        assertThat(accountId).isNotEmpty();
        assertThat(locationHeader).isNotNull();
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        AssertionsForClassTypes.assertThat(accountRepository.findById(UUID.fromString(accountId))).isPresent();

        entityManager.clear();
    }

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void createAccount_shouldReturnStatusCreated() throws IOException {

        InitialRequestDTO initialRequestDTO = objectMapper.readValue(
                new File("src/test/resources/integration_tests/initialRequest.json"), InitialRequestDTO.class
        );

        ResponseEntity<AccountDTO> responseEntity = testRestTemplate.postForEntity(
                AccountController.REST_URL, initialRequestDTO, AccountDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        entityManager.clear();
    }

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void handleHttpMediaTypeNotSupportedException_shouldReturnStatusUnsupportedMediaType() throws IOException {

        InitialRequestDTO initialRequestDTO = objectMapper.readValue(
                new File("src/test/resources/integration_tests/initialRequest.json"), InitialRequestDTO.class
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  // Use a supported media type for the content
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));  // Set an unsupported media type for the accept header

        HttpEntity<InitialRequestDTO> requestEntity = new HttpEntity<>(initialRequestDTO, headers);

        ResponseEntity<ErrorGroupDTO> responseEntity = testRestTemplate.postForEntity(
                AccountController.REST_URL, requestEntity, ErrorGroupDTO.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().errors()).isNotEmpty();
        AssertionsForClassTypes.assertThat(responseEntity.getBody().errors().get(0).message())
                .contains("media type is not supported. Supported media types are:");

        entityManager.clear();
    }

    @Test
    @SqlGroup(value = {@Sql(scripts = {"/db/init.sql"})})
    void createAccount_shouldReturnStatusBadRequest_whenInitialCreditMissing() throws IOException {

        InitialRequestDTO initialRequestDTO = objectMapper.readValue(
                new File("src/test/resources/integration_tests/createAccount_shouldReturnBadRequest/badInitialRequest.json"),
                InitialRequestDTO.class
        );

        ResponseEntity<ErrorGroupDTO> responseEntity = testRestTemplate.postForEntity(
                AccountController.REST_URL, initialRequestDTO, ErrorGroupDTO.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().errors()).isNotEmpty();
        AssertionsForClassTypes.assertThat(responseEntity.getBody().errors().get(0).message())
                .contains("Initial credit amount cannot be zero");

        entityManager.clear();

    }

}
