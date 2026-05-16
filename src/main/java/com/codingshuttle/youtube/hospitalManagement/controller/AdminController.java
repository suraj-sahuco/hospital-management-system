package com.codingshuttle.youtube.hospitalManagement.controller;

import com.codingshuttle.youtube.hospitalManagement.dto.DoctorResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.OnboardDoctorRequestDto;
import com.codingshuttle.youtube.hospitalManagement.dto.PatientResponseDto;
import com.codingshuttle.youtube.hospitalManagement.service.DoctorService;
import com.codingshuttle.youtube.hospitalManagement.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
// @RequiredArgsConstructor ❌ (Lombok removed - manual constructor used)
public class AdminController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    // ✅ MANUAL CONSTRUCTOR (Lombok @RequiredArgsConstructor ka replacement)
    // 👉 Ye constructor Spring ko batata hai ki dependencies kaise inject karni hain
    public AdminController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(patientService.getAllPatients(pageNumber, pageSize));
    }

    @PostMapping("/onBoardNewDoctor")
    public ResponseEntity<DoctorResponseDto> onBoardNewDoctor(//yaha DoctorResponseDto ka field mai set ho ke data client  ko jayega
           @Valid @RequestBody OnboardDoctorRequestDto onboardDoctorRequestDto) {
// yaha onboardDoctorrequestDto ka kaam client se data laana
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.onBoardNewDoctor(onboardDoctorRequestDto));
        //body ke andar data hoga jaise .body({"name":"suraj"}) waise
    }
}