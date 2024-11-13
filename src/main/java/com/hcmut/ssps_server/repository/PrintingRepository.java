package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.model.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PrintingRepository extends JpaRepository<Printing, Long> {
    List<Printing> findByPrinterToPrintID(int printerToPrintID);
    List<Printing> findByExpiredTimeBefore(LocalDateTime currentTime);
    Page<Printing> findAllByPrinterToPrintID(int printerToPrintID, Pageable pageable);
}
