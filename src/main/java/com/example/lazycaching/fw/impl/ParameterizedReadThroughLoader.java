package com.example.lazycaching.fw.impl;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.dao.QueryTimeoutException;
import com.example.lazycaching.fw.LazyCachingMapper;
import com.example.lazycaching.fw.ParameterizedLazyCachingLoader;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.SupplierUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParameterizedReadThroughLoader<T, E, C, D> implements
    ParameterizedLazyCachingLoader<T, E, C, D> {

  protected final CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
      .failureRateThreshold(50)
      .waitDurationInOpenState(Duration.ofMillis(1000))
      .permittedNumberOfCallsInHalfOpenState(2)
      .slidingWindowSize(2)
      .recordExceptions(IOException.class, TimeoutException.class, QueryTimeoutException.class)
      .build();
  protected final CircuitBreakerRegistry circuitBreakerRegistry =
      CircuitBreakerRegistry.of(circuitBreakerConfig);

  protected final String name;
  protected final LazyCachingMapper<E, C, D> mapper;
  protected CircuitBreaker circuitBreaker;
  protected Function<T, C> cached;
  protected Function<T, E> supplier;

  public ParameterizedReadThroughLoader(String name, LazyCachingMapper<E, C, D> mapper) {
    this.name = name;
    this.mapper = mapper;
  }

  public ParameterizedLazyCachingLoader<T, E, C, D> withCache(Function<T, C> cached) {
    this.cached = cached;
    return this;
  }

  public ParameterizedLazyCachingLoader<T, E, C, D> withSupplier(Function<T, E> supplier) {
    this.supplier = supplier;
    return this;
  }

  public ParameterizedLazyCachingLoader<T, E, C, D> build() {
    Objects.requireNonNull(cached, "cached must be non null");
    Objects.requireNonNull(supplier, "supplier must be non null");

    log.info("Init CB {}", name);
    this.circuitBreaker = circuitBreakerRegistry
        .circuitBreaker(name);

    return this;
  }

  public D execute(Object param) {
    @SuppressWarnings("unchecked") final T t = (T) param;
    final C c = this.executeWithCb(t);
    if (LoaderUtils.isEmpty(c)) {
      return mapper.entityToDto(supplier.apply(t));
    } else {
      return mapper.cachedToDto(c);
    }
  }

  protected C executeWithCb(T param) {
    final Supplier<C> recover = SupplierUtils.recover(
        () -> cached.apply(param),
        throwable -> {
          log.error("error during recover cb {}", name, throwable);
          return mapper.entityToCached(supplier.apply(param));
        });

    return this.circuitBreaker.executeSupplier(recover);
  }


}
