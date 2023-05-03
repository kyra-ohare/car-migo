package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.model.request.AuthenticationRequest;
import com.unosquare.carmigo.model.response.AuthenticationResponse;
import com.unosquare.carmigo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "Authentication Controller")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
    @Valid @RequestBody final AuthenticationRequest authenticationRequest) {
    final AuthenticationResponse authenticationViewModel =
      authenticationService.createAuthenticationToken(authenticationRequest);
    return new ResponseEntity<>(authenticationViewModel, HttpStatus.CREATED);
  }
}
