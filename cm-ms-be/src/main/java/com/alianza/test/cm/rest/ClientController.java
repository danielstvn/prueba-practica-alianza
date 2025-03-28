package com.alianza.test.cm.rest;

import com.alianza.test.cm.model.Client;
import com.alianza.test.cm.service.ClientService;
import com.alianza.test.cm.service.criteria.ClientCriteria;
import com.alianza.test.cm.service.impl.ClientServiceImpl;
import com.alianza.test.cm.service.utils.PaginationUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Client resources.
 */

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * {@code POST /api/client} : Create a new client.
     *
     * @param client the client to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new client,
     * or with status {@code 400 (Bad Request)} if the client ID is already specified or request is invalid
     */
    @PostMapping
    public ResponseEntity<Client> create(@RequestBody @Valid Client client) {
        try {
            if (client.getId() != null) {
                throw new Exception("Parameter is not required");
            }
            Client newclient = this.clientService.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(newclient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * {@code PUT /api/client} : Updates an existing client.
     *
     * @param client the client to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated client,
     * or with status {@code 400 (Bad Request)} if the client ID is not provided or request is invalid
     */
    @PutMapping
    public ResponseEntity<Client> update(@RequestBody @Valid Client client) {
        try {
            if (client.getId() == null) {
                throw new Exception("Parameter required");
            }
            Client updClient = this.clientService.update(client);
            return ResponseEntity.ok().body(updClient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * {@code GET /api/client/:id} : Get a client by ID.
     *
     * @param id the ID of the client to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the client,
     * or with status {@code 404 (Not Found)} if the client is not found,
     * or with status {@code 400 (Bad Request)} if the ID is null or invalid
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable UUID id) {
        try {
            if (id == null) {
                throw new Exception("Parameter required");
            }
            Optional<Client> client = this.clientService.findById(id);
            return client
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * {@code GET /api/client/shared-key/:shared} : Get a client by shared key.
     *
     * @param shared the shared key of the client to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the client,
     * or with status {@code 204 (No Content)} if the client is not found or there was an error
     */
    @GetMapping("/shared-key/{shared}")
    public ResponseEntity<?> findBySharedKey(@PathVariable String shared) {
        try {
            return ResponseEntity.ok(this.clientService.findBySharedKey(shared));
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * {@code GET /api/client} : Get all clients with pagination and filtering.
     *
     * @param criteria the criteria to filter clients
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of clients,
     * or with status {@code 400 (Bad Request)} if the request is invalid
     */
    @GetMapping
    public ResponseEntity<List<Client>> findAll(ClientCriteria criteria, Pageable pageable) {
        try {
            Page<Client> clients = this.clientService.findByCriteria(criteria, pageable);

            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), clients);
            return ResponseEntity.ok().headers(headers).body(clients.getContent());

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * {@code DELETE /api/client/:id} : Delete a client by ID.
     *
     * @param id the ID of the client to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)},
     * or with status {@code 400 (Bad Request)} if the ID is null or invalid
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Client> delete(@PathVariable UUID id) {
        try {
            if (id == null) {
                throw new Exception("Parameter required");
            }
            this.clientService.deleteById(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * {@code GET /api/client/export} : Export all clients to CSV format.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the CSV file data,
     * or with status {@code 400 (Bad Request)} if there was an error during export
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportClientsToCsv() {
        try {
            byte[] csvData = clientService.exportClientsToCSV();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients.csv");
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(csvData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
