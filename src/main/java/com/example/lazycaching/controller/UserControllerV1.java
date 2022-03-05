package com.example.lazycaching.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserControllerV1 implements UserOperations {

  private final UserService modernUserService;

  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(this.modernUserService.getUsers());
  }

  public ResponseEntity<UserResponse> getByUsername(String id) {
    return ResponseEntity.of(this.modernUserService.getUserById(id));
  }
}
