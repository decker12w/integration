package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.UserDTO;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.exceptions.DatabaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User insert(User obj) {
        return repository.save(obj);
    }

    public void delete(String id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            new DatabaseException(e.getMessage());
        }
    }

    public User findByID(String id) {
        User user = repository.findById(id).get();
        return user;
    }

    public List<User> findByName(String name) {
        List<User> user = repository.findByName(name);
        return user;
    }

    public User fromDTO(UserDTO dto) {
        String id = UUID.randomUUID().toString();
        return new User(id, dto.name(), dto.email());
    }
}
