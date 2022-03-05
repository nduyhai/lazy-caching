package com.example.lazycaching.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

  protected String id;
  protected String username;
  protected String firstName;
  protected String lastName;
  protected String email;
  protected boolean enabled;
  protected boolean emailVerified;

}
