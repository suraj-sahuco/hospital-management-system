package com.codingshuttle.youtube.hospitalManagement.service;

import com.codingshuttle.youtube.hospitalManagement.dto.DoctorResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.OnboardDoctorRequestDto;
import com.codingshuttle.youtube.hospitalManagement.entity.Doctor;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import com.codingshuttle.youtube.hospitalManagement.repository.DoctorRepository;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    // 🔥 Dependencies (Spring inject karega)
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    // ✅ Manual logger (Lombok @Slf4j ka replacement)
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(DoctorService.class);

    // ✅ Manual constructor (Lombok @RequiredArgsConstructor ka replacement)
    public DoctorService(DoctorRepository doctorRepository,
                         ModelMapper modelMapper,
                         UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    // =========================================================
    // 🔥 GET ALL DOCTORS
    // =========================================================
    public List<DoctorResponseDto> getAllDoctors() {

        // Step 1: DB se saare doctor fetch karo
        List<Doctor> doctors = doctorRepository.findAll();

        // Step 2: Entity → DTO mapping
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDto.class))
                .collect(Collectors.toList());
    }

    // =========================================================
    // 🔥 ONBOARD NEW DOCTOR
    // =========================================================
    @Transactional
    public DoctorResponseDto onBoardNewDoctor(OnboardDoctorRequestDto onBoardDoctorRequestDto) {

        // =====================================================
        // Step 1: User fetch karo (Doctor banane ke liye existing user chahiye)
        // =====================================================
        User user = userRepository.findById(onBoardDoctorRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // =====================================================
        // Step 2: Check karo already doctor hai ya nahi
        // ⚠️ NOTE: ye check weak hai (improve karenge niche)
        // =====================================================
        if (doctorRepository.existsById(onBoardDoctorRequestDto.getUserId())) {
            throw new IllegalArgumentException("Already a doctor");
        }

        // =====================================================
        // Step 3: Doctor object manually create (NO BUILDER)
        // =====================================================
//        Doctor doctor = new Doctor();
//
//        doctor.setName(onBoardDoctorRequestDto.getName()); // doctor name
//        doctor.setSpecialization(onBoardDoctorRequestDto.getSpecialization()); // specialization
//        doctor.setUser(user); // relation (VERY IMPORTANT 🔥)
//
//        // =====================================================
//        // Step 4: User role update karo
//        // =====================================================
//        user.getRoles().add(RoleType.DOCTOR);
//
//        // =====================================================
//        // Step 5: Doctor save karo DB me
//        // =====================================================
//        Doctor savedDoctor = doctorRepository.save(doctor);
//
//        // =====================================================
//        // Step 6: Entity → DTO convert karke return karo
//        // =====================================================
//        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
//    }
        Doctor doctor = new Doctor();

        doctor.setName(onBoardDoctorRequestDto.getName()); // doctor name

        doctor.setSpecialization(
                onBoardDoctorRequestDto.getSpecialization()
        ); // specialization

        doctor.setUser(user); // relation (VERY IMPORTANT 🔥)

// ✅ Email set from user
        doctor.setEmail(user.getUsername());


// =====================================================
// Step 4: User role update karo
// =====================================================
        user.getRoles().add(RoleType.DOCTOR);


// =====================================================
// Step 5: Doctor save karo DB me
// =====================================================
        Doctor savedDoctor = doctorRepository.save(doctor);


// =====================================================
// Step 6: Entity → DTO convert karke return karo
// =====================================================
        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
    }
}