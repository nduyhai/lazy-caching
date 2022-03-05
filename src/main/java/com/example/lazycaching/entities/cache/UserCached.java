package com.example.lazycaching.entities.cache;

import org.springframework.data.redis.core.RedisHash;
import lombok.Getter;
import lombok.Setter;

@RedisHash
@Getter
@Setter
public class UserCached {

  protected String id;
  protected String username;
  protected String firstName;
  protected String lastName;
  protected String email;
  protected boolean enabled;
  protected boolean emailVerified;
}
