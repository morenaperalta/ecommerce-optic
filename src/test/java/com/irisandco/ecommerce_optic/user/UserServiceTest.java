package com.irisandco.ecommerce_optic.user;

import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User userEntity;
    private UserResponse userResponse;
    private UserRequest userRequest;
    private User userMapped;

    @BeforeEach
    void setUp() {
        userEntity = new User("Morena", "more@gmail.com", "12345");
        userResponse = new UserResponse(1L, "Morena","more@gmail.com");
        userRequest = new UserRequest( "Morena", "more@gmail.com", "1234");
        userMapped = UserMapper.toEntity(userRequest);
    }

    @Test
    void getAllUsers_whenUsersExist_returnsListOfUsersResponse() {

        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Morena", result.getFirst().username());
        assertEquals("more@gmail.com", result.getFirst().email());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_whenUserExists_returnsUserEntity() {

        Long id = 1L;
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(userEntity));

        User result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(User.class, result.getClass());
        assertEquals("Morena", result.getUsername());
        assertEquals("more@gmail.com", result.getEmail());

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void getUserById_whenUserDoesNotExist_throwsEntityNotFoundException() {

        Long id = 2L;
        String messageExpected = "User with Id " + id + " was not found";
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception result = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));

        assertEquals(messageExpected,result.getMessage());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void getUserResponseById_whenUserExists_returnsUserResponse() {
        Long id = 1L;
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(userEntity));

        UserResponse result = userService.getUserResponseById(id);

        assertNotNull(result);
        assertEquals(UserResponse.class, result.getClass());
        assertEquals("Morena", result.username());
        assertEquals("more@gmail.com", result.email());

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void saveUser_whenCorrectRequest_returnsUserResponse() {
        when(userRepository.existsByUsername(userRequest.username())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserResponse result = userService.saveUser(userRequest);

        assertNotNull(result);
        assertEquals(UserResponse.class, result.getClass());
        assertEquals("Morena", result.username());
        assertEquals("more@gmail.com", result.email());

        verify(userRepository, times(1)).existsByUsername(userRequest.username());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
