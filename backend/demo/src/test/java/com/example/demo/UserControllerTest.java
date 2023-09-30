package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.demo.controllers.UserController;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private String id;
    private User user;

    @InjectMocks
    UserController controller;

    @Mock
    private UserService service;

    MockMvc mockMvc;
    List<User> expectedUsers;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .alwaysDo(print())
                .build();
        expectedUsers = Arrays.asList(
                new User("UUID.randomUUID()", "jose", "josemaia.comp@gmail.com"),
                new User("UUID.randomUUID()", "jose", "josemaia.comp@gmail.com"),
                new User("UUID.randomUUID()", "jose", "josemaia.comp@gmail.com"));

        id = UUID.randomUUID().toString();
        user = new User(id, "jose", "josemaia.comp@gmail.com");
    }

    @Test
    public void deveEncontrarTodos() throws Exception {

        when(service.findAll()).thenReturn(expectedUsers);

        mockMvc.perform(get("/users")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void deveEncontrarApartirDeUmID() throws Exception {

        when(service.findByID(id)).thenReturn(user);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUser = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonUser));

        verify(service).findByID(id);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void deveDeletarApartirDEumID() throws Exception {

        doNothing().when(service).delete(id);
        mockMvc.perform(delete("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(service).delete(id);
        verifyNoMoreInteractions(service);
    }

}
