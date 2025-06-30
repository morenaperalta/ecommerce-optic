package com.irisandco.ecommerce_optic.user;

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
        return USER_REPOSITORY.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserResponse saveUser(UserRequest userRequest){
        if (USER_REPOSITORY.existByUsername(userRequest.username())){
            new IllegalArgumentException("Username is not available, please choose another one");
        }
        User user = UserMapper.toEntity(userRequest);
        return UserMapper.toDto((USER_REPOSITORY.save(user)));
    }

    public UserResponse updateUser(Long id, UserRequest userRequest){
        User user = getUserById(id);
        if (USER_REPOSITORY.existByUsername(userRequest.username())){
            new IllegalArgumentException("Username is not available, please choose another one");
        }

        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
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
