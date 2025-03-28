package com.alianza.test.cm.service.impl;

import com.alianza.test.cm.model.Client;
import com.alianza.test.cm.repository.ClientRepository;
import com.alianza.test.cm.service.ClientService;
import com.alianza.test.cm.service.criteria.ClientCriteria;
import com.alianza.test.cm.service.utils.DocumentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Get all clients.
     *
     * @return the list of entities
     */
    @Override
    public List<Client> findAll() {
        log.debug("Find all clients");
        return this.clientRepository.findAll();
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Client> findById(UUID id) {
        log.debug("Find client by id {}", id);
        return this.clientRepository.findById(id);
    }

    /**
     * Save a client.
     *
     * @param client the entity to save
     * @return the persisted entity
     */
    @Override
    public Client save(Client client) {
        log.debug("Save entity client {}", client);
        return this.clientRepository.save(client);
    }

    /**
     * Update a client.
     *
     * @param client the entity to update
     * @return the updated entity
     */
    @Override
    public Client update(Client client) throws Exception {
        log.debug("update entity client {}", client);

        Optional<Client> optClient = this.findById(client.getId());

        if (optClient.isEmpty()) {
            throw new Exception("Record not found");
        }

        return this.clientRepository.save(client);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity
     */
    public void deleteById(UUID id) {
        log.debug("Delete client by id {}", id);

        this.clientRepository.deleteById(id);
    }

    /**
     * Find clients by shared key.
     *
     * @param sharedKey the shared key to search
     * @return the list of entities with the given shared key
     */
    @Override
    public List<Client> findBySharedKey(String sharedKey) {
        log.debug("find clients by shared key {}", sharedKey);

        ClientCriteria criteria = new ClientCriteria();
        criteria.setSharedKey(sharedKey);
        return clientRepository.findAll(buildSpecificationCriteria(criteria));
    }

    /**
     * Find all clients with pagination and filtering.
     *
     * @param criteria the filtering criteria
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Client> findByCriteria(ClientCriteria criteria, Pageable pageable) {
        log.debug("Find clients by criteria {} and pageable", criteria, pageable);

        return clientRepository.findAll(buildSpecificationCriteria(criteria), pageable);

    }

    /**
     * Export all clients to CSV format.
     *
     * @return byte array with CSV data
     * @throws IOException if an I/O error occurs during CSV generation
     */
    @Override
    public byte[] exportClientsToCSV() throws IOException {
        log.debug("Export clients to csv");

        List<Client> clients = findAll();
        return DocumentProcessor.generateClientsCsv(clients);
    }

    /**
     * Builds a JPA Specification for filtering Client entities based on the provided criteria.
     * This method creates dynamic queries using Spring Data JPA Specifications.
     *
     * @param criteria the filtering criteria containing the filter values
     * @return the Specification object used for JPA queries with the applied filters
     */
    private Specification<Client> buildSpecificationCriteria(ClientCriteria criteria) {
        Specification<Client> specification = Specification.where(null);

        if (criteria == null) {
            return specification;
        }

        if (criteria.getSharedKey() != null && !criteria.getSharedKey().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("sharedKey")),
                            "%" + criteria.getSharedKey().toLowerCase() + "%"
                    )
            );
        }

        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + criteria.getName().toLowerCase() + "%"
                    )
            );
        }

        if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("email")),
                            "%" + criteria.getEmail().toLowerCase() + "%"
                    )
            );
        }

        if (criteria.getPhone() != null && !criteria.getPhone().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("phone")),
                            "%" + criteria.getPhone().toLowerCase() + "%"
                    )
            );
        }

        if (criteria.getStartDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(
                            root.get("startDate"),
                            criteria.getStartDate()
                    )
            );
        }

        if (criteria.getEndDate() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("endDate"),
                            criteria.getEndDate()
                    )
            );
        }

        return specification;
    }

}
