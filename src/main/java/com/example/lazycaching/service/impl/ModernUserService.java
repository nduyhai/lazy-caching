package com.example.lazycaching.service.impl;

import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.entities.cache.UserCached;
import com.example.lazycaching.entities.pg.UserEntity;
import com.example.lazycaching.fw.LazyCachingLoader;
import com.example.lazycaching.fw.LazyCachingLoaders;
import com.example.lazycaching.mapper.UserMapper;
import com.example.lazycaching.repositories.cache.UserCachedRepository;
import com.example.lazycaching.repositories.pg.UserRepository;
import com.example.lazycaching.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModernUserService implements UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserCachedRepository userCachedRepository;
  private LazyCachingLoader<List<UserEntity>, List<UserCached>, List<UserResponse>> usersLoader;
  private LazyCachingLoader<List<UserEntity>, List<UserCached>, List<UserResponse>> usersRefreshLoader;

  @PostConstruct
  public void init() {
    usersLoader =
        LazyCachingLoaders.readThrough(
                "getUsers", userMapper)
            .withCache(this.userCachedRepository::findAll)
            .withSupplier(this.userRepository::findAll)
            .build();

    usersRefreshLoader = LazyCachingLoaders.refreshReadThrough(
            "getUsers", userMapper)
        .withCache(this.userCachedRepository::findAll)
        .withSupplier(this.userRepository::findAll)
        .withCacheUpdater(this.userCachedRepository::saveAll)
        .build();
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserResponse> getUsers() {
    return usersRefreshLoader.execute();
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<UserResponse> getUserById(String id) {
    return Optional.empty();
  }
}
