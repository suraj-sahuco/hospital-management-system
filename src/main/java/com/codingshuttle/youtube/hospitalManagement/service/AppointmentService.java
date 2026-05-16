package com.codingshuttle.youtube.hospitalManagement.service;

import com.codingshuttle.youtube.hospitalManagement.dto.AppointmentResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.CreateAppointmentRequestDto;
import com.codingshuttle.youtube.hospitalManagement.entity.Appointment;
import com.codingshuttle.youtube.hospitalManagement.entity.Doctor;
import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
import com.codingshuttle.youtube.hospitalManagement.repository.AppointmentRepository;
import com.codingshuttle.youtube.hospitalManagement.repository.DoctorRepository;
import com.codingshuttle.youtube.hospitalManagement.repository.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    // 🔥 Dependencies
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    // ✅ Manual constructor
    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository,
                              ModelMapper modelMapper) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }

    // =========================================================
    // 🔥 CREATE APPOINTMENT
    // =========================================================
    @Transactional
    @Secured("ROLE_PATIENT")
    public AppointmentResponseDto createNewAppointment(
            CreateAppointmentRequestDto createAppointmentRequestDto) {

        Long doctorId = createAppointmentRequestDto.getDoctorId();
        Long patientId = createAppointmentRequestDto.getPatientId();

        // 🔍 Step 1: Patient fetch
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Patient not found with ID: " + patientId));

        // 🔍 Step 2: Doctor fetch
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        // =====================================================
        // ❌ OLD (builder - remove)
        // Appointment.builder() ❌
        // =====================================================

        // =====================================================
        // ✅ NEW (manual object creation)
        // =====================================================
        Appointment appointment = new Appointment();

        appointment.setReason(createAppointmentRequestDto.getReason());
        appointment.setAppointmentTime(createAppointmentRequestDto.getAppointmentTime());

        // 🔥 relations set karna (VERY IMPORTANT)
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        // 🔥 bidirectional consistency
        patient.getAppointments().add(appointment);

        // 💾 save to DB
        appointment = appointmentRepository.save(appointment);

        // 🔁 return DTO
        return modelMapper.map(appointment, AppointmentResponseDto.class);
    }

    // =========================================================
    // 🔥 REASSIGN APPOINTMENT
    // =========================================================
    @Transactional
    @PreAuthorize("hasAuthority('appointment:write') or #doctorId == authentication.principal.id")
    public Appointment reAssignAppointmentToAnotherDoctor(Long appointmentId, Long doctorId) {

        // 🔍 Step 1: fetch appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow();

        // 🔍 Step 2: fetch doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow();

        // 🔥 update doctor
        appointment.setDoctor(doctor);

        // 🔥 maintain relation
        doctor.getAppointments().add(appointment);

        return appointment;
    }

    // =========================================================
    // 🔥 GET ALL APPOINTMENTS OF DOCTOR
    // =========================================================
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('DOCTOR') AND #doctorId == authentication.principal.id)")
    public List<AppointmentResponseDto> getAllAppointmentsOfDoctor(Long doctorId) {

        // 🔍 fetch doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow();

        // 🔁 convert to DTO
        return doctor.getAppointments()
                .stream()
                .map(appointment ->
                        modelMapper.map(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
    }
}