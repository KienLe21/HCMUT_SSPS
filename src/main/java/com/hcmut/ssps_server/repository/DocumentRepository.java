package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
