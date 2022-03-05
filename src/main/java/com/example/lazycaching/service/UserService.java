package com.example.lazycaching.service;

import java.util.List;
import java.util.Optional;
import com.example.lazycaching.dto.UserResponse;

public interface UserService {

  List<UserResponse> getUsers();

  Optional<UserResponse> getUserById(String id);
}
