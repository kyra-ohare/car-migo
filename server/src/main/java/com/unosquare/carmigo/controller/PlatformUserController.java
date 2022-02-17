package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.CreatePassengerDTO;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GetPassengerDTO;
import com.unosquare.carmigo.dto.GetPlatformUserDTO;
import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.request.CreatePlatformUserViewModel;
import com.unosquare.carmigo.model.response.DriverViewModel;
import com.unosquare.carmigo.model.response.PassengerViewModel;
import com.unosquare.carmigo.model.response.PlatformUserViewModel;
import com.unosquare.carmigo.dto.GetDriverDTO;
import com.unosquare.carmigo.service.DriverService;
import com.unosquare.carmigo.service.PassengerService;
import com.unosquare.carmigo.service.PlatformUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class PlatformUserController
{
    final ModelMapper modelMapper;
    final PlatformUserService platformUserService;
    final DriverService driverService;
    final PassengerService passengerService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PlatformUserViewModel> getPlatformUser(@PathVariable final int id)
    {
        final GetPlatformUserDTO platformUserDTO = platformUserService.getPlatformUserById(id);
        final PlatformUserViewModel platformUserViewModel = modelMapper.map(
                platformUserDTO, PlatformUserViewModel.class);
        return ResponseEntity.ok(platformUserViewModel);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlatformUserViewModel> createPlatformUser(
            @RequestBody final CreatePlatformUserViewModel createPlatformUserViewModel)
    {
        final CreatePlatformUserDTO createPlatformUserDTO = modelMapper.map(
                createPlatformUserViewModel, CreatePlatformUserDTO.class);
        final GetPlatformUserDTO platformUserDTO = platformUserService.createPlatformUser(createPlatformUserDTO);
        return ResponseEntity.ok(modelMapper.map(platformUserDTO, PlatformUserViewModel.class));
    }

    // TODO
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> patchPlatformUser(@PathVariable final int id)
    {
        platformUserService.updatePlatformUser(id);
        return ResponseEntity.ok("Accepted");
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deletePlatformUser(@PathVariable final int id)
    {
        platformUserService.deletePlatformUserById(id);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping(value = "/{id}/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DriverViewModel> createDriver(
            @PathVariable final int id, @RequestBody final CreateDriverViewModel createDriverViewModal)
    {
        final CreateDriverDTO createDriverDTO = modelMapper.map(createDriverViewModal, CreateDriverDTO.class);
        final GetDriverDTO driverDTO = driverService.createDriver(id, createDriverDTO);      // return DTO instead
        return ResponseEntity.ok(modelMapper.map(driverDTO, DriverViewModel.class));
    }

    @DeleteMapping(value = "/drivers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteDriver(@PathVariable final int id)
    {
        driverService.deleteDriverById(id);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping(value = "/{id}/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PassengerViewModel> createPassenger(
            @PathVariable final int id, @RequestBody final CreatePlatformUserViewModel createPlatformUserViewModel)
    {
        final CreatePassengerDTO createPassengerDTO =
                modelMapper.map(createPlatformUserViewModel, CreatePassengerDTO.class);
        final GetPassengerDTO passengerDTO = passengerService.createPassenger(id, createPassengerDTO);
        return ResponseEntity.ok(modelMapper.map(passengerDTO, PassengerViewModel.class));
    }

    @DeleteMapping(value = "/passengers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deletePassenger(@PathVariable final int id)
    {
        passengerService.deletePassengerById(id);
        return ResponseEntity.ok("Ok");
    }
}
