package com.example.lazycaching.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.mapper.UserMapper;
import com.example.lazycaching.repositories.cache.UserCachedRepository;
import com.example.lazycaching.repositories.pg.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserCachedRepository userCachedRepository;


  @Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    try {
      //We will add circle breaker here
      var cachedUsers = this.userCachedRepository.findAll();

      if (CollectionUtils.isEmpty(cachedUsers)) {
        // Async saved
        final var users = this.userRepository.findAll();
        cachedUsers = users.stream().map(this.userMapper::toCache)
            .collect(Collectors.toList());
        this.userCachedRepository.saveAll(cachedUsers);
        log.info("Cache miss!!!");
      } else {
        log.info("Cache hit!!!");
      }
      return cachedUsers.stream().map(this.userMapper::toResponse).collect(
          Collectors.toList());
    } catch (Exception e) {
      log.error("error during access redis", e);
      log.info("Cache miss!! and fallback with database");
      return this.userRepository.findAll().stream().map(this.userMapper::toResponse).collect(
          Collectors.toList());
    }
  }

  @Transactional(readOnly = true)
  public Optional<UserResponse> getUserById(String id) {
    try {
      return this.userCachedRepository.findOneById(id).map(this.userMapper::toResponse);
    } catch (Exception e) {
      log.error("error during access redis", e);
      log.info("Cache miss!! and fallback with database");
      return this.userRepository.findOneById(id).map(this.userMapper::toResponse);
    }
  }
}
