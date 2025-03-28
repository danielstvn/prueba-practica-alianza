package com.alianza.test.cm.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alianza.test.cm.constant.ClientConstantTest;
import com.alianza.test.cm.model.Client;
import com.alianza.test.cm.repository.ClientRepository;
import com.alianza.test.cm.service.impl.ClientServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    private static Logger log = LoggerFactory.getLogger(ClientControllerTest.class);

    @Autowired
    private MockMvc restMockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    /**
     * Create a Client entity for this test
     * @return Client
     */
    public static Client createEntity(){
        Client client = new Client();
        client.setSharedKey(ClientConstantTest.DEFAULT_SHARED_KEY);
        client.setName(ClientConstantTest.DEFAULT_NAME);
        client.setEmail(ClientConstantTest.DEFAULT_EMAIL);
        client.setPhone(ClientConstantTest.DEFAULT_PHONE);
        client.setStartDate(ClientConstantTest.DEFAULT_START_DATE);
        client.setEndDate(ClientConstantTest.DEFAULT_END_DATE);

        return client;
    }

    @BeforeEach
    public void initTest(){
        log.info("Init test, create client entity");
        client = createEntity();
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        log.info("Test create client");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // create client
        restMockMvc.perform(post("/api/client")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsBytes(client)))
                .andExpect(status().isCreated());

        // validate client in database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);

        Client testClient = clientList.get(clientList.size() -1);

        Assertions.assertThat(testClient.getSharedKey()).isEqualTo(ClientConstantTest.DEFAULT_SHARED_KEY);
        Assertions.assertThat(testClient.getName()).isEqualTo(ClientConstantTest.DEFAULT_NAME);
        Assertions.assertThat(testClient.getEmail()).isEqualTo(ClientConstantTest.DEFAULT_EMAIL);
        Assertions.assertThat(testClient.getPhone()).isEqualTo(ClientConstantTest.DEFAULT_PHONE);
        Assertions.assertThat(testClient.getStartDate()).isEqualTo(ClientConstantTest.DEFAULT_START_DATE);
        Assertions.assertThat(testClient.getEndDate()).isEqualTo(ClientConstantTest.DEFAULT_END_DATE);

    }

    @Test
    @Transactional
    public void createClientWhitExistingId() throws Exception {
        log.info("Test create client whit existing id");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // create user
        client.setId(UUID.randomUUID());
        restMockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(client)))
                .andExpect(status().isBadRequest());

        // validate client in database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        log.info("Test check name is required");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // create client
        client.setName(null);
        restMockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(client)))
                .andExpect(status().isBadRequest());

        // Validate client in the database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        log.info("Test check email is required");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // create client
        client.setEmail(null);
        restMockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(client)))
                .andExpect(status().isBadRequest());

        // Validate client in the database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        log.info("Test check phone is required");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // create client
        client.setPhone(null);
        restMockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(client)))
                .andExpect(status().isBadRequest());

        // Validate client in the database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        log.info("Test check start date is required");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // create client
        client.setStartDate(null);
        restMockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(client)))
                .andExpect(status().isBadRequest());

        // Validate client in the database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        log.info("Test check end date is required");

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // create client
        client.setEndDate(null);
        restMockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(client)))
                .andExpect(status().isBadRequest());

        // Validate client in the database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate);

    }


    @Test
    @Transactional
    public void getAllClients() throws Exception {
        log.info("Test get all clients");

        clientRepository.saveAndFlush(client);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        // get all clients
        restMockMvc.perform(get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(CoreMatchers.hasItem(client.getId().toString())))
                .andExpect(jsonPath("$.[*].sharedKey").value(CoreMatchers.hasItem(client.getSharedKey())))
                .andExpect(jsonPath("$.[*].name").value(CoreMatchers.hasItem(client.getName())))
                .andExpect(jsonPath("$.[*].email").value(CoreMatchers.hasItem(client.getEmail())))
                .andExpect(jsonPath("$.[*].phone").value(CoreMatchers.hasItem(client.getPhone())))
                .andExpect(jsonPath("$.[*].startDate").value(CoreMatchers.hasItem(client.getStartDate().format(formatter))))
                .andExpect(jsonPath("$.[*].endDate").value(CoreMatchers.hasItem(client.getEndDate().format(formatter))))
                .andExpect(jsonPath("$.[*].dateAdded").value(CoreMatchers.hasItem(client.getDataAdded().format(formatter))));

    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        log.info("Test get non existing client");
        // get client
        restMockMvc.perform(get("/api/client/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());

    }


    @Test
    @Transactional
    public void updateClient() throws Exception {
        log.info("Test update client");

        clientRepository.saveAndFlush(client);
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        Client updateClient = clientRepository.findById(client.getId()).get();

        entityManager.detach(updateClient);

        updateClient.setSharedKey(ClientConstantTest.UPDATE_SHARED_KEY);
        updateClient.setName(ClientConstantTest.UPDATE_NAME);
        updateClient.setEmail(ClientConstantTest.UPDATE_EMAIL);
        updateClient.setPhone(ClientConstantTest.UPDATE_PHONE);
        updateClient.setStartDate(ClientConstantTest.UPDATE_START_DATE);
        updateClient.setEndDate(ClientConstantTest.UPDATE_END_DATE);

        restMockMvc.perform(put("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(updateClient)))
                .andExpect(status().isOk());

        // validate client in database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate);

        Client testClient = clientList.get(databaseSizeBeforeCreate - 1);

        Assertions.assertThat(testClient.getSharedKey()).isEqualTo(ClientConstantTest.UPDATE_SHARED_KEY);
        Assertions.assertThat(testClient.getName()).isEqualTo(ClientConstantTest.UPDATE_NAME);
        Assertions.assertThat(testClient.getEmail()).isEqualTo(ClientConstantTest.UPDATE_EMAIL);
        Assertions.assertThat(testClient.getPhone()).isEqualTo(ClientConstantTest.UPDATE_PHONE);
        Assertions.assertThat(testClient.getStartDate()).isEqualTo(ClientConstantTest.UPDATE_START_DATE);
        Assertions.assertThat(testClient.getEndDate()).isEqualTo(ClientConstantTest.UPDATE_END_DATE);

    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        log.info("Test delete client");

        clientRepository.saveAndFlush(client);
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        restMockMvc.perform(delete("/api/client/{id}", client.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // validate client in database
        List<Client> clientList = clientRepository.findAll();
        Assertions.assertThat(clientList).hasSize(databaseSizeBeforeCreate - 1);

    }
}
