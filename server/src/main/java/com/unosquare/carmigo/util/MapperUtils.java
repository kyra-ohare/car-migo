package com.unosquare.carmigo.util;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public class MapperUtils {

  private MapperUtils() {}

  public static <S, T> List<T> mapList(
      final List<S> source, final Class<T> targetClass, final ModelMapper modelMapper) {
    return source.stream()
        .map(element -> modelMapper.map(element, targetClass))
        .collect(Collectors.toList());
  }
}
