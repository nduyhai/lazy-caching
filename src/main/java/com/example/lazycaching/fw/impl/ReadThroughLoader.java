package com.example.lazycaching.fw.impl;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import org.springframework.util.CollectionUtils;
import com.example.lazycaching.fw.LazyCachingLoader;
import com.example.lazycaching.fw.LazyCachingMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.SupplierUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadThroughLoader<E, C, D> implements LazyCachingLoader<E, C, D> {

  protected final CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
      .failureRateThreshold(50)
      .waitDurationInOpenState(Duration.ofMillis(1000))
      .permittedNumberOfCallsInHalfOpenState(2)
      .slidingWindowSize(2)
      .recordExceptions(IOException.class, TimeoutException.class)
      .build();
  protected final CircuitBreakerRegistry circuitBreakerRegistry =
      CircuitBreakerRegistry.of(circuitBreakerConfig);

  protected final String name;
  protected final LazyCachingMapper<E, C, D> mapper;
  protected CircuitBreaker circuitBreaker;
  protected Supplier<C> cached;
  protected Supplier<C> recoverCached;
  protected Supplier<E> supplier;

  public ReadThroughLoader(String name, LazyCachingMapper<E, C, D> mapper) {
    this.name = name;
    this.mapper = mapper;
  }

  public LazyCachingLoader<E, C, D> withCache(Supplier<C> cached) {
    this.cached = cached;
    return this;
  }

  public LazyCachingLoader<E, C, D> withSupplier(Supplier<E> supplier) {
    this.supplier = supplier;
    return this;
  }

  public LazyCachingLoader<E, C, D> build() {
    Objects.requireNonNull(cached, "cached must be non null");
    Objects.requireNonNull(supplier, "supplier must be non null");

    log.info("Init CB {}", name);
    this.circuitBreaker = circuitBreakerRegistry
        .circuitBreaker(name);

    this.recoverCached = SupplierUtils.recover(cached,
        throwable -> {
          log.error("error during recover cb {}", name, throwable);
          return mapper.entityToCached(supplier.get());
        });

    return this;
  }

  public D execute() {
    final var c = this.circuitBreaker.executeSupplier(recoverCached);
    if (isEmpty(c)) {
      return mapper.entityToDto(supplier.get());
    } else {
      return mapper.cachedToDto(c);
    }
  }

  protected boolean isEmpty(Object c) {
    if (Objects.isNull(c)) {
      return true;
    }
    if (c instanceof Optional) {
      return ((Optional<?>) c).isEmpty();
    }
    if (c instanceof Collection) {
      return CollectionUtils.isEmpty((Collection<?>) c);
    }
    return true;
  }


}
