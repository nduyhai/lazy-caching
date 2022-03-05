package com.example.lazycaching.fw;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import org.springframework.util.CollectionUtils;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.SupplierUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LazyCachingLoaders {

  private LazyCachingLoaders() {
  }

  public static <E, C, D> D of(Supplier<C> cached, Supplier<E> database,
      LazyCachingMapper<E, C, D> mapper) {
    final CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(50)
        .waitDurationInOpenState(Duration.ofMillis(1000))
        .permittedNumberOfCallsInHalfOpenState(2)
        .slidingWindowSize(2)
        .recordExceptions(IOException.class, TimeoutException.class)
        .build();
    final CircuitBreakerRegistry circuitBreakerRegistry =
        CircuitBreakerRegistry.of(circuitBreakerConfig);

    final var cbName = UUID.randomUUID().toString();
    log.info("Init CB {}", cbName);
    final CircuitBreaker circuitBreaker = circuitBreakerRegistry
        .circuitBreaker(cbName);

    final var cbCached = circuitBreaker.decorateSupplier(cached);

    final var recover = SupplierUtils.recover(cbCached,
        throwable -> {
           log.error("error during recover cb {}", cbName, throwable);
          return mapper.entityToCached(database.get());
        });

    final var c = circuitBreaker.executeSupplier(recover);
    if (isEmpty(c)) {
      return mapper.entityToDto(database.get());
    } else {
      return mapper.cachedToDto(c);
    }
  }

  private static boolean isEmpty(Object c) {
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
