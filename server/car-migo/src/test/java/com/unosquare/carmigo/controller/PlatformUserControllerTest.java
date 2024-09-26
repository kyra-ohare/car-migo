package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.VALID_PASSWORD_MESSAGE;
import static com.unosquare.carmigo.util.ResourceUtility.convertObjectToJsonBytes;
import static com.unosquare.carmigo.util.ResourceUtility.getObjectResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.request.PlatformUserRequest;
import com.unosquare.carmigo.dto.response.PlatformUserResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.PlatformUserService;
import com.unosquare.carmigo.util.AuthenticationBeansTestCase;
import jakarta.persistence.EntityExistsException;
import java.util.LinkedHashMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = PlatformUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
public class PlatformUserControllerTest {

  private static final String API_LEADING = "/v1/users";
  private static final String ERROR_MSG = "%s do not match";

  @Autowired private MockMvc mockMvc;
  @MockBean private PlatformUserService platformUserServiceMock;
  @MockBean private AppUser appUserMock;
  @Fixture private AppUser.CurrentAppUser currentAppUserFixture;
  @Fixture private PlatformUserRequest platformUserRequestFixture;
  @Fixture private PlatformUserResponse platformUserResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);

    platformUserRequestFixture.setEmail("test@example.com");
    platformUserRequestFixture.setPassword("Pass1234!");
    when(appUserMock.get()).thenReturn(currentAppUserFixture);
  }

  @SneakyThrows
  @Test
  void createPlatformUserTest() {
    when(platformUserServiceMock.createPlatformUser(any(PlatformUserRequest.class)))
        .thenReturn(platformUserResponseFixture);
    final var response = mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(platformUserRequestFixture)))
        .andExpect(status().isCreated()).andReturn();

    callAssertions(response);
    verify(platformUserServiceMock).createPlatformUser(any(PlatformUserRequest.class));
  }

  @SneakyThrows
  @Test
  void createPlatformUserAgainTest() {
    doThrow(EntityExistsException.class).when(platformUserServiceMock)
        .createPlatformUser(any(PlatformUserRequest.class));
    mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(platformUserRequestFixture)))
        .andExpect(status().isConflict()).andReturn();

    verify(platformUserServiceMock).createPlatformUser(any(PlatformUserRequest.class));
  }

  @SneakyThrows
  @Test
  void createPlatformUserWithBadEmailTest() {
    platformUserRequestFixture.setEmail("bad email");
    doThrow(EntityExistsException.class).when(platformUserServiceMock)
        .createPlatformUser(any(PlatformUserRequest.class));
    final var response = mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(platformUserRequestFixture)))
        .andExpect(status().isBadRequest()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("message"), "Argument not valid: email must be a well-formed email address");
    verify(platformUserServiceMock, times(0)).createPlatformUser(any(PlatformUserRequest.class));
  }

  @SneakyThrows
  @Test
  void createPlatformUserWithBadPasswordTest() {
    platformUserRequestFixture.setPassword("bad password");
    doThrow(EntityExistsException.class).when(platformUserServiceMock)
        .createPlatformUser(any(PlatformUserRequest.class));
    final var response = mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(platformUserRequestFixture)))
        .andExpect(status().isBadRequest()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("message"), "Argument not valid: password " + VALID_PASSWORD_MESSAGE);
    verify(platformUserServiceMock, times(0)).createPlatformUser(any(PlatformUserRequest.class));
  }

  @SneakyThrows
  @Test
  void confirmEmailTest() {
    doNothing().when(platformUserServiceMock).confirmEmail(anyString());
    mockMvc.perform(post(API_LEADING + "/confirm-email")
            .param("email", "my.test@example.com"))
        .andExpect(status().isOk()).andReturn();

    verify(platformUserServiceMock).confirmEmail(anyString());
  }

  @SneakyThrows
  @Test
  void confirmEmailAgainTest() {
    doThrow(IllegalStateException.class).when(platformUserServiceMock).confirmEmail(anyString());
    mockMvc.perform(post(API_LEADING + "/confirm-email")
            .param("email", "my.test@example.com"))
        .andExpect(status().isUnprocessableEntity()).andReturn();

    verify(platformUserServiceMock).confirmEmail(anyString());
  }

  @SneakyThrows
  @Test
  void getCurrentPlatformUserProfileTest() {
    when(platformUserServiceMock.getPlatformUserById(anyInt())).thenReturn(platformUserResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/profile")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    callAssertions(response);
    verify(platformUserServiceMock).getPlatformUserById(anyInt());
  }

  @SneakyThrows
  @Test
  void getPlatformUserByIdTest() {
    when(platformUserServiceMock.getPlatformUserById(anyInt())).thenReturn(platformUserResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    callAssertions(response);
    verify(platformUserServiceMock).getPlatformUserById(anyInt());
  }

  @SneakyThrows
  @Test
  void patchCurrentPlatformUserTest() {
    when(platformUserServiceMock.patchPlatformUserById(anyInt(), any(JsonPatch.class)))
        .thenReturn(platformUserResponseFixture);
    final var response = mockMvc.perform(patch(API_LEADING)
            .contentType("application/json-patch+json")
            .content(patchValidPlatformUser()))
        .andExpect(status().isAccepted()).andReturn();

    callAssertions(response);
    verify(platformUserServiceMock).patchPlatformUserById(anyInt(), any(JsonPatch.class));
  }

  @SneakyThrows
  @Test
  void patchPlatformUserByIdTest() {
    when(platformUserServiceMock.patchPlatformUserById(anyInt(), any(JsonPatch.class))).thenReturn(
        platformUserResponseFixture);
    final var response = mockMvc.perform(patch(API_LEADING + "/1")
            .contentType("application/json-patch+json")
            .content(patchValidPlatformUser()))
        .andExpect(status().isAccepted()).andReturn();

    callAssertions(response);
    verify(platformUserServiceMock).patchPlatformUserById(anyInt(), any(JsonPatch.class));
  }

  @SneakyThrows
  @Test
  void patchInvalidPlatformUserTest() {
    mockMvc.perform(patch(API_LEADING)
            .contentType("application/json-patch+json")
            .content(patchInvalidPlatformUser()))
        .andExpect(status().isBadRequest()).andReturn();

    verify(platformUserServiceMock, times(0)).patchPlatformUserById(anyInt(), any(JsonPatch.class));
  }

  @SneakyThrows
  @Test
  void deleteCurrentPlatformUserTest() {
    doNothing().when(platformUserServiceMock).deletePlatformUserById(anyInt());
    mockMvc.perform(delete(API_LEADING))
        .andExpect(status().isNoContent()).andReturn();

    verify(platformUserServiceMock).deletePlatformUserById(anyInt());
  }

  @SneakyThrows
  @Test
  void deletePlatformUserByIdTest() {
    doNothing().when(platformUserServiceMock).deletePlatformUserById(anyInt());
    mockMvc.perform(delete(API_LEADING + "/1")).andExpect(status().isNoContent()).andReturn();

    verify(platformUserServiceMock).deletePlatformUserById(anyInt());
  }

  @SneakyThrows
  @SuppressWarnings("unchecked cast")
  private void callAssertions(final MvcResult response) {
    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("id"), platformUserResponseFixture.getId(), ERROR_MSG.formatted("Platform user ids"));
    assertEquals(content.get("createdDate"), platformUserResponseFixture.getCreatedDate().toString(),
        ERROR_MSG.formatted("Created dates"));
    assertEquals(content.get("firstName"), platformUserResponseFixture.getFirstName(),
        ERROR_MSG.formatted("First Names"));
    assertEquals(content.get("lastName"), platformUserResponseFixture.getLastName(), ERROR_MSG.formatted("Last names"));
    assertEquals(content.get("dob"), platformUserResponseFixture.getDob().toString(),
        ERROR_MSG.formatted("Last names"));
    assertEquals(content.get("email"), platformUserResponseFixture.getEmail(), ERROR_MSG.formatted("Dobs"));
    assertEquals(content.get("phoneNumber"), platformUserResponseFixture.getPhoneNumber(),
        ERROR_MSG.formatted("Phone Numbers"));
    assertEquals(content.get("driver"), platformUserResponseFixture.isDriver(), ERROR_MSG.formatted("Drivers"));
    assertEquals(content.get("passenger"), platformUserResponseFixture.isPassenger(), ERROR_MSG.formatted("Passengers"));

    final LinkedHashMap<String, Object> userAccessStatusResponse =
        (LinkedHashMap<String, Object>) content.get("userAccessStatus");
    assertEquals(userAccessStatusResponse.get("id"), platformUserResponseFixture.getUserAccessStatus().getId(),
        ERROR_MSG.formatted("User access status ids"));
    assertEquals(userAccessStatusResponse.get("status"), platformUserResponseFixture.getUserAccessStatus().getStatus(),
        ERROR_MSG.formatted("User access statuses"));
  }

  private String patchValidPlatformUser() {
    return """
        [
            {
                "op": "replace",
                "path": "/phoneNumber",
                "value": "02865482233"
            },
            {
                "op": "replace",
                "path": "/userAccessStatus/id",
                "value": "2"
            }
        ]
        """;
  }

  private String patchInvalidPlatformUser() {
    return """
        [
           {
             "op": "dummy",
             "path": "/email",
             "value": "patched@example.com"
           }
         ]
        """;
  }
}
