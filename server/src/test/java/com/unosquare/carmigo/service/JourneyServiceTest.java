package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.constant.AppConstants.ACTIVE;
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
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabDistanceDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.exception.UnauthorizedException;
import com.unosquare.carmigo.model.request.CreateCalculateDistanceCriteria;
import com.unosquare.carmigo.model.request.CreateSearchJourneysCriteria;
import com.unosquare.carmigo.openfeign.DistanceApi;
import com.unosquare.carmigo.openfeign.DistanceHolder;
import com.unosquare.carmigo.openfeign.Points;
import com.unosquare.carmigo.repository.JourneyRepository;
import com.unosquare.carmigo.repository.PassengerJourneyRepository;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.security.AppUser.CurrentAppUser;
import com.unosquare.carmigo.util.MapperUtils;
import com.unosquare.carmigo.util.PatchUtility;
import com.unosquare.carmigo.util.ResourceUtility;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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

  @Fixture private GrabJourneyDTO grabJourneyDTOFixture;
  @Fixture private Journey journeyFixture;
  @Fixture private CreateJourneyDTO createJourneyDTOFixture;
  @Fixture private List<Journey> journeyFixtureList;
  @Fixture private CreateSearchJourneysCriteria createSearchJourneysCriteriaFixture;
  @Fixture private DistanceHolder distanceHolderFixture;
  @Fixture private CreateCalculateDistanceCriteria createCalculateDistanceCriteriaFixture;
  @Fixture private List<Passenger> passengerFixtureList;
  @Fixture private Passenger passengerFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @Test
  public void get_Journey_By_Id_Returns_GrabJourneyDTO() {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
    final GrabJourneyDTO grabJourneyDTO = journeyService.getJourneyById(anyInt());

    assertThat(grabJourneyDTO.getId()).isEqualTo(grabJourneyDTOFixture.getId());
    assertThat(grabJourneyDTO.getCreatedDate()).isEqualTo(grabJourneyDTOFixture.getCreatedDate());
    assertThat(grabJourneyDTO.getLocationFrom()).isEqualTo(grabJourneyDTOFixture.getLocationFrom());
    assertThat(grabJourneyDTO.getLocationTo()).isEqualTo(grabJourneyDTOFixture.getLocationTo());
    assertThat(grabJourneyDTO.getMaxPassengers()).isEqualTo(grabJourneyDTOFixture.getMaxPassengers());
    assertThat(grabJourneyDTO.getDateTime()).isEqualTo(grabJourneyDTOFixture.getDateTime());
    assertThat(grabJourneyDTO.getDriver()).isEqualTo(grabJourneyDTOFixture.getDriver());
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
  public void search_Journeys_Returns_List_of_GrabJourneyDTO() {
    when(journeyRepositoryMock.findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
        anyInt(), anyInt(), any(Instant.class), any(Instant.class))).thenReturn(journeyFixtureList);
    final List<GrabJourneyDTO> grabJourneyDTOList = MapperUtils.mapList(journeyFixtureList, GrabJourneyDTO.class,
        modelMapperMock);
    final List<GrabJourneyDTO> journeyList = journeyService.searchJourneys(createSearchJourneysCriteriaFixture);

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
        () -> journeyService.searchJourneys(createSearchJourneysCriteriaFixture),
        "ResourceNotFoundException is expected.");
    verify(journeyRepositoryMock).findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
        anyInt(), anyInt(), any(Instant.class), any(Instant.class));
  }

  @Test
  public void get_Journeys_By_Driver_Id_Returns_List_Of_GrabJourneyDTO() {
    when(journeyRepositoryMock.findJourneysByDriverId(anyInt())).thenReturn(journeyFixtureList);
    final List<GrabJourneyDTO> grabJourneyDTOList = MapperUtils.mapList(
        journeyFixtureList, GrabJourneyDTO.class, modelMapperMock);
    final List<GrabJourneyDTO> journeyDriverList = journeyService.getJourneysByDriverId(anyInt());

    assertThat(journeyDriverList.size()).isEqualTo(grabJourneyDTOList.size());
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
  public void get_Journeys_By_Passengers_Id_Returns_List_Of_GrabJourneyDTO() {
    when(journeyRepositoryMock.findJourneysByPassengersId(anyInt())).thenReturn(journeyFixtureList);
    final List<GrabJourneyDTO> grabJourneyDTOList = MapperUtils.mapList(
        journeyFixtureList, GrabJourneyDTO.class, modelMapperMock);
    final List<GrabJourneyDTO> journeyList = journeyService.getJourneysByPassengersId(anyInt());

    assertThat(journeyList.size()).isEqualTo(grabJourneyDTOList.size());
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
  public void create_Journey_Returns_GrabJourneyDTO() {
    final Journey spyJourney = spy(new Journey());
    when(modelMapperMock.map(createJourneyDTOFixture, Journey.class)).thenReturn(spyJourney);
    when(journeyRepositoryMock.save(spyJourney)).thenReturn(journeyFixture);
    when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
    spyJourney.setCreatedDate(any(Instant.class));
    spyJourney.setLocationFrom(any(Location.class));
    spyJourney.setLocationTo(any(Location.class));
    spyJourney.setDriver(any(Driver.class));
    final GrabJourneyDTO grabJourneyDTO = journeyService.createJourney(1, createJourneyDTOFixture);

    assertThat(grabJourneyDTO.getCreatedDate()).isEqualTo(grabJourneyDTOFixture.getCreatedDate());
    assertThat(grabJourneyDTO.getLocationFrom()).isEqualTo(grabJourneyDTOFixture.getLocationFrom());
    assertThat(grabJourneyDTO.getLocationTo()).isEqualTo(grabJourneyDTOFixture.getLocationTo());
    assertThat(grabJourneyDTO.getDriver()).isEqualTo(grabJourneyDTOFixture.getDriver());
    verify(journeyRepositoryMock).save(any(Journey.class));
  }

  @Test
  public void add_Passenger_To_Journey_Returns_GrabJourneyDTO() {
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
  public void patch_Journey_Returns_GrabJourneyDTO() throws Exception {
    when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
    when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
    final JsonPatch patch = PatchUtility.jsonPatch(PATCH_JOURNEY_VALID_JSON);
    final JsonNode journeyNode = PatchUtility.jsonNode(journeyFixture, patch);
    when(objectMapperMock.convertValue(grabJourneyDTOFixture, JsonNode.class)).thenReturn(journeyNode);
    when(objectMapperMock.treeToValue(journeyNode, Journey.class)).thenReturn(journeyFixture);
    when(journeyRepositoryMock.save(journeyFixture)).thenReturn(journeyFixture);
    when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
    setCurrentAppUserId(grabJourneyDTOFixture.getDriver().getId());
    final GrabJourneyDTO grabJourneyDTO = journeyService.patchJourney(journeyFixture.getId(), patch);

    assertThat(grabJourneyDTO.getMaxPassengers()).isEqualTo(grabJourneyDTOFixture.getMaxPassengers());
    assertThat(grabJourneyDTO.getLocationFrom().getId()).isEqualTo(grabJourneyDTOFixture.getLocationFrom().getId());
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
    DistanceHolder distanceHolder = new DistanceHolder();
    distanceHolder.setPoints(List.of(new Points(), new Points()));
    when(distanceApiMock.getDistance(anyString())).thenReturn(distanceHolderFixture);
    final GrabDistanceDTO grabDistanceDTO = journeyService
        .calculateDistance(createCalculateDistanceCriteriaFixture);

    assertThat(grabDistanceDTO.getLocationFrom().getLocation())
        .isEqualTo(distanceHolderFixture.getPoints().get(0).getProperties().getGeocode().getName());
    assertThat(grabDistanceDTO.getLocationFrom().getCoordinates().getLatitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(0).getProperties().getGeocode().getLatitude());
    assertThat(grabDistanceDTO.getLocationFrom().getCoordinates().getLongitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(0).getProperties().getGeocode().getLongitude());
    assertThat(grabDistanceDTO.getLocationTo().getLocation())
        .isEqualTo(distanceHolderFixture.getPoints().get(1).getProperties().getGeocode().getName());
    assertThat(grabDistanceDTO.getLocationTo().getCoordinates().getLatitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(1).getProperties().getGeocode().getLatitude());
    assertThat(grabDistanceDTO.getLocationTo().getCoordinates().getLongitude())
        .isEqualTo(distanceHolderFixture.getPoints().get(1).getProperties().getGeocode().getLongitude());
    verify(distanceApiMock).getDistance(anyString());
  }

  private void setCurrentAppUserId(final int userId) {
    final CurrentAppUser currentAppUser = CurrentAppUser.builder().id(userId).userAccessStatus(ACTIVE).build();
    when(appUserMock.get()).thenReturn(currentAppUser);
  }
}
