package com.irisandco.ecommerce_optic.user;

import com.irisandco.ecommerce_optic.exception.EntityAlreadyExistsException;
import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository USER_REPOSITORY;

    public UserService(UserRepository userRepository){
        USER_REPOSITORY = userRepository;
    }

    public List<UserResponse> getAllUsers(){
        return listToDto(USER_REPOSITORY.findAll());
    }

    public User getUserById(Long id){
        return USER_REPOSITORY.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "Id", id.toString()));
    }

    public UserResponse getUserResponseById(Long id){
        User user = getUserById(id);
        return UserMapper.toDto(user);
    }

    public UserResponse saveUser(UserRequest userRequest){
        if (USER_REPOSITORY.existsByUsername(userRequest.username())) {
            throw new EntityAlreadyExistsException(User.class.getSimpleName(), "username", userRequest.username());
        }
        User user = UserMapper.toEntity(userRequest);
        return UserMapper.toDto(USER_REPOSITORY.save(user));
    }

    public UserResponse updateUser(Long id, UserRequest userRequest){
        User user = getUserById(id);
        String username = userRequest.username().trim();
        if(!user.getUsername().equals(username)){
            if (USER_REPOSITORY.existsByUsername(username)){
                throw new EntityAlreadyExistsException(User.class.getSimpleName(), "username", username);
            }
        }

        user.setUsername(username);
        user.setEmail(userRequest.email().trim());
        user.setPassword(userRequest.password());

        return UserMapper.toDto(USER_REPOSITORY.save(user));
    }

    public void deleteUser(Long id){
        User user = getUserById(id);
        USER_REPOSITORY.deleteById(id);
    }

    private List<UserResponse> listToDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }
}