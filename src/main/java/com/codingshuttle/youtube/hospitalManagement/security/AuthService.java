//package com.codingshuttle.youtube.hospitalManagement.security;
//
//import com.codingshuttle.youtube.hospitalManagement.dto.*;
//import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
//import com.codingshuttle.youtube.hospitalManagement.entity.User;
//import com.codingshuttle.youtube.hospitalManagement.entity.type.AuthProviderType;
//import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
//import com.codingshuttle.youtube.hospitalManagement.repository.PatientRepository;
//import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
//
//import jakarta.transaction.Transactional;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//
//@Service
//public class AuthService {
//
//    // 🔥 Dependencies (Spring inject karega)
//    private final AuthenticationManager authenticationManager;
//    private final AuthUtil authUtil;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final PatientRepository patientRepository;
//
//    // ✅ Manual constructor (Lombok @RequiredArgsConstructor ka replacement)
//    public AuthService(AuthenticationManager authenticationManager,
//                       AuthUtil authUtil,
//                       UserRepository userRepository,
//                       PasswordEncoder passwordEncoder,
//                       PatientRepository patientRepository) {
//        this.authenticationManager = authenticationManager;
//        this.authUtil = authUtil;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.patientRepository = patientRepository;
//    }
//
//    // =========================================================
//    // 🔐 LOGIN METHOD
//    // =========================================================
//    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
//
//        // Step 1: Username + Password verify
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequestDto.getUsername(),
//                        loginRequestDto.getPassword()
//                )
//        );
//
//        // Step 2: Authenticated user nikaalo
//        User user = (User) authentication.getPrincipal();
//
//        // Step 3: JWT generate karo
//        String token = authUtil.generateAccessToken(user);
//
//        // Step 4: Response return karo
//        return new LoginResponseDto(token, user.getId());
//    }
//
//    // =========================================================
//    // 📝 INTERNAL SIGNUP (CORE LOGIC)
//    // =========================================================
//    public User signUpInternal(SignUpRequestDto signupRequestDto,
//                               AuthProviderType authProviderType,
//                               String providerId) {
//
//        // Step 1: Check user already exist
//        User existingUser = userRepository
//                .findByUsername(signupRequestDto.getUsername())
//                .orElse(null);
//
//        if (existingUser != null) {
//            throw new IllegalArgumentException("User already exists");
//        }
//
//        // Step 2: User object manually create (NO BUILDER)
//        User user = new User();
//        user.setUsername(signupRequestDto.getUsername());
//        user.setProviderId(providerId);
//        user.setProviderType(authProviderType);
//
//        // ⚠️ Better: default role set karo (safe)
//        if (signupRequestDto.getRoles() == null || signupRequestDto.getRoles().isEmpty()) {
//            user.setRoles(Set.of(RoleType.PATIENT));
//        } else {
//            user.setRoles(signupRequestDto.getRoles());
//        }
//
//        // Step 3: Password encode (only EMAIL signup)
//        if (authProviderType == AuthProviderType.EMAIL) {
//            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
//        }
//
//        // Step 4: Save user in DB
//        user = userRepository.save(user);
//
//        // Step 5: Patient entity create (User → Patient relation)
//        Patient patient = new Patient();
//        patient.setName(signupRequestDto.getName());
//        patient.setEmail(signupRequestDto.getUsername());
//        patient.setUser(user);
//
//        // Step 6: Save patient
//        patientRepository.save(patient);
//
//        return user;
//    }
//
//    // =========================================================
//    // 📝 SIGNUP API
//    // =========================================================
//    public SignupResponseDto signup(SignUpRequestDto signupRequestDto) {
//
//        User user = signUpInternal(
//                signupRequestDto,
//                AuthProviderType.EMAIL,
//                null
//        );
//
//        return new SignupResponseDto(user.getId(), user.getUsername());
//    }
//
//    // =========================================================
//    // 🌐 OAUTH2 LOGIN FLOW (ADVANCED)
//    // =========================================================
//    @Transactional
//    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(
//            OAuth2User oAuth2User,
//            String registrationId
//    ) {
//
//        // Step 1: Provider type (Google/GitHub)
//        AuthProviderType providerType =
//                authUtil.getProviderTypeFromRegistrationId(registrationId);
//
//        // Step 2: Provider ID (unique id from Google/GitHub)
//        String providerId =
//                authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);
//
//        // Step 3: Check existing user
//        User user = userRepository
//                .findByProviderIdAndProviderType(providerId, providerType)
//                .orElse(null);
//
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//
//        User emailUser = userRepository.findByUsername(email).orElse(null);
//
//        // =========================================================
//        // CASE 1: New OAuth user → signup
//        // =========================================================
//        if (user == null && emailUser == null) {
//
//            String username = authUtil.determineUsernameFromOAuth2User(
//                    oAuth2User,
//                    registrationId,
//                    providerId
//            );
//
//            user = signUpInternal(
//                    new SignUpRequestDto(
//                            username,
//                            null,
//                            name,
//                            Set.of(RoleType.PATIENT)
//                    ),
//                    providerType,
//                    providerId
//            );
//
//        }
//        // =========================================================
//        // CASE 2: Existing OAuth user
//        // =========================================================
//        else if (user != null) {
//
//            // email update if changed
//            if (email != null && !email.isBlank()
//                    && !email.equals(user.getUsername())) {
//
//                user.setUsername(email);
//                userRepository.save(user);
//            }
//
//        }
//        // =========================================================
//        // CASE 3: Conflict
//        // =========================================================
//        else {
//            throw new BadCredentialsException(
//                    "This email is already registered with provider "
//                            + emailUser.getProviderType()
//            );
//        }
//
//        // Step 4: JWT generate
//        String token = authUtil.generateAccessToken(user);
//
//        LoginResponseDto response =
//                new LoginResponseDto(token, user.getId());
//
//        return ResponseEntity.ok(response);
//    }
//}
package com.codingshuttle.youtube.hospitalManagement.security;

import com.codingshuttle.youtube.hospitalManagement.dto.*;
import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
import com.codingshuttle.youtube.hospitalManagement.entity.RefreshToken;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.entity.type.AuthProviderType;
import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import com.codingshuttle.youtube.hospitalManagement.repository.PatientRepository;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import com.codingshuttle.youtube.hospitalManagement.service.RefreshTokenService;

import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    // 🔥 Dependencies
    private final AuthenticationManager authenticationManager;

    private final AuthUtil authUtil;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PatientRepository patientRepository;

    private final RefreshTokenService refreshTokenService;

    // ✅ Constructor Injection
    public AuthService(
            AuthenticationManager authenticationManager,
            AuthUtil authUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PatientRepository patientRepository,
            RefreshTokenService refreshTokenService
    ) {

        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.refreshTokenService = refreshTokenService;
    }

    // =========================================================
    // 🔐 LOGIN METHOD
    // =========================================================

    public LoginResponseDto login(
            LoginRequestDto loginRequestDto
    ) {

        // Step 1: Authenticate user

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.getUsername(),
                                loginRequestDto.getPassword()
                        )
                );

        // Step 2: Get authenticated user

        User user =
                (User) authentication.getPrincipal();

        // Step 3: Generate access token

        String accessToken =
                authUtil.generateAccessToken(user);

        // Step 4: Generate refresh token

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // Step 5: Create response

        LoginResponseDto response =
                new LoginResponseDto();

        response.setJwt(accessToken);

        response.setRefreshToken(
                refreshToken.getToken()
        );

        response.setUserId(user.getId());

        return response;
    }

    // =========================================================
    // 🔄 REFRESH TOKEN METHOD
    // =========================================================

    public LoginResponseDto refreshToken(String token) {

        // Step 1: Verify refresh token

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(token);

        // Step 2: Get user

        User user = refreshToken.getUser();

        // Step 3: Generate new access token

        String accessToken =
                authUtil.generateAccessToken(user);

        // Step 4: Prepare response

        LoginResponseDto response =
                new LoginResponseDto();

        response.setJwt(accessToken);

        response.setRefreshToken(
                refreshToken.getToken()
        );

        response.setUserId(user.getId());

        return response;
    }

    // =========================================================
    // 📝 INTERNAL SIGNUP
    // =========================================================

    public User signUpInternal(
            SignUpRequestDto signupRequestDto,
            AuthProviderType authProviderType,
            String providerId
    ) {

        // Step 1: Check existing user

        User existingUser = userRepository
                .findByUsername(
                        signupRequestDto.getUsername()
                )
                .orElse(null);

        if (existingUser != null) {
            throw new IllegalArgumentException(
                    "User already exists"
            );
        }

        // Step 2: Create user

        User user = new User();

        user.setUsername(
                signupRequestDto.getUsername()
        );

        user.setProviderId(providerId);

        user.setProviderType(authProviderType);

        // Default role

        if (signupRequestDto.getRoles() == null
                ||
                signupRequestDto.getRoles().isEmpty()) {

            user.setRoles(
                    Set.of(RoleType.PATIENT)
            );

        } else {

            user.setRoles(
                    signupRequestDto.getRoles()
            );
        }

        // Step 3: Encode password

        if (authProviderType ==
                AuthProviderType.EMAIL) {

            user.setPassword(
                    passwordEncoder.encode(
                            signupRequestDto.getPassword()
                    )
            );
        }

        // Step 4: Save user

        user = userRepository.save(user);

        // Step 5: Create patient

        Patient patient = new Patient();

        patient.setName(
                signupRequestDto.getName()
        );

        patient.setEmail(
                signupRequestDto.getUsername()
        );

        patient.setUser(user);

        // Step 6: Save patient

        patientRepository.save(patient);

        return user;
    }

    // =========================================================
    // 📝 SIGNUP METHOD
    // =========================================================

    public SignupResponseDto signup(
            SignUpRequestDto signupRequestDto
    ) {

        User user = signUpInternal(
                signupRequestDto,
                AuthProviderType.EMAIL,
                null
        );

        return new SignupResponseDto(
                user.getId(),
                user.getUsername()
        );
    }
    public void logout(String refreshToken) {

        refreshTokenService
                .verifyRefreshToken(refreshToken);
    }

    // =========================================================
    // 🌐 OAUTH2 LOGIN FLOW
    // =========================================================

    @Transactional
    public ResponseEntity<LoginResponseDto>
    handleOAuth2LoginRequest(
            OAuth2User oAuth2User,
            String registrationId
    ) {

        // Step 1: Provider type

        AuthProviderType providerType =
                authUtil.getProviderTypeFromRegistrationId(
                        registrationId
                );

        // Step 2: Provider ID

        String providerId =
                authUtil.determineProviderIdFromOAuth2User(
                        oAuth2User,
                        registrationId
                );

        // Step 3: Existing user check

        User user = userRepository
                .findByProviderIdAndProviderType(
                        providerId,
                        providerType
                )
                .orElse(null);

        String email =
                oAuth2User.getAttribute("email");

        String name =
                oAuth2User.getAttribute("name");

        User emailUser =
                userRepository.findByUsername(email)
                        .orElse(null);

        // =====================================================
        // CASE 1: New OAuth user
        // =====================================================

        if (user == null && emailUser == null) {

            String username =
                    authUtil.determineUsernameFromOAuth2User(
                            oAuth2User,
                            registrationId,
                            providerId
                    );

            user = signUpInternal(
                    new SignUpRequestDto(
                            username,
                            null,
                            name,
                            Set.of(RoleType.PATIENT)
                    ),
                    providerType,
                    providerId
            );
        }

        // =====================================================
        // CASE 2: Existing OAuth user
        // =====================================================

        else if (user != null) {

            if (email != null
                    &&
                    !email.isBlank()
                    &&
                    !email.equals(user.getUsername())) {

                user.setUsername(email);

                userRepository.save(user);
            }
        }

        // =====================================================
        // CASE 3: Conflict
        // =====================================================

        else {

            throw new BadCredentialsException(
                    "This email is already registered with provider "
                            + emailUser.getProviderType()
            );
        }

        // Step 4: Generate access token

        String accessToken =
                authUtil.generateAccessToken(user);

        // Step 5: Generate refresh token

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // Step 6: Prepare response

        LoginResponseDto response =
                new LoginResponseDto();

        response.setJwt(accessToken);

        response.setRefreshToken(
                refreshToken.getToken()
        );

        response.setUserId(user.getId());

        return ResponseEntity.ok(response);
    }
}