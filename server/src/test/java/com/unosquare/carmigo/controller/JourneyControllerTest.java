package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.ResourceUtility.convertObjectToJsonBytes;
import static com.unosquare.carmigo.util.ResourceUtility.getObjectResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.unosquare.carmigo.dto.request.DistanceRequest;
import com.unosquare.carmigo.dto.request.JourneyRequest;
import com.unosquare.carmigo.dto.request.SearchJourneysRequest;
import com.unosquare.carmigo.dto.response.DistanceResponse;
import com.unosquare.carmigo.dto.response.DriverResponse;
import com.unosquare.carmigo.dto.response.JourneyResponse;
import com.unosquare.carmigo.dto.response.PassengerResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.JourneyService;
import com.unosquare.carmigo.util.AuthenticationBeansTestCase;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

@WebMvcTest(controllers = JourneyController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
public class JourneyControllerTest {

  private static final String API_LEADING = "/v1/journeys";
  private static final String ERROR_MSG = "%s do not match";

  @Autowired private MockMvc mockMvc;
  @MockBean private JourneyService journeyServiceMock;
  @MockBean private AppUser appUserMock;
  @Fixture private AppUser.CurrentAppUser currentAppUserFixture;
  @Fixture private JourneyRequest journeyRequestFixture;
  @Fixture private JourneyResponse journeyResponseFixture;
  @Fixture private SearchJourneysRequest searchJourneysRequestFixture;
  @Fixture private List<JourneyResponse> journeyResponseListFixture;
  @Fixture private DistanceRequest distanceRequestFixture;
  @Fixture private DistanceResponse distanceResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);

    when(appUserMock.get()).thenReturn(currentAppUserFixture);
  }

  @SneakyThrows
  @Test
  void getJourneyByIdTest() {
    when(journeyServiceMock.getJourneyById(anyInt())).thenReturn(journeyResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/1"))
        .andExpect(status().isOk()).andReturn();

    final LinkedHashMap<String, Object> journey = getObjectResponse(response.getResponse().getContentAsString());
    callAssertions(journey, journeyResponseFixture);
    verify(journeyServiceMock).getJourneyById(anyInt());
  }

  @SneakyThrows
  @Test
  void searchJourneysTest() {
    when(journeyServiceMock.searchJourneys(any(SearchJourneysRequest.class))).thenReturn(journeyResponseListFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/search")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationIdFrom", "5")
            .param("locationIdTo", "1")
            .param("dateTimeFrom", "2021-11-30T09:00:00Z")
            .param("dateTimeTo", "2023-12-01T09:00:00Z"))
        .andExpect(status().isOk()).andReturn();

    final ArrayList<LinkedHashMap<String, Object>> journeys = getObjectResponse(
        response.getResponse().getContentAsString());
    callAssertions(journeys, journeyResponseListFixture);
    verify(journeyServiceMock).searchJourneys(any(SearchJourneysRequest.class));
  }

  @SneakyThrows
  @Test
  void searchJourneysMissingParamTest() {
    when(journeyServiceMock.searchJourneys(any(SearchJourneysRequest.class))).thenReturn(journeyResponseListFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/search")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationIdFrom", "5")
            .param("locationIdTo", "1")
            .param("dateTimeFrom", "2021-11-30T09:00:00Z"))
        .andExpect(status().isBadRequest()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("message"), "Argument not valid: dateTimeTo must not be null");
    verify(journeyServiceMock, times(0)).searchJourneys(any(SearchJourneysRequest.class));
  }

  @SneakyThrows
  @Test
  void getJourneysByCurrentDriverTest() {
    when(journeyServiceMock.getJourneysByDriverId(anyInt())).thenReturn(journeyResponseListFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/drivers/my-journeys"))
        .andExpect(status().isOk()).andReturn();

    final ArrayList<LinkedHashMap<String, Object>> journeys = getObjectResponse(
        response.getResponse().getContentAsString());
    callAssertions(journeys, journeyResponseListFixture);
    verify(journeyServiceMock).getJourneysByDriverId(anyInt());
  }

  @SneakyThrows
  @Test
  void getJourneysByDriverIdTest() {
    when(journeyServiceMock.getJourneysByDriverId(anyInt())).thenReturn(journeyResponseListFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/drivers/1"))
        .andExpect(status().isOk()).andReturn();

    final ArrayList<LinkedHashMap<String, Object>> journeys = getObjectResponse(
        response.getResponse().getContentAsString());
    callAssertions(journeys, journeyResponseListFixture);
    verify(journeyServiceMock).getJourneysByDriverId(anyInt());
  }

  @SneakyThrows
  @Test
  void getJourneysByCurrentPassengerTest() {
    when(journeyServiceMock.getJourneysByPassengersId(anyInt())).thenReturn(journeyResponseListFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/passengers/my-journeys"))
        .andExpect(status().isOk()).andReturn();

    final ArrayList<LinkedHashMap<String, Object>> journeys = getObjectResponse(
        response.getResponse().getContentAsString());
    callAssertions(journeys, journeyResponseListFixture);
    verify(journeyServiceMock).getJourneysByPassengersId(anyInt());
  }

  @SneakyThrows
  @Test
  void getJourneysByPassengerIdTest() {
    when(journeyServiceMock.getJourneysByPassengersId(anyInt())).thenReturn(journeyResponseListFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/passengers/1"))
        .andExpect(status().isOk()).andReturn();

    final ArrayList<LinkedHashMap<String, Object>> journeys = getObjectResponse(
        response.getResponse().getContentAsString());
    callAssertions(journeys, journeyResponseListFixture);
    verify(journeyServiceMock).getJourneysByPassengersId(anyInt());
  }

  @SneakyThrows
  @Test
  void createJourneyTest() {
    when(journeyServiceMock.createJourney(anyInt(), any(JourneyRequest.class))).thenReturn(journeyResponseFixture);
    final var response = mockMvc.perform(post(API_LEADING)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(journeyRequestFixture)))
        .andExpect(status().isCreated()).andReturn();

    final LinkedHashMap<String, Object> journey = getObjectResponse(response.getResponse().getContentAsString());
    assertJourney(journey, journeyResponseFixture);
    verify(journeyServiceMock).createJourney(anyInt(), any(JourneyRequest.class));
  }

  @SneakyThrows
  @Test
  void createJourneyByDriverIdTest() {
    when(journeyServiceMock.createJourney(anyInt(), any(JourneyRequest.class))).thenReturn(journeyResponseFixture);
    final var response = mockMvc.perform(post(API_LEADING + "/drivers/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(journeyRequestFixture)))
        .andExpect(status().isCreated()).andReturn();

    final LinkedHashMap<String, Object> journey = getObjectResponse(response.getResponse().getContentAsString());
    assertJourney(journey, journeyResponseFixture);
    verify(journeyServiceMock).createJourney(anyInt(), any(JourneyRequest.class));
  }

  @SneakyThrows
  @Test
  void addCurrentPassengerToJourneyTest() {
    doNothing().when(journeyServiceMock).addPassengerToJourney(anyInt(), anyInt());
    mockMvc.perform(post(API_LEADING + "/1/add-passenger"))
        .andExpect(status().isOk()).andReturn();

    verify(journeyServiceMock).addPassengerToJourney(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void addPassengerToThisJourneyTest() {
    doNothing().when(journeyServiceMock).addPassengerToJourney(anyInt(), anyInt());
    mockMvc.perform(post(API_LEADING + "/1/add-passenger/1"))
        .andExpect(status().isOk()).andReturn();

    verify(journeyServiceMock).addPassengerToJourney(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void addPassengerToJourneyAgainTest() {
    doThrow(EntityExistsException.class).when(journeyServiceMock).addPassengerToJourney(anyInt(), anyInt());
    mockMvc.perform(post(API_LEADING + "/1/add-passenger/1"))
        .andExpect(status().isConflict()).andReturn();

    verify(journeyServiceMock).addPassengerToJourney(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void patchValidJourneyTest() {
    when(journeyServiceMock.patchJourney(anyInt(), any(JsonPatch.class))).thenReturn(journeyResponseFixture);
    final var response = mockMvc.perform(patch(API_LEADING + "/1")
            .contentType("application/json-patch+json")
            .content(patchValidJourney()))
        .andExpect(status().isAccepted()).andReturn();

    final LinkedHashMap<String, Object> journey = getObjectResponse(response.getResponse().getContentAsString());
    assertJourney(journey, journeyResponseFixture);
    verify(journeyServiceMock).patchJourney(anyInt(), any(JsonPatch.class));
  }

  @SneakyThrows
  @Test
  void patchInvalidJourneyTest() {
    mockMvc.perform(patch(API_LEADING + "/1")
            .contentType("application/json-patch+json")
            .content(patchInvalidJourney()))
        .andExpect(status().isBadRequest()).andReturn();

    verify(journeyServiceMock, times(0)).patchJourney(anyInt(), any(JsonPatch.class));
  }

  @SneakyThrows
  @Test
  void deleteJourneyTest() {
    doNothing().when(journeyServiceMock).deleteJourneyById(anyInt());
    mockMvc.perform(delete(API_LEADING + "/1"))
        .andExpect(status().isNoContent()).andReturn();

    verify(journeyServiceMock).deleteJourneyById(anyInt());
  }

  @SneakyThrows
  @Test
  void deleteJourneyAgainTest() {
    doThrow(EntityNotFoundException.class).when(journeyServiceMock).deleteJourneyById(anyInt());
    mockMvc.perform(delete(API_LEADING + "/1"))
        .andExpect(status().isNotFound()).andReturn();

    verify(journeyServiceMock).deleteJourneyById(anyInt());
  }

  @SneakyThrows
  @Test
  void removeCurrentPassengerFromJourneyTest() {
    doNothing().when(journeyServiceMock).removePassengerFromJourney(anyInt(), anyInt());
    mockMvc.perform(delete(API_LEADING + "/1/remove-passenger"))
        .andExpect(status().isNoContent()).andReturn();

    verify(journeyServiceMock).removePassengerFromJourney(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void removePassengerFromThisJourneyTest() {
    doNothing().when(journeyServiceMock).removePassengerFromJourney(anyInt(), anyInt());
    mockMvc.perform(delete(API_LEADING + "/1/remove-passenger/1"))
        .andExpect(status().isNoContent()).andReturn();

    verify(journeyServiceMock).removePassengerFromJourney(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void removePassengerFromJourneyAgainTest() {
    doThrow(EntityNotFoundException.class).when(journeyServiceMock).removePassengerFromJourney(anyInt(), anyInt());
    mockMvc.perform(delete(API_LEADING + "/1/remove-passenger"))
        .andExpect(status().isNotFound()).andReturn();

    verify(journeyServiceMock).removePassengerFromJourney(anyInt(), anyInt());
  }

  @SneakyThrows
  @Test
  void calculateDistanceTest() {
    when(journeyServiceMock.calculateDistance(any(DistanceRequest.class))).thenReturn(distanceResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/calculateDistance")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationFrom", "BT62-3JR")
            .param("countryFrom", "GBR")
            .param("locationTo", "BT71-6SS")
            .param("countryTo", "GBR"))
        .andExpect(status().isOk()).andReturn();

    verify(journeyServiceMock).calculateDistance(any(DistanceRequest.class));
  }

  @SneakyThrows
  @Test
  void calculateDistanceMissingParamTest() {
    when(journeyServiceMock.calculateDistance(any(DistanceRequest.class))).thenReturn(distanceResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/calculateDistance")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("locationFrom", "BT62-3JR")
            .param("countryFrom", "GBR")
            .param("locationTo", "BT71-6SS"))
        .andExpect(status().isBadRequest()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("message"), "Argument not valid: countryTo must not be empty");
    verify(journeyServiceMock, times(0)).calculateDistance(any(DistanceRequest.class));
  }

  @SneakyThrows
  @SuppressWarnings("unchecked cast")
  private void callAssertions(final LinkedHashMap<String, Object> journey, final JourneyResponse journeyResponse) {
    assertJourney(journey, journeyResponse);
    assertDriver((LinkedHashMap<String, Object>) journey.get("driver"), journeyResponse.getDriver());
    assertPassengers((ArrayList<LinkedHashMap<String, Object>>) journey.get("passengers"),
        journeyResponse.getPassengers());
  }

  @SneakyThrows
  @SuppressWarnings("unchecked cast")
  private void callAssertions(
      final ArrayList<LinkedHashMap<String, Object>> journeys, final List<JourneyResponse> journeyResponseList) {
    for (int i = 0; i < journeys.size(); i++) {
      assertJourney(journeys.get(i), journeyResponseList.get(i));
      assertDriver((LinkedHashMap<String, Object>) journeys.get(i).get("driver"),
          journeyResponseList.get(i).getDriver());
      assertPassengers((ArrayList<LinkedHashMap<String, Object>>) journeys.get(i).get("passengers"),
          journeyResponseList.get(i).getPassengers());
    }
  }

  @SuppressWarnings("unchecked cast")
  private void assertJourney(final LinkedHashMap<String, Object> journey, final JourneyResponse journeyResponse) {
    assertEquals(journey.get("id"), journeyResponse.getId(), ERROR_MSG.formatted("Journey ids"));
    assertEquals(journey.get("createdDate"), journeyResponse.getCreatedDate().toString(),
        ERROR_MSG.formatted("Journey's created dates"));
    assertEquals(journey.get("maxPassengers"), journeyResponse.getMaxPassengers(),
        ERROR_MSG.formatted("Max passengers"));
    assertEquals(journey.get("dateTime"), journeyResponse.getDateTime().toString(), ERROR_MSG.formatted("Date times"));

    final LinkedHashMap<String, Object> locationFrom =
        (LinkedHashMap<String, Object>) journey.get("locationFrom");
    assertEquals(locationFrom.get("id"), journeyResponse.getLocationFrom().getId(),
        ERROR_MSG.formatted("Location from ids"));
    assertEquals(locationFrom.get("description"), journeyResponse.getLocationFrom().getDescription(),
        ERROR_MSG.formatted("Location from descriptions"));

    final LinkedHashMap<String, Object> locationTo =
        (LinkedHashMap<String, Object>) journey.get("locationTo");
    assertEquals(locationTo.get("id"), journeyResponse.getLocationTo().getId(),
        ERROR_MSG.formatted("Location to ids"));
    assertEquals(locationTo.get("description"), journeyResponse.getLocationTo().getDescription(),
        ERROR_MSG.formatted("Location to descriptions"));
  }

  @SuppressWarnings("unchecked cast")
  private void assertDriver(final LinkedHashMap<String, Object> driver, final DriverResponse driverResponse) {
    assertEquals(driver.get("id"), driverResponse.getId(), ERROR_MSG.formatted("Driver's ids"));
    assertEquals(driver.get("licenseNumber"), driverResponse.getLicenseNumber(),
        ERROR_MSG.formatted("Driver's license numbers"));

    final LinkedHashMap<String, Object> driversProfile = (LinkedHashMap<String, Object>) driver.get("platformUser");
    assertEquals(driversProfile.get("id"), driverResponse.getPlatformUser().getId(),
        ERROR_MSG.formatted("Driver's Platform User Ids"));
    assertEquals(driversProfile.get("createdDate"), driverResponse.getPlatformUser().getCreatedDate().toString(),
        ERROR_MSG.formatted("Driver's created dates"));
    assertEquals(driversProfile.get("firstName"), driverResponse.getPlatformUser().getFirstName(),
        ERROR_MSG.formatted("Driver's First Names"));
    assertEquals(driversProfile.get("lastName"), driverResponse.getPlatformUser().getLastName(),
        ERROR_MSG.formatted("Driver's Last names"));
    assertEquals(driversProfile.get("dob"), driverResponse.getPlatformUser().getDob().toString(),
        ERROR_MSG.formatted("Driver's Dobs"));
    assertEquals(driversProfile.get("email"), driverResponse.getPlatformUser().getEmail(),
        ERROR_MSG.formatted("Driver's emails"));
    assertEquals(driversProfile.get("phoneNumber"), driverResponse.getPlatformUser().getPhoneNumber(),
        ERROR_MSG.formatted("Driver's Phone Numbers"));

    final LinkedHashMap<String, Object> driversUserAccessStatus =
        (LinkedHashMap<String, Object>) driversProfile.get("userAccessStatus");
    assertEquals(driversUserAccessStatus.get("id"), driverResponse.getPlatformUser().getUserAccessStatus().getId(),
        ERROR_MSG.formatted("User access status ids"));
    assertEquals(driversUserAccessStatus.get("status"),
        driverResponse.getPlatformUser().getUserAccessStatus().getStatus(),
        ERROR_MSG.formatted("User access statuses"));
  }

  @SuppressWarnings("unchecked cast")
  private void assertPassengers(
      final ArrayList<LinkedHashMap<String, Object>> passengers, final List<PassengerResponse> passengerResponseList) {
    for (int i = 0; i < passengers.size(); i++) {
      assertEquals(passengers.get(i).get("id"), passengerResponseList.get(i).getId(),
          ERROR_MSG.formatted("Passenger's ids"));

      final LinkedHashMap<String, Object> passengersProfile =
          (LinkedHashMap<String, Object>) passengers.get(i).get("platformUser");
      assertEquals(passengersProfile.get("id"), passengerResponseList.get(i).getPlatformUser().getId(),
          ERROR_MSG.formatted("Passenger's profile's id"));
      assertEquals(passengersProfile.get("createdDate").toString(),
          passengerResponseList.get(i).getPlatformUser().getCreatedDate().toString(),
          ERROR_MSG.formatted("Passenger's profile's created dates"));
      assertEquals(passengersProfile.get("firstName"), passengerResponseList.get(i).getPlatformUser().getFirstName(),
          ERROR_MSG.formatted("Passenger's profile's first names"));
      assertEquals(passengersProfile.get("lastName"), passengerResponseList.get(i).getPlatformUser().getLastName(),
          ERROR_MSG.formatted("Passenger's profile's last names"));
      assertEquals(passengersProfile.get("dob").toString(),
          passengerResponseList.get(i).getPlatformUser().getDob().toString(),
          ERROR_MSG.formatted("Passenger's profile's dobs"));
      assertEquals(passengersProfile.get("email"), passengerResponseList.get(i).getPlatformUser().getEmail(),
          ERROR_MSG.formatted("Passenger's profile's emails"));
      assertEquals(passengersProfile.get("phoneNumber"),
          passengerResponseList.get(i).getPlatformUser().getPhoneNumber(),
          ERROR_MSG.formatted("Passenger's profile's phone numbers"));

      final LinkedHashMap<String, Object> passengersUserStatus =
          (LinkedHashMap<String, Object>) passengersProfile.get("userAccessStatus");
      assertEquals(passengersUserStatus.get("id"),
          passengerResponseList.get(i).getPlatformUser().getUserAccessStatus().getId(),
          ERROR_MSG.formatted("Passenger's user status ids"));
      assertEquals(passengersUserStatus.get("status"),
          passengerResponseList.get(i).getPlatformUser().getUserAccessStatus().getStatus(),
          ERROR_MSG.formatted("Passenger's user status"));
    }
  }

  private String patchValidJourney() {
    return """
        [
           {
             "op": "replace",
             "path": "/locationFrom/id",
             "value": "5"
           },
           {
             "op": "replace",
             "path": "/maxPassengers",
             "value": "5"
           }
         ]
        """;
  }

  private String patchInvalidJourney() {
    return """
        [
          {
            "op": "dummy",
            "path": "/maxPassengers",
            "value": "5"
          }
        ]
        """;
  }
}
