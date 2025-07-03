package com.irisandco.ecommerce_optic.user;

import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private User userEntity;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
        userEntity = new User("Morena", "more@gmail.com", "12345");
        userResponse = new UserResponse(1L, "Morena","more@gmail.com");
    }

    @Test
    void getAllUsers_returnsListOfUsersResponse() {

        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Morena", result.get(0).username());
        assertEquals("more@gmail.com", result.get(0).email());
    }

    @Test
    void getUserById_returnsUserEntity() {

        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));

        User result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(User.class, result.getClass());
        assertEquals("Morena", result.getUsername());
        assertEquals("more@gmail.com", result.getEmail());
    }

    @Test
    void getUserById_returnsEntityNotFoundException() {

        Long id = 2L;
        String messageExpected = "User with Id " + id + " was not found";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Exception result = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));

        assertEquals(messageExpected,result.getMessage());
    }

    @Test
    void getUserResponseById_returnsUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
    }
}
