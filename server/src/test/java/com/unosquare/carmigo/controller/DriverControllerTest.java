package com.unosquare.carmigo.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unosquare.carmigo.util.ControllerUtility;
import com.unosquare.carmigo.util.ResourceUtility;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class DriverControllerTest {

  private static final String API_LEADING = "/v1/drivers";
  private static final String POST_DRIVER_VALID_JSON =
      ResourceUtility.generateStringFromResource("jsonAssets/PostDriverValid.json");
  private static final String POST_DRIVER_INVALID_JSON =
      ResourceUtility.generateStringFromResource("jsonAssets/PostDriverInvalid.json");
  private final static String STAGED_USER = "staged@example.com";
  private static final int STAGED_USER_ID = 1;
  private final static String ACTIVE_USER = "active@example.com";
  private static final int ACTIVE_USER_ID = 2;
  private final static String SUSPENDED_USER = "suspended@example.com";
  private static final int SUSPENDED_USER_ID = 3;
  private final static String LOCKED_OUT_USER = "locked_out@example.com";
  private static final int LOCKED_OUT_USER_ID = 4;
  private final static String ADMIN_USER = "admin@example.com";
  private static final int ADMIN_USER_ID = 5;

  private ControllerUtility controllerUtility;
  @Autowired private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    controllerUtility = new ControllerUtility(mockMvc, API_LEADING);
  }

  @Test
  @WithAnonymousUser
  public void testEndpointsWithAnonymousUser() throws Exception {
    testUnauthorizedUsers(status().isForbidden());
  }

  @Test
  @WithMockUser(STAGED_USER)
  public void testEndpointsWithStagedUser() throws Exception {
    testUnauthorizedUsers(status().isBadRequest());
  }

  @Test
  @WithUserDetails(ACTIVE_USER)
  public void testEndpointsWithActiveUser() throws Exception {
    controllerUtility.makeGetRequest(status().isOk());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makeDeleteRequest(status().isNoContent());
    controllerUtility.makeDeleteRequest(status().isNotFound());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makePostRequest(POST_DRIVER_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest(POST_DRIVER_VALID_JSON, status().isConflict());
    controllerUtility.makePostRequest(POST_DRIVER_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_INVALID_JSON,
        status().isBadRequest());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, POST_DRIVER_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, POST_DRIVER_VALID_JSON, status().isForbidden());
  }

  @Test
  @WithUserDetails(SUSPENDED_USER)
  public void testEndpointsWithSuspendedUser() throws Exception {
    testUnauthorizedUsers(status().isBadRequest());
  }

  @Test
  @WithMockUser(LOCKED_OUT_USER)
  public void testEndpointsWithLockedOutUser() throws Exception {
    testUnauthorizedUsers(status().isBadRequest());
  }

  @Test
  @WithUserDetails(ADMIN_USER)
  public void testEndpointsWithAdminUser() throws Exception {
    controllerUtility.makeGetRequest(status().isOk());
    controllerUtility.makeGetRequest("/" + STAGED_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + SUSPENDED_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + LOCKED_OUT_USER_ID, status().isOk());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isOk());

    controllerUtility.makeDeleteRequest(status().isNoContent());
    controllerUtility.makeDeleteRequest(status().isNotFound());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isNotFound());
    controllerUtility.makeDeleteRequest("/" + STAGED_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + STAGED_USER_ID, status().isNotFound());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isNotFound());
    controllerUtility.makeDeleteRequest("/" + SUSPENDED_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + SUSPENDED_USER_ID, status().isNotFound());
    controllerUtility.makeDeleteRequest("/" + LOCKED_OUT_USER_ID, status().isNoContent());
    controllerUtility.makeDeleteRequest("/" + LOCKED_OUT_USER_ID, status().isNotFound());

    controllerUtility.makePostRequest(POST_DRIVER_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest(POST_DRIVER_VALID_JSON, status().isConflict());
    controllerUtility.makePostRequest(POST_DRIVER_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/" + STAGED_USER_ID, POST_DRIVER_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest("/" + STAGED_USER_ID, POST_DRIVER_VALID_JSON, status().isConflict());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_VALID_JSON, status().isConflict());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_INVALID_JSON,
        status().isBadRequest());
    controllerUtility.makePostRequest("/" + SUSPENDED_USER_ID, POST_DRIVER_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest("/" + SUSPENDED_USER_ID, POST_DRIVER_VALID_JSON, status().isConflict());
    controllerUtility.makePostRequest("/" + SUSPENDED_USER_ID, POST_DRIVER_INVALID_JSON,
        status().isBadRequest());
    controllerUtility.makePostRequest("/" + LOCKED_OUT_USER_ID, POST_DRIVER_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest("/" + LOCKED_OUT_USER_ID, POST_DRIVER_VALID_JSON,
        status().isConflict());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, POST_DRIVER_VALID_JSON, status().isConflict());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, POST_DRIVER_INVALID_JSON, status().isBadRequest());
  }

  private void testUnauthorizedUsers(final ResultMatcher expectation) throws Exception {
    controllerUtility.makeGetRequest(status().isForbidden());
    controllerUtility.makeGetRequest("/" + STAGED_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + SUSPENDED_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + LOCKED_OUT_USER_ID, status().isForbidden());
    controllerUtility.makeGetRequest("/" + ADMIN_USER_ID, status().isForbidden());

    controllerUtility.makePostRequest(POST_DRIVER_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest(POST_DRIVER_INVALID_JSON, expectation);
    controllerUtility.makePostRequest("/" + STAGED_USER_ID, POST_DRIVER_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/" + ACTIVE_USER_ID, POST_DRIVER_INVALID_JSON, expectation);
    controllerUtility.makePostRequest("/" + SUSPENDED_USER_ID, POST_DRIVER_VALID_JSON,
        status().isForbidden());
    controllerUtility.makePostRequest("/" + LOCKED_OUT_USER_ID, POST_DRIVER_VALID_JSON,
        status().isForbidden());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, POST_DRIVER_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/" + ADMIN_USER_ID, POST_DRIVER_INVALID_JSON, expectation);

    controllerUtility.makeDeleteRequest(status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + STAGED_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ACTIVE_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + SUSPENDED_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + LOCKED_OUT_USER_ID, status().isForbidden());
    controllerUtility.makeDeleteRequest("/" + ADMIN_USER_ID, status().isForbidden());
  }
}
