package com.hcmut.ssps_server.enums;

public enum PrintableStatus {
    PRINTABLE,                      // Tài liệu có thể in
    UNSUPPORTED_FILE_TYPE,          // Loại tài liệu không được máy in hỗ trợ
    PRINTER_NOT_HAVE_ENOUGH_PAPERS,  // Máy in không đủ giấy
    STUDENT_NOT_HAVE_ENOUGH_PAGES,  // Sinh viên in không đủ giấy
    PRINTER_NOT_FOUND              //Không tồn tại máy in
}
