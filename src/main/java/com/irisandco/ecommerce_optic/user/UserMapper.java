package com.irisandco.ecommerce_optic.user;

public class UserMapper {
    public static User toEntity(UserRequest userRequest) {
        return new User(userRequest.username(), userRequest.email(), userRequest.password());
    }

    public static UserResponse toDto(User user){
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    };
}
