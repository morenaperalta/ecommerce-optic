package com.irisandco.ecommerce_optic.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void getAllUsers_returnsListOfUsersResponse() {

        User userEntity = new User("Morena", "more@gmail.com", "12345");
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Morena", result.get(0).username());
        assertEquals("more@gmail.com", result.get(0).email());
    }
}
