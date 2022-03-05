package com.example.lazycaching.fw;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @param <T> Parameterized
 * @param <E> Entity
 * @param <C> Cache
 * @param <D> DTO - response
 */
public interface ParameterizedLazyCachingLoader<T, E, C, D> {

  ParameterizedLazyCachingLoader<T, E, C, D> withCache(Function<T, C> cached);

  default ParameterizedLazyCachingLoader<T, E, C, D> withCacheUpdater(Consumer<C> cacheUpdater) {
    return this;
  }

  ParameterizedLazyCachingLoader<T, E, C, D> withSupplier(Function<T, E> supplier);

  ParameterizedLazyCachingLoader<T, E, C, D> build();

  D execute(Object param);
}
