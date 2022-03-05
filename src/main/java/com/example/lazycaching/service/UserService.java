package com.example.lazycaching.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.mapper.UserMapper;
import com.example.lazycaching.repositories.pg.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;


@Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    return this.userRepository.findAll().stream().map(this.userMapper::toResponse).collect(
        Collectors.toList());
  }

  @Transactional(readOnly = true)

  public Optional<UserResponse> getUser(String username) {
    return this.userRepository.findOneByUsername(username).map(this.userMapper::toResponse);
  }
}
