package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    User user;

    @BeforeEach
    public void setup() {
        user = new User("UUID.randomUUID()", "jose", "josemaia.comp@gmail.com");
    }

    @Test
    void mustFindAllUsers(){

        when(repository.findAll()).thenReturn(Collections.singletonList(user));
        
        List<User> users = service.findAll();

        assertEquals(Collections.singletonList(user),users);
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
        }

    @Test
    void mustFindAllUsers2() {
        // Configurar o comportamento do repositório para retornar uma lista de usuários
        List<User> expectedUsers = Arrays.asList(
                new User("UUID.randomUUID()", "jose", "josemaia.comp@gmail.com"),
                new User("UUID.randomUUID()", "jose", "josemaia.comp@gmail.com"),
                new User("UUID.randomUUID()", "jose", "josemaia.comp@gmail.com"));
        when(repository.findAll()).thenReturn(expectedUsers);

        // Chamar o método findAll do serviço
        List<User> actualUsers = service.findAll();

        // Verificar se a lista retornada pelo serviço é igual à lista esperada
        assertEquals(expectedUsers, actualUsers);

        // Verificar se o método findAll do repositório foi chamado
        verify(repository).findAll();

        // Verificar se não houve mais interações com o repositório
        verifyNoMoreInteractions(repository);
    }

}
