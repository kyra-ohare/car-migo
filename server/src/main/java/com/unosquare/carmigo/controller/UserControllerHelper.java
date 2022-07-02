package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.response.DriverViewModel;
import com.unosquare.carmigo.model.response.PassengerViewModel;
import com.unosquare.carmigo.model.response.PlatformUserViewModel;
import com.unosquare.carmigo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class UserControllerHelper {

  private final ModelMapper modelMapper;
  private final UserService userService;

  PlatformUserViewModel getPlatformUserById(final int id) {
    final GrabPlatformUserDTO grabPlatformUserDTO = userService.getPlatformUserById(id);
    return modelMapper.map(grabPlatformUserDTO, PlatformUserViewModel.class);
  }

  PlatformUserViewModel patchPlatformUserById(final int id, final JsonPatch patch) {
    final GrabPlatformUserDTO grabPlatformUserDTO = userService.patchPlatformUserById(id, patch);
    return modelMapper.map(grabPlatformUserDTO, PlatformUserViewModel.class);
  }

  void deletePlatformUserById(final int id) {
    userService.deletePlatformUserById(id);
  }

  DriverViewModel getDriverById(final int id) {
    final GrabDriverDTO grabDriverDTO = userService.getDriverById(id);
    return modelMapper.map(grabDriverDTO, DriverViewModel.class);
  }

  DriverViewModel createDriverById(final int id, final CreateDriverViewModel createDriverViewModel) {
    final CreateDriverDTO createDriverDTO = modelMapper.map(createDriverViewModel, CreateDriverDTO.class);
    final GrabDriverDTO driverDTO = userService.createDriverById(id, createDriverDTO);
    return modelMapper.map(driverDTO, DriverViewModel.class);
  }

  void deleteDriverById(final int id) {
    userService.deleteDriverById(id);
  }

  PassengerViewModel getPassengerById(final int id) {
    final GrabPassengerDTO grabPassengerDTO = userService.getPassengerById(id);
    return modelMapper.map(grabPassengerDTO, PassengerViewModel.class);
  }

  PassengerViewModel createPassengerById(final int id) {
    final GrabPassengerDTO passengerDTO = userService.createPassengerById(id);
    return modelMapper.map(passengerDTO, PassengerViewModel.class);
  }

  void deletePassengerById(final int id) {
    userService.deletePassengerById(id);
  }
}
