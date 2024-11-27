package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.model.Document;
import com.hcmut.ssps_server.model.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPrintingService {
    void addPrintRequest(Document document, int printerId);
    Page<Printing> getPrintRequestsByPrinterId(int printerId, Pageable pageable);
    Page<Printing> getAllPrintRequests(Pageable pageable);
}
