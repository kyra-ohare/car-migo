package com.unosquare.carmigo.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Static utility methods.
 */
public final class CommonBehaviours {

  public static <E> E findEntityById(
      final int entityId, final JpaRepository<E, Integer> repository, final String errorMsg) {
    return repository.findById(entityId).orElseThrow(
        () -> new EntityNotFoundException(errorMsg));
  }
}
