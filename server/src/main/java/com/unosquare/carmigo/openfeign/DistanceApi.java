package com.unosquare.carmigo.openfeign;

import com.unosquare.carmigo.configuration.OpenFeignDistanceConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "distance",
    url = "${open-feign.distance.endpoint}",
    configuration = OpenFeignDistanceConfiguration.class,
    fallback = DistanceApiFallback.class)
public interface DistanceApi {

  @GetMapping(value = "?route={route}")
  DistanceHolder getDistance(@PathVariable final String route);
}
