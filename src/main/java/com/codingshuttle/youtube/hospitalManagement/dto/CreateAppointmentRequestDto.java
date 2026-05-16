package com.codingshuttle.youtube.hospitalManagement.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

//@Data
public class CreateAppointmentRequestDto {
    @NotNull(message = "Doctor id cannot be null")
    private Long doctorId;

    @NotNull(message = "Patient id cannot be null")
    private Long patientId;

    @NotNull(message = "Appointment time cannot be null")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    @NotBlank(message = "Reason cannot be empty")
    @Size(min = 5, max = 200, message = "Reason must be between 5 and 200 characters")
    private String reason;

    public CreateAppointmentRequestDto(Long doctorId, Long patientId, LocalDateTime appointmentTime, String reason) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CreateAppointmentRequestDto{" +
                "doctorId=" + doctorId +
                ", patientId=" + patientId +
                ", appointmentTime=" + appointmentTime +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAppointmentRequestDto that = (CreateAppointmentRequestDto) o;
        return Objects.equals(doctorId, that.doctorId) && Objects.equals(patientId, that.patientId) && Objects.equals(appointmentTime, that.appointmentTime) && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, patientId, appointmentTime, reason);
    }
}
