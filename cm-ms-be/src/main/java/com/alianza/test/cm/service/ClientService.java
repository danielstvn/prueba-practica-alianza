package com.alianza.test.cm.service;

import com.alianza.test.cm.model.Client;
import com.alianza.test.cm.service.criteria.ClientCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link Client} entities.
 */
public interface ClientService {

    /**
     * Get all clients.
     *
     * @return the list of entities
     */
    List<Client> findAll();

    /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Client> findById(UUID id);

    /**
     * Save a client.
     *
     * @param client the entity to save
     * @return the persisted entity
     */
    Client save(Client client);

    /**
     * Update a client.
     *
     * @param client the entity to update
     * @return the updated entity
     */
    Client update(Client client) throws Exception;

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity
     */
    void deleteById(UUID id);

    /**
     * Find clients by shared key.
     *
     * @param sharedKey the shared key to search
     * @return the list of entities with the given shared key
     */
    List<Client> findBySharedKey(String sharedKey);

    /**
     * Find all clients with pagination and filtering.
     *
     * @param criteria the filtering criteria
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Client> findByCriteria(ClientCriteria criteria, Pageable pageable);

    /**
     * Export all clients to CSV format.
     *
     * @return byte array with CSV data
     * @throws IOException if an I/O error occurs during CSV generation
     */
    byte[] exportClientsToCSV() throws IOException;
}
