package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.ResourceUtility.getObjectResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.dto.response.PassengerResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.PassengerService;
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

@WebMvcTest(controllers = PassengerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
public class PassengerControllerTest {

  private static final String API_LEADING = "/v1/passengers";
  private static final String ERROR_MSG = "%s do not match";

  @Autowired private MockMvc mockMvc;
  @MockBean private PassengerService passengerServiceMock;
  @MockBean private AppUser appUserMock;
  @Fixture private AppUser.CurrentAppUser currentAppUserFixture;
  @Fixture private PassengerResponse passengerResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);

    when(appUserMock.get()).thenReturn(currentAppUserFixture);
  }

  @SneakyThrows
  @Test
  void createPassengerTest() {
    when(passengerServiceMock.createPassengerById(anyInt())).thenReturn(passengerResponseFixture);
    final var response = mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated()).andReturn();

    callAssertions(response);
    verify(passengerServiceMock).createPassengerById(anyInt());
  }

  @SneakyThrows
  @Test
  void createPassengerAgainTest() {
    doThrow(EntityExistsException.class).when(passengerServiceMock).createPassengerById(anyInt());
    mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isConflict()).andReturn();

    verify(passengerServiceMock).createPassengerById(anyInt());
  }

  @SneakyThrows
  @Test
  void createPassengerByIdTest() {
    when(passengerServiceMock.createPassengerById(anyInt())).thenReturn(passengerResponseFixture);
    final var response = mockMvc.perform(post(API_LEADING + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated()).andReturn();

    callAssertions(response);
    verify(passengerServiceMock).createPassengerById(anyInt());
  }

  @SneakyThrows
  @Test
  void createPassengerByIdAgainTest() {
    doThrow(EntityExistsException.class).when(passengerServiceMock).createPassengerById(anyInt());
    mockMvc.perform(post(API_LEADING + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isConflict()).andReturn();

    verify(passengerServiceMock).createPassengerById(anyInt());
  }

  @SneakyThrows
  @Test
  void getCurrentPassengerProfileTest() {
    when(passengerServiceMock.getPassengerById(anyInt())).thenReturn(passengerResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/profile"))
        .andExpect(status().isOk()).andReturn();

    callAssertions(response);
    verify(passengerServiceMock).getPassengerById(anyInt());
  }

  @SneakyThrows
  @Test
  void getPassengerByIdTest() {
    when(passengerServiceMock.getPassengerById(anyInt())).thenReturn(passengerResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/1"))
        .andExpect(status().isOk()).andReturn();

    callAssertions(response);
    verify(passengerServiceMock).getPassengerById(anyInt());
  }

  @SneakyThrows
  @Test
  void deleteCurrentPassengerTest() {
    doNothing().when(passengerServiceMock).deletePassengerById(anyInt());
    mockMvc.perform(delete(API_LEADING + "/1"))
        .andExpect(status().isNoContent()).andReturn();

    verify(passengerServiceMock).deletePassengerById(anyInt());
  }

  @SneakyThrows
  @SuppressWarnings("unchecked cast")
  private void callAssertions(final MvcResult response) {
    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("id"), passengerResponseFixture.getId(), ERROR_MSG.formatted("Passenger ids"));

    final LinkedHashMap<String, Object> platformUserResponse =
        (LinkedHashMap<String, Object>) content.get("platformUser");
    assertEquals(platformUserResponse.get("id"), passengerResponseFixture.getPlatformUser().getId(),
        ERROR_MSG.formatted("Platform User Ids"));
    assertEquals(platformUserResponse.get("createdDate"),
        passengerResponseFixture.getPlatformUser().getCreatedDate().toString(), ERROR_MSG.formatted("Created dates"));
    assertEquals(platformUserResponse.get("firstName"), passengerResponseFixture.getPlatformUser().getFirstName(),
        ERROR_MSG.formatted("First Names"));
    assertEquals(platformUserResponse.get("lastName"), passengerResponseFixture.getPlatformUser().getLastName(),
        ERROR_MSG.formatted("Last names"));
    assertEquals(platformUserResponse.get("dob"), passengerResponseFixture.getPlatformUser().getDob().toString(),
        ERROR_MSG.formatted("Last names"));
    assertEquals(platformUserResponse.get("email"), passengerResponseFixture.getPlatformUser().getEmail(),
        ERROR_MSG.formatted("Dobs"));
    assertEquals(platformUserResponse.get("phoneNumber"), passengerResponseFixture.getPlatformUser().getPhoneNumber(),
        ERROR_MSG.formatted("Phone Numbers"));

    final LinkedHashMap<String, Object> userAccessStatusResponse =
        (LinkedHashMap<String, Object>) platformUserResponse.get("userAccessStatus");
    assertEquals(userAccessStatusResponse.get("id"),
        passengerResponseFixture.getPlatformUser().getUserAccessStatus().getId(),
        ERROR_MSG.formatted("User access status ids"));
    assertEquals(userAccessStatusResponse.get("status"),
        passengerResponseFixture.getPlatformUser().getUserAccessStatus().getStatus(),
        ERROR_MSG.formatted("User access statuses"));
  }
}
