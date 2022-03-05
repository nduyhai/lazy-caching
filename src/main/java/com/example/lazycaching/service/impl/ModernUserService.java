package com.example.lazycaching.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModernUserService implements UserService {

  @Override
  public List<UserResponse> getUsers() {
    return null;
  }

  @Override
  public Optional<UserResponse> getUserById(String id) {
    return Optional.empty();
  }
}
