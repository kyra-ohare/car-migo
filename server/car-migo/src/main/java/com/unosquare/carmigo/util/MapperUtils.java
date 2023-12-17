package com.unosquare.carmigo.util;

import java.util.List;
import org.modelmapper.ModelMapper;

/**
 * Utility to handle object mapping.
 */
public final class MapperUtils {

  private MapperUtils() {}

  /**
   * Map a List to an object.
   * @param source the List.
   * @param targetClass the object to be mapped to.
   * @param modelMapper ModelMapper reference.
   * @return the converted List.
   */
  public static <S, T> List<T> mapList(
      final List<S> source, final Class<T> targetClass, final ModelMapper modelMapper) {
    return source.stream()
        .map(element -> modelMapper.map(element, targetClass))
        .toList();
  }
}
