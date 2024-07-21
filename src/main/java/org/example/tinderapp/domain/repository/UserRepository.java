package org.example.tinderapp.domain.repository;

import org.example.tinderapp.domain.entity.User;
import org.example.tinderapp.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByUsername(String username);

    Long getUserId(String username, String password) throws UserNotFoundException;

    void deleteById(Long id);

    List<User> findAll();
}
