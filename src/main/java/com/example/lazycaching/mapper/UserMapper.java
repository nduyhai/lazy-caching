package com.example.lazycaching.mapper;


import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import com.example.lazycaching.dto.UserResponse;
import com.example.lazycaching.entities.cache.UserCached;
import com.example.lazycaching.entities.pg.UserEntity;
import com.example.lazycaching.fw.LazyCachingMapper;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper extends
    LazyCachingMapper<List<UserEntity>, List<UserCached>, List<UserResponse>> {

  UserResponse entityToDto(UserEntity userEntity);

  List<UserResponse> entityToDto(List<UserEntity> userEntity);

  UserResponse cachedToDto(UserCached userCached);

  List<UserResponse> cachedToDto(List<UserCached> userCached);

  UserCached entityToCached(UserEntity entity);

  List<UserCached> entityToCached(List<UserEntity> entity);
}
