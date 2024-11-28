package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.model.Printer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrinterRepository extends JpaRepository<Printer, Long> {
    @Query("SELECT p FROM Printer p " +
            "JOIN p.availableDocType adt " +
            "WHERE adt IN :requiredDocumentType " +
            "GROUP BY p.printerID " +
            "HAVING COUNT(adt) = :requiredDocumentTypeSize")
    Page<Printer> findByAvailableDocTypeContainingAll(List<String> requiredDocumentType, int requiredDocumentTypeSize, Pageable pageable);

    Boolean existsByPrinterID(int printerID);
}
