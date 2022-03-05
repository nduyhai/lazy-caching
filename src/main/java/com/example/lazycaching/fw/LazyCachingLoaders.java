package com.example.lazycaching.fw;

import com.example.lazycaching.fw.impl.ReadThroughLoader;
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
}
