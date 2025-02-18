package org.example.services;

import org.example.models.KitchenUser;
import org.example.repositories.KitchenUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
