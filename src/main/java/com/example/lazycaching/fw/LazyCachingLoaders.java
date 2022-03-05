package com.example.lazycaching.fw;

import com.example.lazycaching.fw.impl.ParameterizedReadThroughLoader;
import com.example.lazycaching.fw.impl.ReadThroughLoader;
import com.example.lazycaching.fw.impl.RefreshParameterizedReadThroughLoader;
import com.example.lazycaching.fw.impl.RefreshReadThroughLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LazyCachingLoaders {

  private LazyCachingLoaders() {
  }

  public static <E, C, D> LazyCachingLoader<E, C, D> readThrough(String name,
      LazyCachingMapper<E, C, D> mapper) {
    return new ReadThroughLoader<>(name, mapper);
  }

  public static <E, C, D> LazyCachingLoader<E, C, D> refreshReadThrough(String name,
      LazyCachingMapper<E, C, D> mapper) {
    return new RefreshReadThroughLoader<>(name, mapper);
  }

  public static <T, E, C, D> ParameterizedLazyCachingLoader<T, E, C, D> parameterizedReadThrough(
      String name,
      LazyCachingMapper<E, C, D> mapper) {
    return new ParameterizedReadThroughLoader<>(name, mapper);
  }

  public static <T, E, C, D> ParameterizedLazyCachingLoader<T, E, C, D> parameterizedRefreshReadThrough(
      String name,
      LazyCachingMapper<E, C, D> mapper) {
    return new RefreshParameterizedReadThroughLoader<>(name, mapper);
  }

}
