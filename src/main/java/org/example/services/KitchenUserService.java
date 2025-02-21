package org.example.services;

import org.example.models.KitchenUser;
import org.example.repositories.KitchenUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitchenUserService{

    private final KitchenUserRepository kitchenUserRepository;

    @Autowired
    public KitchenUserService(KitchenUserRepository kitchenUserRepository) {
        this.kitchenUserRepository = kitchenUserRepository;
    }

    public List<KitchenUser> findAll() {
        return kitchenUserRepository.findAll();
    }

    public KitchenUser findById(Long id) {
        return kitchenUserRepository.findById(id).orElse(null);
    }

    public KitchenUser save(KitchenUser kitchenUser) {
        return kitchenUserRepository.save(kitchenUser);
    }

    public void deleteById(Long id) {
        kitchenUserRepository.deleteById(id);
    }
    public boolean authenticate(String username, String password) {
        Optional<KitchenUser> userOptional = Optional.ofNullable(kitchenUserRepository.findByUsername(username));
        if (userOptional.isPresent()) {
            KitchenUser user = userOptional.get();
            return user.getPassword().equals(password);
        }
        return false;
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
}
