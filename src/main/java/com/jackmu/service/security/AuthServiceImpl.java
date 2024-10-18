package com.jackmu.service.security;

import com.jackmu.jwt.JwtTokenProvider;
import com.jackmu.model.security.LoginDTO;
import com.jackmu.model.security.RegisterDTO;
import com.jackmu.model.security.Role;
import com.jackmu.model.security.User;
import com.jackmu.repository.security.RoleRepository;
import com.jackmu.repository.security.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService{
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public ResponseEntity<String> register(RegisterDTO registerDTO){
        JSONObject resp = new JSONObject();
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            resp.put("status", 400);
            resp.put("body", "Email already exists");
            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByUsername(registerDTO.getUsername())){
            resp.put("status", 400);
            resp.put("body", "Username already exists");
            return new ResponseEntity<>(resp.toString(), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        resp.put("status", 201);
        resp.put("body", "User registered successfully");
        return new ResponseEntity<>(resp.toString(), HttpStatus.CREATED);

    }


}
