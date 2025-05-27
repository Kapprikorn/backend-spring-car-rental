package nl.novi.sd.carrental.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.model.User;
import nl.novi.sd.carrental.model.UserRole;
import nl.novi.sd.carrental.security.JwtUtil;
import nl.novi.sd.carrental.security.dto.AuthenticationRequest;
import nl.novi.sd.carrental.security.dto.AuthenticationResponse;
import nl.novi.sd.carrental.security.dto.RegistrationRequest;
import nl.novi.sd.carrental.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @ResponseBody
    @PostMapping("/login")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest request) {
        User user = userService.getUserByUsername(request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setUsername(userDetails.getUsername());
        response.setRole(user.getRole().toString());

        return response;
    }

    @ResponseBody
    @PostMapping("/register")
    public AuthenticationResponse register(@Valid @RequestBody RegistrationRequest request) {

        return handleRegistration(request, UserRole.USER);
    }

    @ResponseBody
    @PostMapping("/register/admin")
    public AuthenticationResponse registerAdmin(@Valid @RequestBody RegistrationRequest request) {
        return handleRegistration(request, UserRole.ADMIN);
    }

    private AuthenticationResponse handleRegistration(RegistrationRequest request, UserRole role) {
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(role);

        User savedUser = userService.createUser(user);

        // Generate token
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getUsername())
                .password(savedUser.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + savedUser.getRole())))
                .build();

        String token = jwtUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole().toString());

        return response;
    }
}
