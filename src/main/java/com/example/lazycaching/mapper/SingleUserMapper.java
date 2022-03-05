package com.example.lazycaching.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.entities.cache.UserCached;
import com.example.lazycaching.entities.pg.UserEntity;
import com.example.lazycaching.fw.LazyCachingMapper;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SingleUserMapper extends
    LazyCachingMapper<UserEntity, UserCached, UserResponse> {

  UserResponse entityToDto(UserEntity userEntity);


  UserResponse cachedToDto(UserCached userCached);


  UserCached entityToCached(UserEntity entity);

}
