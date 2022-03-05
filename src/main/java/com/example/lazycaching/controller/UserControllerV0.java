package com.example.lazycaching.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserControllerV0 implements UserOperations {

  private final UserService legacyUserService;

  @GetMapping
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(this.legacyUserService.getUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getByUsername(@PathVariable String id) {
    return ResponseEntity.of(this.legacyUserService.getUserById(id));
  }
}
