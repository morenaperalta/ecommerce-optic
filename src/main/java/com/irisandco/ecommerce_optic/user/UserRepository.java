package com.irisandco.ecommerce_optic.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findUserByUsername(String username);
    Boolean existsByUsername(String username);
}
