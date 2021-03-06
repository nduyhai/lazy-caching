package com.example.lazycaching.fw.impl;

import java.util.Objects;
import java.util.function.Consumer;
import com.example.lazycaching.fw.LazyCachingLoader;
import com.example.lazycaching.fw.LazyCachingMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshReadThroughLoader<E, C, D> extends ReadThroughLoader<E, C, D> {

  protected Consumer<C> cachedUpdater;

  public RefreshReadThroughLoader(String name,
      LazyCachingMapper<E, C, D> mapper) {
    super(name, mapper);
  }

  @Override
  public D execute() {
    final var c = this.circuitBreaker.executeSupplier(recoverCached);
    if (LoaderUtils.isEmpty(c)) {
      final var entity = supplier.get();
      final var entityCaches = this.mapper.entityToCached(entity);

      this.updateCache(entityCaches);

      return mapper.entityToDto(entity);
    } else {
      return mapper.cachedToDto(c);
    }
  }

  private void updateCache(C entityCached) {
    if (this.circuitBreaker.tryAcquirePermission()) {
      log.info("[{}] Acquired permission and updating cached", name);
      this.cachedUpdater.accept(entityCached);
      log.info("[{}] Acquired permission and updated cached", name);

    }
  }

  @Override
  public LazyCachingLoader<E, C, D> withCacheUpdater(Consumer<C> cacheUpdater) {
    this.cachedUpdater = cacheUpdater;
    return this;
  }

  @Override
  public LazyCachingLoader<E, C, D> build() {
    Objects.requireNonNull(cachedUpdater, "cachedUpdater must be non null");
    return super.build();
  }
}
