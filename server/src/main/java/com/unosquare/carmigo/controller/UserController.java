package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.request.CreateUserViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

//    @Autowired
//    private UserRepository userRepository;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getUsers(@PathVariable final int id) {
        return "Hello user " + id;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody final CreateUserViewModel createUserViewModel) {
        return "User created";
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String patchUser(@PathVariable final int id) {
        return "User " + id + " was patched";
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteUser(@PathVariable final int id) {
        return "User " + id + " was deleted";
    }

    @PostMapping(value = "/{id}/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createDriver(@RequestBody final CreateDriverViewModel createDriverViewModal) {
        return "Driver created";
    }

    @DeleteMapping(value = "/drivers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteDriver(@PathVariable final int id) {
        return "Driver " + id + " was deleted";
    }

    @PostMapping(value = "/{id}/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createPassenger(@PathVariable final int id) {
        return "Passenger created " + id;
    }

    @DeleteMapping(value = "/passengers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deletePassenger(@PathVariable final int id) {
        return "Passenger " + id + " was deleted";
    }
}
