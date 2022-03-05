package com.example.lazycaching.fw.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.util.CollectionUtils;

final class LoaderUtils {

  private LoaderUtils() {
  }

  public static boolean isEmpty(Object c) {
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
