package com.example.lazycaching.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.fw.LazyCachingLoaders;
import com.example.lazycaching.mapper.UserMapper;
import com.example.lazycaching.repositories.cache.UserCachedRepository;
import com.example.lazycaching.repositories.pg.UserRepository;
import com.example.lazycaching.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LegacyUserService implements UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserCachedRepository userCachedRepository;


  @Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    return LazyCachingLoaders.of(this.userCachedRepository::findAll,
        this.userRepository::findAll,
        userMapper);
  }

  @Transactional(readOnly = true)
  public Optional<UserResponse> getUserById(String id) {
    try {
      return this.userCachedRepository.findOneById(id).map(this.userMapper::cachedToDto);
    } catch (Exception e) {
      log.error("error during access redis", e);
      log.info("Cache miss!! and fallback with database");
      return this.userRepository.findOneById(id).map(this.userMapper::entityToDto);
    }
  }

}
