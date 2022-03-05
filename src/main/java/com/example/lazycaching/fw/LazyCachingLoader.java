package com.example.lazycaching.fw;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface LazyCachingLoader<E, C, D> {

  LazyCachingLoader<E, C, D> withCache(Supplier<C> cached);

  default LazyCachingLoader<E, C, D> withCacheUpdater(Consumer<C> cacheUpdater) {
    return this;
  }

  LazyCachingLoader<E, C, D> withSupplier(Supplier<E> supplier);

  LazyCachingLoader<E, C, D> build();

  D execute();
}
