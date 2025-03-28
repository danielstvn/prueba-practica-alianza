package com.alianza.test.cm.repository;

import com.alianza.test.cm.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> , JpaSpecificationExecutor<Client> {
}
