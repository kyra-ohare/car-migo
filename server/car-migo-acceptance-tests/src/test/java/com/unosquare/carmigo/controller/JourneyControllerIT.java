package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.Constants.ACTIVE_USER;
import static com.unosquare.carmigo.util.Constants.ADMIN_USER;
import static com.unosquare.carmigo.util.Constants.LOCKED_OUT_USER;
import static com.unosquare.carmigo.util.Constants.PATCH_JOURNEY_INVALID_JSON;
import static com.unosquare.carmigo.util.Constants.PATCH_JOURNEY_VALID_JSON;
import static com.unosquare.carmigo.util.Constants.POST_JOURNEY_INVALID_JSON;
import static com.unosquare.carmigo.util.Constants.POST_JOURNEY_VALID_JSON;
import static com.unosquare.carmigo.util.Constants.STAGED_USER;
import static com.unosquare.carmigo.util.Constants.SUSPENDED_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unosquare.carmigo.util.ControllerUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class JourneyControllerIT {

  private static final String API_LEADING = "/v1/journeys";

  @Autowired private MockMvc mockMvc;
  private ControllerUtility controllerUtility;

  @BeforeEach
  public void setUp() {
    controllerUtility = new ControllerUtility(mockMvc, API_LEADING);
  }

  @Test
  @WithAnonymousUser
  public void testSearchJourneysWithAnonymousUser() throws Exception {
    mockMvc.perform(get(API_LEADING + "/search")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationIdFrom", "1")
            .param("locationIdTo", "2")
            .param("dateTimeFrom", "2021-11-30T09:00:00Z")
            .param("dateTimeTo", "2023-12-01T09:00:00Z"))
        .andExpect(status().isOk());

    mockMvc.perform(get(API_LEADING + "/search")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationIdFrom", "1")
            .param("locationIdTo", "2")
            .param("dateTimeFrom", "2022-12-01T09:00:00Z")
            .param("dateTimeTo", "2023-12-01T09:00:00Z"))
        .andExpect(status().isNotFound());

    mockMvc.perform(get(API_LEADING + "/search")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationIdFrom", "1")
            .param("locationIdTo", "2"))
        .andExpect(status().isBadRequest());

    testUnauthorizedUsers(status().isForbidden());
  }

  @Test
  @WithAnonymousUser
  public void testCalculateDistanceWithAnonymousUser() throws Exception {
  /*
    This is commented because it may incur costs with RapidApi
    https://rapidapi.com/Distance.to/api/distance/pricing?utm_source=api-quota-85&utm_medium=email&utm_campaign=Distance
  */
//    mockMvc.perform(get(API_LEADING + "/calculateDistance")
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .param("locationFrom", "Belfast")
//            .param("countryFrom", "GBR")
//            .param("locationTo", "Newry")
//            .param("countryTo", "GBR"))
//        .andExpect(status().isOk());

    mockMvc.perform(get(API_LEADING + "/calculateDistance")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationFrom", "Belfast")
            .param("countryFrom", "GBR"))
        .andExpect(status().isBadRequest());

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
    controllerUtility.makeGetRequest("/foo", status().isBadRequest());
    controllerUtility.makeGetRequest("/drivers/my-journeys", status().isOk());
    controllerUtility.makeGetRequest("/drivers/1", status().isForbidden());
    controllerUtility.makeGetRequest("/passengers/my-journeys", status().isOk());
    controllerUtility.makeGetRequest("/passengers/1", status().isForbidden());
    controllerUtility.makePostRequest("", POST_JOURNEY_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest("", POST_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/1/add-passenger", "", status().isConflict());
    controllerUtility.makePostRequest("/2/add-passenger", "", status().isOk());
    controllerUtility.makePostRequest("/2/add-passenger", "", status().isConflict());
    controllerUtility.makeDeleteRequest("/2/remove-passenger", status().isNoContent());
    controllerUtility.makeDeleteRequest("/2/remove-passenger", status().isNotFound());
    controllerUtility.makePostRequest("/1/add-passenger/1", "", status().isForbidden());
    controllerUtility.makeDeleteRequest("/1/remove-passenger/1", status().isForbidden());
    controllerUtility.makePatchRequest("/3", PATCH_JOURNEY_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makeDeleteRequest("/1", status().isForbidden());
    controllerUtility.makeDeleteRequest("/6", status().isNoContent());
    controllerUtility.makeDeleteRequest("/6", status().isNotFound());
  }

  @Test
  @WithUserDetails(SUSPENDED_USER)
  public void testEndpointsWithSuspendedUser() throws Exception {
    controllerUtility.makeGetRequest("/foo", status().isBadRequest());
    controllerUtility.makeGetRequest("/1", status().isForbidden());
    controllerUtility.makeGetRequest("/3", status().isForbidden());
    controllerUtility.makeGetRequest("/drivers/my-journeys", status().isOk());
    controllerUtility.makeGetRequest("/drivers/1", status().isForbidden());
    controllerUtility.makeGetRequest("/passengers/my-journeys", status().isOk());
    controllerUtility.makeGetRequest("/passengers/1", status().isForbidden());
    controllerUtility.makePostRequest("", POST_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("", POST_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/1/add-passenger", "", status().isForbidden());
    controllerUtility.makePostRequest("/3/add-passenger", "", status().isForbidden());
    controllerUtility.makeDeleteRequest("/1/remove-passenger", status().isForbidden());
    controllerUtility.makeDeleteRequest("/3/remove-passenger", status().isForbidden());
    controllerUtility.makePostRequest("/1/add-passenger/3", "", status().isForbidden());
    controllerUtility.makeDeleteRequest("/1/remove-passenger/3", status().isForbidden());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/4", PATCH_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makeDeleteRequest("/4", status().isForbidden());
    controllerUtility.makeDeleteRequest("/5", status().isForbidden());
  }

  @Test
  @WithMockUser(LOCKED_OUT_USER)
  public void testEndpointsWithLockedOutUser() throws Exception {
    testUnauthorizedUsers(status().isBadRequest());
  }

  @Test
  @WithUserDetails(ADMIN_USER)
  public void testEndpointsWithAdminUser() throws Exception {
    controllerUtility.makeGetRequest("/foo", status().isBadRequest());
    controllerUtility.makeGetRequest("/1", status().isOk());
    controllerUtility.makeGetRequest("/2", status().isOk());
    controllerUtility.makeGetRequest("/drivers/my-journeys", status().isOk());
    controllerUtility.makeGetRequest("/drivers/1", status().isOk());
    controllerUtility.makeGetRequest("/drivers/2", status().isOk());
    controllerUtility.makeGetRequest("/passengers/my-journeys", status().isOk());
    controllerUtility.makeGetRequest("/passengers/1", status().isOk());
    controllerUtility.makeGetRequest("/passengers/2", status().isOk());
    controllerUtility.makePostRequest("", POST_JOURNEY_VALID_JSON, status().isCreated());
    controllerUtility.makeGetRequest("/drivers/my-journeys", status().isOk());
    controllerUtility.makePostRequest("", POST_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_VALID_JSON, status().isCreated());
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makePostRequest("/2/add-passenger", "", status().isConflict());
    controllerUtility.makePostRequest("/3/add-passenger", "", status().isOk());
    controllerUtility.makePostRequest("/3/add-passenger", "", status().isConflict());
    controllerUtility.makeDeleteRequest("/3/remove-passenger", status().isNoContent());
    controllerUtility.makeDeleteRequest("/3/remove-passenger", status().isNotFound());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/3", PATCH_JOURNEY_VALID_JSON, status().isAccepted());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_INVALID_JSON, status().isBadRequest());
    controllerUtility.makeDeleteRequest("/5", status().isNoContent());
    controllerUtility.makeDeleteRequest("/5", status().isNotFound());
    controllerUtility.makeDeleteRequest("/7", status().isNoContent());
    controllerUtility.makeDeleteRequest("/7", status().isNotFound());
  }

  private void testUnauthorizedUsers(final ResultMatcher expectation) throws Exception {
    controllerUtility.makeGetRequest("/foo", expectation);
    controllerUtility.makeGetRequest("/1", status().isForbidden());
    controllerUtility.makeGetRequest("/3", status().isForbidden());
    controllerUtility.makeGetRequest("/drivers/my-journeys", status().isForbidden());
    controllerUtility.makeGetRequest("/drivers/1", status().isForbidden());
    controllerUtility.makeGetRequest("/passengers/my-journeys", status().isForbidden());
    controllerUtility.makeGetRequest("/passengers/1", status().isForbidden());
    controllerUtility.makePostRequest("", POST_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("", POST_JOURNEY_INVALID_JSON, expectation);
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePostRequest("/drivers/1", POST_JOURNEY_INVALID_JSON, expectation);
    controllerUtility.makePostRequest("/1/add-passenger", "", status().isForbidden());
    controllerUtility.makePostRequest("/3/add-passenger", "", status().isForbidden());
    controllerUtility.makeDeleteRequest("/1/remove-passenger", status().isForbidden());
    controllerUtility.makeDeleteRequest("/3/remove-passenger", status().isForbidden());
    controllerUtility.makePostRequest("/1/add-passenger/3", "", status().isForbidden());
    controllerUtility.makeDeleteRequest("/1/remove-passenger/3", status().isForbidden());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/4", PATCH_JOURNEY_VALID_JSON, status().isForbidden());
    controllerUtility.makePatchRequest("/1", PATCH_JOURNEY_INVALID_JSON, expectation);
    controllerUtility.makeDeleteRequest("/4", status().isForbidden());
    controllerUtility.makeDeleteRequest("/5", status().isForbidden());
  }
}
