package com.example.lazycaching.entities.pg;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_entity")
public class UserEntity implements Serializable {

  @Id
  protected String id;
  protected String username;
  protected String firstName;
  protected Long createdTimestamp;
  protected String lastName;
  protected String email;
  protected boolean enabled;
  protected boolean emailVerified;

  protected String emailConstraint;

  protected String realmId;
}
