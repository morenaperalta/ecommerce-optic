package com.irisandco.ecommerce_optic.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse user1;
    private UserResponse user2;
    private UserRequest user2Request;
    private List<UserResponse> userResponses;

    @BeforeEach
    void setUp() {
       userResponses = new ArrayList<>();
       user1 = new UserResponse(1L, "Judit", "judit@gmail.com");
       user2 = new UserResponse(2L, "Iris", "iris@hotmail.com");
       user2Request = new UserRequest("Iris", "iris@hotmail.com", "123456789012");
       userResponses.add(user1);
       userResponses.add(user2);
    }

    @Test
    void getAllUsers_whenUsersExist_returnsListOfUsersResponse() throws Exception{
        given(userService.getAllUsers()).willReturn(userResponses);

        mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].username").value("Judit"))
                .andExpect(jsonPath("$[1].username").value("Iris"))
                .andExpect(jsonPath("$[0].email").value("judit@gmail.com"))
                .andExpect(jsonPath("$[1].email").value("iris@hotmail.com"));

    }

    @Test
    void getUserById_whenUserExists_returnsUserResponse() throws Exception{
        Long userId = 1L;
        given(userService.getUserResponseById(userId)).willReturn(user1);

        mockMvc.perform(get("/api/users/{id}", userId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Judit"))
                .andExpect(jsonPath("$.email").value("judit@gmail.com"));
    }


    @Test
    void saveUser_whenCorrectRequest_returnsUserResponse() throws Exception{
        given(userService.saveUser(Mockito.any(UserRequest.class))).willReturn(user2);

        String json = objectMapper.writeValueAsString(user2Request);

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("Iris"))
                .andExpect(jsonPath("$.email").value("iris@hotmail.com"));
    }

}
