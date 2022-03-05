package com.example.lazycaching.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.lazycaching.dto.UserResponse;

@RestController
public interface UserOperations {

  @GetMapping
  ResponseEntity<List<UserResponse>> getUsers();

  @GetMapping("/{id}")
  ResponseEntity<UserResponse> getByUsername(@PathVariable String id);
}
