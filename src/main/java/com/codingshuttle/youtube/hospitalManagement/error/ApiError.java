package com.codingshuttle.youtube.hospitalManagement.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class ApiError {

    private LocalDateTime timeStamp;
    private String error;
    private HttpStatus statusCode;

    // ✅ Validation errors field
    private Map<String, String> validationErrors;

    public ApiError() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus statusCode) {
        this();
        this.error = error;
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    // ✅ Getter
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    // ✅ Setter
    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "timeStamp=" + timeStamp +
                ", error='" + error + '\'' +
                ", statusCode=" + statusCode +
                ", validationErrors=" + validationErrors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiError apiError = (ApiError) o;

        return Objects.equals(timeStamp, apiError.timeStamp) &&
                Objects.equals(error, apiError.error) &&
                statusCode == apiError.statusCode &&
                Objects.equals(validationErrors, apiError.validationErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, error, statusCode, validationErrors);
    }
}