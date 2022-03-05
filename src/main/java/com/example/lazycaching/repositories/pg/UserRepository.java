package com.example.lazycaching.repositories.pg;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lazycaching.entities.pg.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  Optional<UserEntity> findOneById(String id);

}
