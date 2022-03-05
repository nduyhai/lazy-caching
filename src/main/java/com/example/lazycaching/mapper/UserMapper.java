package com.example.lazycaching.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.entities.cache.UserCached;
import com.example.lazycaching.entities.pg.UserEntity;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

  UserResponse toResponse(UserEntity userEntity);

  UserResponse toResponse(UserCached userCached);

  UserCached toCache(UserEntity entity);
}
