package com.ericmignardi.gms.service;

import com.ericmignardi.gms.model.User;
import com.ericmignardi.gms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User readById(Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User update(Long id, User user) {
        User updatedUser = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());
        return userRepository.save(updatedUser);
    }

    public User delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        userRepository.delete(user);
        return user;
    }
}