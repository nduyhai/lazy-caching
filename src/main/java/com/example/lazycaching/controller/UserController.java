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
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(this.userService.getUsers());
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
    return ResponseEntity.of(this.userService.getUser(username));
  }
}
