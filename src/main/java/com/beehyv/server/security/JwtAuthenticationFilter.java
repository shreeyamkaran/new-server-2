package com.beehyv.server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = null;
        String header = request.getHeader("Authorization");

        // Check if the token is present in the Authorization header
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // Remove "Bearer " prefix
        }

        // If the token was not found in the Authorization header, try extracting it from the URL path
        if (token == null) {
            String path = request.getRequestURI();
            token = extractTokenFromPath(path);
        }

        // If a token was found, validate it and authenticate the user
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractClaims(token).getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    private String extractTokenFromPath(String path) {
        // Split the path by "/"
        String[] pathParts = path.split("/");

        // Ensure there is at least one part to process
        if (pathParts.length > 0) {
            String lastSegment = pathParts[pathParts.length - 1];
            // Check if the last segment starts with "Bearer"
            if (lastSegment.startsWith("Bearer")) {
                // Extract and return the token by removing the "Bearer" prefix
                return lastSegment.substring(6);  // Adjust for "Bearer" without space
            }
        }

        // Return null if no token is found
        return null;
    }
}
