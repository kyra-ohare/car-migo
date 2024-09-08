package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.Constants.ACTIVE_USER;
import static com.unosquare.carmigo.util.Constants.ACTIVE_USER_ID;
import static com.unosquare.carmigo.util.Constants.ADMIN_USER;
import static com.unosquare.carmigo.util.Constants.ADMIN_USER_ID;
import static com.unosquare.carmigo.util.Constants.LOCKED_OUT_USER;
import static com.unosquare.carmigo.util.Constants.LOCKED_OUT_USER_ID;
import static com.unosquare.carmigo.util.Constants.PATCH_PLATFORM_USER_INVALID_JSON;
import static com.unosquare.carmigo.util.Constants.PATCH_PLATFORM_USER_VALID_JSON;
import static com.unosquare.carmigo.util.Constants.POST_PLATFORM_USER_INVALID_JSON;
import static com.unosquare.carmigo.util.Constants.POST_PLATFORM_USER_VALID_JSON;
import static com.unosquare.carmigo.util.Constants.STAGED_USER;
import static com.unosquare.carmigo.util.Constants.STAGED_USER_ID;
import static com.unosquare.carmigo.util.Constants.SUSPENDED_USER;
import static com.unosquare.carmigo.util.Constants.SUSPENDED_USER_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unosquare.carmigo.config.TestRedisConfiguration;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.repository.DriverRepository;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import com.unosquare.carmigo.util.ControllerUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@AutoConfigureMockMvc
@ActiveProfiles("h2")
@SpringBootTest(classes = TestRedisConfiguration.class)
public class PlatformUserControllerIT {

  private static final String API_LEADING = "/v1/users";

  @Autowired private MockMvc mockMvc;
  @Autowired private PlatformUserRepository platformUserRepository;
  @Autowired private DriverRepository driverRepository;
  @Autowired private PassengerRepository passengerRepository;
  @Autowired private EntityManager entityManager;

  private ControllerUtility controllerUtility;

  @BeforeEach
  public void setUp() {
    controllerUtility = new ControllerUtility(mockMvc, API_LEADING);
  }

  @Test
  @WithAnonymousUser
  public void testEndpointsWithAnonymousUser() throws Exception {
    controllerUtility.makePostRequest("/create", POST_PLATFORM_USER_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest("/create", POST_PLATFORM_USER_VALID_JSON, status().isConflict());
    controllerUtility.makePostRequest("/create", POST_PLATFORM_USER_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/confirm-email", "", status().isBadRequest());
    controllerUtility.makePostRequestWithOneParam("/confirm-email", "email", "", status().isNotFound());
    controllerUtility.makePostRequestWithOneParam("/confirm-email", "email", "staged@example.com", status().isOk());
    controllerUtility.makePostRequestWithOneParam(
        "/confirm-email", "email", "staged@example.com", status().isConflict());
    controllerUtility.makePostRequestWithOneParam(
        "/confirm-email", "email", "fake-staged@example.com", status().isNotFound());
    controllerUtility.makePostRequestWithOneParam(
        "/confirm-email", "email", "active@example.com", status().isConflict());
    testUnauthorizedUsersUltra(status().isForbidden());
  }

  @Test
  @WithMockUser(STAGED_USER)
  public void testEndpointsWithStagedUser() throws Exception {
    testUnauthorizedUsersUltra(status().isBadRequest());
  }

  @Test
  @WithUserDetails(ACTIVE_USER)
  public void testEndpointsWithActiveUser() throws Exception {
    controllerUtility.makeGetRequest("/profile", status().isOk());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makePatchRequest("", PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("", PATCH_PLATFORM_USER_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePatchRequest("/" + ACTIVE_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/" + ADMIN_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isForbidden());

    controllerUtility.makeDeleteRequest("", status().isNoContent());
    recreateEntities(ACTIVE_USER);
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isForbidden());
  }

  @Test
  @WithUserDetails(SUSPENDED_USER)
  public void testEndpointsWithSuspendedUser() throws Exception {
    controllerUtility.makeGetRequest("/profile", status().isOk());
    controllerUtility.makePatchRequest("", PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    testUnauthorizedUsers(status().isBadRequest());
  }

  @Test
  @WithMockUser(LOCKED_OUT_USER)
  public void testEndpointsWithLockedOutUser() throws Exception {
    testUnauthorizedUsersUltra(status().isBadRequest());
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

    controllerUtility.makePatchRequest("", PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("", PATCH_PLATFORM_USER_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePatchRequest("/" + STAGED_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/" + ACTIVE_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/" + ACTIVE_USER_ID, PATCH_PLATFORM_USER_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePatchRequest("/" + SUSPENDED_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/" + LOCKED_OUT_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/" + ADMIN_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/" + ADMIN_USER_ID, PATCH_PLATFORM_USER_INVALID_JSON, status().isBadRequest());

    controllerUtility.makeDeleteRequest("", status().isNoContent());
    recreateEntities(ADMIN_USER);
    controllerUtility.makeDeleteRequest("/" + STAGED_USER_ID, status().isNoContent());
    recreateEntities(STAGED_USER);
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isNoContent());
    recreateEntities(ACTIVE_USER);
    controllerUtility.makeDeleteRequest("/" + SUSPENDED_USER_ID, status().isNoContent());
    recreateEntities(SUSPENDED_USER);
    controllerUtility.makeDeleteRequest("/" + LOCKED_OUT_USER_ID, status().isNoContent());
    recreateEntities(LOCKED_OUT_USER);
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isNoContent());
    recreateEntities(ADMIN_USER);
  }

  private void testUnauthorizedUsersUltra(final ResultMatcher expectation) throws Exception {
    controllerUtility.makeGetRequest("/profile", status().isForbidden());
    controllerUtility.makePatchRequest("", PATCH_PLATFORM_USER_VALID_JSON, status().isForbidden());
    testUnauthorizedUsers(expectation);
  }

  private void testUnauthorizedUsers(final ResultMatcher expectation) throws Exception {
    controllerUtility.makeGetRequest("/" + STAGED_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + SUSPENDED_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + LOCKED_OUT_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makePatchRequest("", PATCH_PLATFORM_USER_INVALID_JSON, expectation);
    controllerUtility.makePatchRequest("/" + STAGED_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/" + ACTIVE_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/" + ACTIVE_USER_ID, PATCH_PLATFORM_USER_INVALID_JSON, expectation);
    controllerUtility.makePatchRequest("/" + SUSPENDED_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/" + LOCKED_OUT_USER_ID, PATCH_PLATFORM_USER_VALID_JSON,
        status().isForbidden());
    controllerUtility.makePatchRequest("/" + ADMIN_USER_ID, PATCH_PLATFORM_USER_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/" + ADMIN_USER_ID, PATCH_PLATFORM_USER_INVALID_JSON, expectation);

    controllerUtility.makeDeleteRequest("", status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + STAGED_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + SUSPENDED_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + LOCKED_OUT_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isForbidden());
  }

  private void recreateEntities(final String email) {
    final PlatformUser platformUser;
    switch (email) {
      case "staged@example.com" -> {
        platformUser = recreatePlatformUser(1, email);
        recreateDriver(platformUser);
        recreatePassenger(platformUser);
        STAGED_USER_ID = platformUser.getId();
      }
      case "active@example.com" -> {
        platformUser = recreatePlatformUser(2, email);
        recreateDriver(platformUser);
        recreatePassenger(platformUser);
        ACTIVE_USER_ID = platformUser.getId();
      }
      case "suspended@example.com" -> {
        platformUser = recreatePlatformUser(3, email);
        recreateDriver(platformUser);
        recreatePassenger(platformUser);
        SUSPENDED_USER_ID = platformUser.getId();
      }
      case "locked_out@example.com" -> {
        platformUser = recreatePlatformUser(4, email);
        recreateDriver(platformUser);
        recreatePassenger(platformUser);
        LOCKED_OUT_USER_ID = platformUser.getId();
      }
      case "admin@example.com" -> {
        platformUser = recreatePlatformUser(5, email);
        recreateDriver(platformUser);
        recreatePassenger(platformUser);
        ADMIN_USER_ID = platformUser.getId();
      }
      default -> throw new EntityNotFoundException("Not possible to create entity for %s".formatted(email));
    }
  }

  private PlatformUser recreatePlatformUser(final int userAccessStatusId, final String email) {
    final PlatformUser platformUser = new PlatformUser();
    platformUser.setCreatedDate(Instant.now());
    platformUser.setFirstName("Foo");
    platformUser.setLastName("Foo");
    platformUser.setDob(Instant.now());
    platformUser.setEmail(email);
    platformUser.setPassword("foo");
    platformUser.setPhoneNumber("foo");
    platformUser.setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, userAccessStatusId));
    return platformUserRepository.save(platformUser);
  }

  private void recreateDriver(final PlatformUser platformUser) {
    final Driver driver = new Driver();
    driver.setId(platformUser.getId());
    driver.setLicenseNumber("11111");
    driver.setPlatformUser(platformUser);
    driverRepository.save(driver);
  }

  private void recreatePassenger(final PlatformUser platformUser) {
    final Passenger passenger = new Passenger();
    passenger.setId(platformUser.getId());
    passenger.setPlatformUser(platformUser);
    passengerRepository.save(passenger);
  }
}
