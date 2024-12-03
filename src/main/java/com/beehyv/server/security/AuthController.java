package com.beehyv.server.security;

import com.beehyv.server.entity.Employee;
import com.beehyv.server.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Extract user details
        var user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        Employee employee = employeeRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Cannot find user"));

        // Generate JWT
        return jwtUtil.generateToken(user.getUsername(), user.getAuthorities().iterator().next().getAuthority(), employee.getId());
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginRequest {
    private String username;
    private String password;
}
