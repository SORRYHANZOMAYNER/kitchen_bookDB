package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.configs.security.JwtService;
import org.example.dto.TokenDTO;
import org.example.models.KitchenUser;
import org.example.repositories.KitchenUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.Optional;

@Service
public class KitchenUserService{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private final KitchenUserRepository kitchenUserRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    public KitchenUserService(KitchenUserRepository kitchenUserRepository) {
        this.kitchenUserRepository = kitchenUserRepository;
    }

    public List<KitchenUser> findAll() {
        return kitchenUserRepository.findAll();
    }

    public KitchenUser findById(Long id) {
        return kitchenUserRepository.findById(id).orElse(null);
    }
    @Transactional
    public KitchenUser save(KitchenUser kitchenUser) {
        System.out.println("user for reg " + kitchenUser);
        System.out.println("users password " + passwordEncoder.encode(kitchenUser.getPassword()));
        kitchenUser.setPassword(passwordEncoder.encode(kitchenUser.getPassword()));
        kitchenUserRepository.save(kitchenUser);
        return kitchenUser;
    }
    @Transactional
    public void deleteById(Long id) {
        kitchenUserRepository.deleteById(id);
    }
    public TokenDTO authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            KitchenUser user = kitchenUserRepository.findByUsername(username);
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            System.out.println("Access Token: " + accessToken);
            System.out.println("Refresh Token: " + refreshToken);

            return new TokenDTO(accessToken, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Token generation failed: " + e.getMessage());
            return null;
        }
    }
    public KitchenUser findByUsername(String username) {
        return kitchenUserRepository.findByUsername(username);
    }
    public KitchenUser getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof KitchenUser) {
                return (KitchenUser) principal;
            } else if (principal instanceof org.springframework.security.core.userdetails.User) {
                String login = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                return kitchenUserRepository.findByUsername(login);
            }
        }
        throw new UsernameNotFoundException("User not found");
    }
    @Transactional
    public void updateUser(KitchenUser updatedUser) {
        KitchenUser existingUser = kitchenUserRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        existingUser.setName(updatedUser.getName());
        existingUser.setSurName(updatedUser.getSurName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setTelephoneNumber(updatedUser.getTelephoneNumber());
        existingUser.setUserImage(updatedUser.getUserImage());
        existingUser.setUsername(updatedUser.getUsername());
        kitchenUserRepository.save(existingUser);
    }
    public TokenDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("No JWT token");
        }
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        KitchenUser user = kitchenUserRepository.findByUsername(username);
        if (jwtService.validate(token)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return new TokenDTO(accessToken, refreshToken);
        }
        return null;
    }
}
