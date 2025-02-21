package org.example.repositories;

import org.example.models.KitchenUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenUserRepository extends JpaRepository<KitchenUser,Long> {
    KitchenUser findByUsername(String username);
}
