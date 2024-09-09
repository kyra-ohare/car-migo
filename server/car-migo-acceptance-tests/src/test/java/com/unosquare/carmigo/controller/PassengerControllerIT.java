package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.Constants.ACTIVE_USER;
import static com.unosquare.carmigo.util.Constants.ACTIVE_USER_ID;
import static com.unosquare.carmigo.util.Constants.ADMIN_USER;
import static com.unosquare.carmigo.util.Constants.ADMIN_USER_ID;
import static com.unosquare.carmigo.util.Constants.LOCKED_OUT_USER;
import static com.unosquare.carmigo.util.Constants.LOCKED_OUT_USER_ID;
import static com.unosquare.carmigo.util.Constants.STAGED_USER;
import static com.unosquare.carmigo.util.Constants.STAGED_USER_ID;
import static com.unosquare.carmigo.util.Constants.SUSPENDED_USER;
import static com.unosquare.carmigo.util.Constants.SUSPENDED_USER_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unosquare.carmigo.config.TestRedisConfiguration;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.util.ControllerUtility;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("h2")
@SpringBootTest(classes = TestRedisConfiguration.class)
public class PassengerControllerIT {

  private static final String API_LEADING = "/v1/passengers";

  private ControllerUtility controllerUtility;
  @Autowired private MockMvc mockMvc;
  @Autowired private PassengerRepository passengerRepository;

  @BeforeEach
  public void setUp() {
    controllerUtility = new ControllerUtility(mockMvc, API_LEADING);

    STAGED_USER_ID = reassignEntityId(STAGED_USER);
    ACTIVE_USER_ID = reassignEntityId(ACTIVE_USER);
    SUSPENDED_USER_ID = reassignEntityId(SUSPENDED_USER);
    LOCKED_OUT_USER_ID = reassignEntityId(LOCKED_OUT_USER);
    ADMIN_USER_ID = reassignEntityId(ADMIN_USER);
  }

  @Test
  @WithAnonymousUser
  public void testEndpointsWithAnonymousUser() throws Exception {
    testUnauthorizedUsers();
  }

  @Test
  @WithMockUser(STAGED_USER)
  public void testEndpointsWithStagedUser() throws Exception {
    testUnauthorizedUsers();
  }

  @Test
  @WithUserDetails(ACTIVE_USER)
  public void testEndpointsWithActiveUser() throws Exception {
    controllerUtility.makeGetRequest("/profile", status().isOk());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makeDeleteRequest("", status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makePostRequest("/create", "", status().isCreated());
    controllerUtility.makePostRequest("/create", "", status().isConflict());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, "", status().isForbidden());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, "", status().isForbidden());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, "", status().isForbidden());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, "", status().isForbidden());
  }

  @Test
  @WithUserDetails(SUSPENDED_USER)
  public void testEndpointsWithSuspendedUser() throws Exception {
    controllerUtility.makeGetRequest("/profile", status().isOk());
    testUnauthorizedUsers();
  }

  @Test
  @WithMockUser(LOCKED_OUT_USER)
  public void testEndpointsWithLockedOutUser() throws Exception {
    testUnauthorizedUsers();
  }

  @Test
  @WithUserDetails(ADMIN_USER)
  public void testEndpointsWithAdminUser() throws Exception {
    controllerUtility.makeGetRequest("/profile", status().isOk());
    controllerUtility.makeGetRequest("/" + STAGED_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + SUSPENDED_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + LOCKED_OUT_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isOk());

    controllerUtility.makeDeleteRequest("", status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + STAGED_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + SUSPENDED_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + LOCKED_OUT_USER_ID, status().isNoContent());

    controllerUtility.makePostRequest("/create", "", status().isCreated());
    controllerUtility.makePostRequest("/create", "", status().isConflict());
    controllerUtility.makePostRequest("/" + STAGED_USER_ID, "", status().isCreated());
    controllerUtility.makePostRequest("/" + STAGED_USER_ID, "", status().isConflict());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, "", status().isCreated());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, "", status().isConflict());
    controllerUtility.makePostRequest("/" + SUSPENDED_USER_ID, "", status().isCreated());
    controllerUtility.makePostRequest("/" + SUSPENDED_USER_ID, "", status().isConflict());
    controllerUtility.makePostRequest("/" + LOCKED_OUT_USER_ID, "", status().isCreated());
    controllerUtility.makePostRequest("/" + LOCKED_OUT_USER_ID, "", status().isConflict());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, "", status().isConflict());
  }

  private void testUnauthorizedUsers() throws Exception {
    controllerUtility.makeGetRequest("/" + STAGED_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + SUSPENDED_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + LOCKED_OUT_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makePostRequest("/create", "", status().isForbidden());
    controllerUtility.makePostRequest("/" + STAGED_USER_ID, "", status().isForbidden());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, "", status().isForbidden());
    controllerUtility.makePostRequest("/" + SUSPENDED_USER_ID, "", status().isForbidden());
    controllerUtility.makePostRequest("/" + LOCKED_OUT_USER_ID, "", status().isForbidden());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, "", status().isForbidden());

    controllerUtility.makeDeleteRequest("", status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + STAGED_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + SUSPENDED_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + LOCKED_OUT_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isForbidden());
  }

  private int reassignEntityId(final String email) {
    final PlatformUser platformUser = new PlatformUser();
    platformUser.setEmail(email);
    final Passenger passenger = new Passenger();
    passenger.setPlatformUser(platformUser);

    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
        .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact())
        .withIgnorePaths("id");

    Optional<Passenger> result = passengerRepository.findOne(Example.of(passenger, exampleMatcher));
    return result.orElseThrow(EntityNotFoundException::new).getId();
  }
}
