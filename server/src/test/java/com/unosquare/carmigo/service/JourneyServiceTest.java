package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.security.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.exception.UnauthorizedException;
import com.unosquare.carmigo.dto.request.DistanceRequest;
import com.unosquare.carmigo.dto.request.JourneyRequest;
import com.unosquare.carmigo.dto.request.SearchJourneysRequest;
import com.unosquare.carmigo.dto.response.JourneyResponse;
import com.unosquare.carmigo.openfeign.DistanceApi;
import com.unosquare.carmigo.openfeign.DistanceHolder;
import com.unosquare.carmigo.repository.JourneyRepository;
import com.unosquare.carmigo.repository.PassengerJourneyRepository;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.security.AppUser.CurrentAppUser;
import com.unosquare.carmigo.util.MapperUtils;
import com.unosquare.carmigo.util.PatchUtility;
import com.unosquare.carmigo.util.ResourceUtility;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class JourneyServiceTest {

  private static final String PATCH_JOURNEY_VALID_JSON =
      ResourceUtility.generateStringFromResource("jsonAssets/PatchJourneyValid.json");

  @Mock private JourneyRepository journeyRepositoryMock;
  @Mock private PassengerJourneyRepository passengerJourneyRepositoryMock;
  @Mock private ModelMapper modelMapperMock;
  @Mock private ObjectMapper objectMapperMock;
  @Mock private EntityManager entityManagerMock;
  @Mock private DistanceApi distanceApiMock;
  @Mock private AppUser appUserMock;
  @Mock private PassengerService passengerServiceMock;
  @InjectMocks private JourneyService journeyService;

  @Fixture private Journey journeyFixture;
  @Fixture private JourneyResponse journeyResponseFixture;
  @Fixture private JourneyRequest JourneyRequestFixture;
  @Fixture private List<Journey> journeyFixtureList;
  @Fixture private SearchJourneysRequest searchJourneysRequestFixture;
  @Fixture private DistanceHolder distanceHolderFixture;
  @Fixture private DistanceRequest distanceRequestFixture;
  @Fixture private List<Passenger> passengerFixtureList;
  @Fixture private Passenger passengerFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @Test
  public void get_Journey_By_Id_Returns_JourneyResponse() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    when(modelMapperMock.map(journeyFixture, JourneyResponse.class)).thenReturn(journeyResponseFixture);
    final var response = journeyService.getJourneyById(anyInt());

    assertThat(response.getId()).isEqualTo(journeyResponseFixture.getId());
    assertThat(response.getCreatedDate()).isEqualTo(journeyResponseFixture.getCreatedDate());
    assertThat(response.getLocationFrom()).isEqualTo(journeyResponseFixture.getLocationFrom());
    assertThat(response.getLocationTo()).isEqualTo(journeyResponseFixture.getLocationTo());
    assertThat(response.getMaxPassengers()).isEqualTo(journeyResponseFixture.getMaxPassengers());
    assertThat(response.getDateTime()).isEqualTo(journeyResponseFixture.getDateTime());
    assertThat(response.getDriver()).isEqualTo(journeyResponseFixture.getDriver());
    verify(journeyRepositoryMock).findById(anyInt());
  }

  @Test
  public void get_Journey_By_Id_Throws_ResourceNotFoundException() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class,
        () -> journeyService.getJourneyById(anyInt()),
        "ResourceNotFoundException is expected.");
    verify(journeyRepositoryMock).findById(anyInt());
  }

  @Test
  public void search_Journeys_Returns_List_of_JourneyResponse() {
    when(journeyRepositoryMock.findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
        anyInt(), anyInt(), any(Instant.class), any(Instant.class))).thenReturn(journeyFixtureList);
    final List<JourneyResponse> grabJourneyDTOList = MapperUtils.mapList(journeyFixtureList, JourneyResponse.class,
        modelMapperMock);
    final List<JourneyResponse> journeyList = journeyService.searchJourneys(searchJourneysRequestFixture);

    assertThat(journeyList.size()).isEqualTo(grabJourneyDTOList.size());
    verify(journeyRepositoryMock).findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
        anyInt(), anyInt(), any(Instant.class), any(Instant.class));
  }

  @Test
  public void search_Journeys_Throws_ResourceNotFoundException() {
    when(journeyRepositoryMock.findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
        anyInt(), anyInt(), any(Instant.class), any(Instant.class))).thenReturn(journeyFixtureList);
    journeyFixtureList.clear();
    assertThrows(ResourceNotFoundException.class,
        () -> journeyService.searchJourneys(searchJourneysRequestFixture),
        "ResourceNotFoundException is expected.");
    verify(journeyRepositoryMock).findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
        anyInt(), anyInt(), any(Instant.class), any(Instant.class));
  }

  @Test
  public void get_Journeys_By_Driver_Id_Returns_List_Of_JourneyResponse() {
    when(journeyRepositoryMock.findJourneysByDriverId(anyInt())).thenReturn(journeyFixtureList);
    final var response = journeyService.getJourneysByDriverId(anyInt());

    assertThat(response.size()).isEqualTo(journeyFixtureList.size());
    verify(journeyRepositoryMock).findJourneysByDriverId(anyInt());
  }

  @Test
  public void get_Journeys_By_Driver_Id_Throws_ResourceNotFoundException() {
    when(journeyRepositoryMock.findJourneysByDriverId(anyInt())).thenReturn(journeyFixtureList);
    journeyFixtureList.clear();
    assertThrows(ResourceNotFoundException.class,
        () -> journeyService.getJourneysByDriverId(anyInt()),
        "ResourceNotFoundException is expected.");
    verify(journeyRepositoryMock).findJourneysByDriverId(anyInt());
  }

  @Test
  public void get_Journeys_By_Passengers_Id_Returns_List_Of_JourneyResponse() {
    when(journeyRepositoryMock.findJourneysByPassengersId(anyInt())).thenReturn(journeyFixtureList);
    final var response = journeyService.getJourneysByPassengersId(anyInt());

    assertThat(response.size()).isEqualTo(journeyFixtureList.size());
    verify(journeyRepositoryMock).findJourneysByPassengersId(anyInt());
  }

  @Test
  public void get_Journeys_By_Passengers_Id_Throws_ResourceNotFoundException() {
    when(journeyRepositoryMock.findJourneysByPassengersId(anyInt())).thenReturn(journeyFixtureList);
    journeyFixtureList.clear();
    assertThrows(ResourceNotFoundException.class,
        () -> journeyService.getJourneysByPassengersId(anyInt()),
        "ResourceNotFoundException is expected.");
    verify(journeyRepositoryMock).findJourneysByPassengersId(anyInt());
  }

  @Test
  public void create_Journey_Returns_JourneyResponse() {
    final Journey spyJourney = spy(new Journey());
    when(modelMapperMock.map(JourneyRequestFixture, Journey.class)).thenReturn(spyJourney);
    when(journeyRepositoryMock.save(spyJourney)).thenReturn(journeyFixture);
    when(modelMapperMock.map(journeyFixture, JourneyResponse.class)).thenReturn(journeyResponseFixture);
    spyJourney.setCreatedDate(any(Instant.class));
    spyJourney.setLocationFrom(any(Location.class));
    spyJourney.setLocationTo(any(Location.class));
    spyJourney.setDriver(any(Driver.class));
    final var response = journeyService.createJourney(1, JourneyRequestFixture);

    assertThat(response.getCreatedDate()).isEqualTo(journeyResponseFixture.getCreatedDate());
    assertThat(response.getLocationFrom()).isEqualTo(journeyResponseFixture.getLocationFrom());
    assertThat(response.getLocationTo()).isEqualTo(journeyResponseFixture.getLocationTo());
    assertThat(response.getDriver()).isEqualTo(journeyResponseFixture.getDriver());
    verify(journeyRepositoryMock).save(any(Journey.class));
  }

  @Test
  public void add_Passenger_To_Journey_Returns_JourneyResponse() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    when(passengerServiceMock.findPassengerById(anyInt())).thenReturn(passengerFixture);
    passengerFixtureList.add(passengerFixture);
    journeyFixture.setPassengers(passengerFixtureList);
    when(journeyRepositoryMock.save(journeyFixture)).thenReturn(journeyFixture);
    journeyService.addPassengerToJourney(1, 2);
    verify(journeyRepositoryMock).findById(anyInt());
    verify(passengerServiceMock).findPassengerById(anyInt());
    verify(journeyRepositoryMock).save(any(Journey.class));
  }

  @Test
  public void add_Passenger_To_Journey_Throws_EntityExistsException() {
    final int passengerId = 1;
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    journeyFixture.getPassengers().get(0).setId(passengerId);
    assertThrows(EntityExistsException.class,
        () -> journeyService.addPassengerToJourney(journeyFixture.getId(), passengerId),
        "EntityExistsException is expected.");
    verify(journeyRepositoryMock).findById(anyInt());
    verify(passengerServiceMock, times(0)).findPassengerById(anyInt());
    verify(journeyRepositoryMock, times(0)).save(any(Journey.class));
  }

  @Test
  public void add_Passenger_To_Journey_Throws_IllegalStateException() {
    final int driverId = 1;
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    journeyFixture.getDriver().setId(driverId);
    assertThrows(IllegalStateException.class,
        () -> journeyService.addPassengerToJourney(journeyFixture.getId(), 1),
        "IllegalStateException is expected.");
    verify(journeyRepositoryMock).findById(anyInt());
    verify(passengerServiceMock, times(0)).findPassengerById(anyInt());
    verify(journeyRepositoryMock, times(0)).save(any(Journey.class));
  }

  @Test
  public void patch_Journey_Returns_JourneyResponse() throws Exception {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    when(modelMapperMock.map(journeyFixture, JourneyResponse.class)).thenReturn(journeyResponseFixture);
    final JsonPatch patch = PatchUtility.jsonPatch(PATCH_JOURNEY_VALID_JSON);
    final JsonNode journeyNode = PatchUtility.jsonNode(journeyFixture, patch);
    when(objectMapperMock.convertValue(journeyResponseFixture, JsonNode.class)).thenReturn(journeyNode);
    when(objectMapperMock.treeToValue(journeyNode, Journey.class)).thenReturn(journeyFixture);
    when(journeyRepositoryMock.save(journeyFixture)).thenReturn(journeyFixture);
    when(modelMapperMock.map(journeyFixture, JourneyResponse.class)).thenReturn(journeyResponseFixture);
    setCurrentAppUserId(journeyResponseFixture.getDriver().getId());
    final var response = journeyService.patchJourney(journeyFixture.getId(), patch);

    assertThat(response.getMaxPassengers()).isEqualTo(journeyResponseFixture.getMaxPassengers());
    assertThat(response.getLocationFrom().getId()).isEqualTo(journeyResponseFixture.getLocationFrom().getId());
    verify(journeyRepositoryMock).findById(anyInt());
    verify(journeyRepositoryMock).save(any(Journey.class));
  }

  @Test
  public void delete_Journey_By_Id_Returns_Void() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    setCurrentAppUserId(journeyFixture.getDriver().getId());
    journeyService.deleteJourneyById(anyInt());
    verify(journeyRepositoryMock).findById(anyInt());
    verify(journeyRepositoryMock).deleteById(anyInt());
  }

  @Test
  public void delete_Journey_By_Id_Throws_UnauthorizedException() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    setCurrentAppUserId(0);
    assertThrows(UnauthorizedException.class,
        () -> journeyService.deleteJourneyById(anyInt()),
        "UnauthorizedException is expected.");
    verify(journeyRepositoryMock).findById(anyInt());
    verify(journeyRepositoryMock, times(0)).deleteById(anyInt());
  }

  @Test
  public void delete_Journey_By_Id_Throws_ResourceNotFoundException() {
    doThrow(ResourceNotFoundException.class).when(journeyRepositoryMock).findById(anyInt());
    assertThrows(ResourceNotFoundException.class,
        () -> journeyService.deleteJourneyById(anyInt()),
        "ResourceNotFoundException is expected.");
    verify(journeyRepositoryMock).findById(anyInt());
    verify(journeyRepositoryMock, times(0)).deleteById(anyInt());
  }

  @Test
  public void remove_Passenger_From_Journey_Returns_Void() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    journeyService.removePassengerFromJourney(journeyFixture.getId(), journeyFixture.getPassengers().get(0).getId());
    verify(journeyRepositoryMock).findById(anyInt());
    verify(passengerJourneyRepositoryMock).deleteByJourneyIdAndPassengerId(anyInt(), anyInt());
  }

  @Test
  public void remove_Passenger_From_Journey_Throws_EntityNotFoundException() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    assertThrows(EntityNotFoundException.class,
        () -> journeyService.removePassengerFromJourney(1, 1),
        "EntityNotFoundException is expected.");
    verify(journeyRepositoryMock).findById(anyInt());
    verify(passengerJourneyRepositoryMock, times(0)).deleteByJourneyIdAndPassengerId(anyInt(), anyInt());
  }

  @Test
  public void calculate_Distance_Returns_GrabDistanceDTO() {
    when(distanceApiMock.getDistance(anyString())).thenReturn(distanceHolderFixture);
    final var response = journeyService.calculateDistance(distanceRequestFixture);

    assertThat(response.getLocationFrom().getLocation())
        .isEqualTo(distanceHolderFixture.getPoints().get(0).getProperties().getGeocode().getName());
    assertThat(response.getLocationFrom().getCoordinates().getLatitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(0).getProperties().getGeocode().getLatitude());
    assertThat(response.getLocationFrom().getCoordinates().getLongitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(0).getProperties().getGeocode().getLongitude());
    assertThat(response.getLocationTo().getLocation())
        .isEqualTo(distanceHolderFixture.getPoints().get(1).getProperties().getGeocode().getName());
    assertThat(response.getLocationTo().getCoordinates().getLatitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(1).getProperties().getGeocode().getLatitude());
    assertThat(response.getLocationTo().getCoordinates().getLongitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(1).getProperties().getGeocode().getLongitude());
    verify(distanceApiMock).getDistance(anyString());
  }

  private void setCurrentAppUserId(final int userId) {
    final CurrentAppUser currentAppUser = CurrentAppUser.builder().id(userId).userAccessStatus(ACTIVE).build();
    when(appUserMock.get()).thenReturn(currentAppUser);
  }
}
