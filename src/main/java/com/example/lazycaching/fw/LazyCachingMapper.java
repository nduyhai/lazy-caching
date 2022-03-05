package com.example.lazycaching.fw;

public interface LazyCachingMapper<E, C, D> {

  D entityToDto(E entity);

  D cachedToDto(C cached);

  C entityToCached(E entity);
}
