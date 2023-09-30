package com.example.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dtos.UserDTO;
import com.example.demo.dtos.getUserDTO;
import com.example.demo.models.User;
import com.example.demo.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> obj = service.findAll();
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<getUserDTO> findById(@PathVariable String id) {
        User obj = service.findByID(id);
        return ResponseEntity.ok().body(new getUserDTO(obj.getId(), obj.getName(), obj.getEmail()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/searchName")
    public ResponseEntity<List<User>> findByName(@RequestParam(value = "name", defaultValue = "") String name) {
        String text;
        try {
            text = URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            text = "";
        }
        List<User> obj = service.findByName(text);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<User> insert(@RequestBody UserDTO objDto) {
        User obj = service.fromDTO(objDto);
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

}
