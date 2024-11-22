package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.dto.request.PrinterCreationRequest;
import com.hcmut.ssps_server.dto.request.UploadConfigRequest;
import com.hcmut.ssps_server.enums.PrintableStatus;
import com.hcmut.ssps_server.model.Printer;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IPrinterService {
    Printer addPrinter(PrinterCreationRequest request);
    Printer updatePrinter(Long printerId, Map<String, Object> updates);
    Printer getPrinter(Long printerId);
    List<Printer> getAllPrinters(Pageable pageable);
    void deletePrinter(Long printerId);
    void enablePrinter(Long printerId);
    void disablePrinter(Long printerId);
    String print(int printerId);
    PrintableStatus isPrintable(MultipartFile file, UploadConfigRequest uploadConfigRequest) throws IOException;
    List<Printer> findMatchPrinters(List<String> requiredDocumentType, Pageable pageable);
}
