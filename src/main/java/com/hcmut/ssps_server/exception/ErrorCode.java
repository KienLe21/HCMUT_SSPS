package com.hcmut.ssps_server.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),
    USER_EXISTED(1002, "User existed"),
    UNAUTHENTICATED(1003, "Unauthenticated"),
    PASSWORD_INVALID(1004, "Password invalid"),
    PRINTER_NOT_FOUND(1005, "Printer not found"),
    PRINT_REQUEST_NOT_FOUND(1006, "Print request not found"),
    PRINTING_LOG_ID_NOT_FOUND(1006, "PrintingLogId request not found");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
