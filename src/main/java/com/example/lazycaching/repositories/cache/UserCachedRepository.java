package com.example.lazycaching.repositories.cache;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lazycaching.entities.cache.UserCached;

public interface UserCachedRepository extends JpaRepository<UserCached, String> {

  Optional<UserCached> findOneById(String id);

}
