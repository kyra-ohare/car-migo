package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.constant.AppConstants.JOURNEY_CACHE;
import static com.unosquare.carmigo.constant.AppConstants.NOT_PERMITTED;
import static com.unosquare.carmigo.constant.AppConstants.NO_AVAILABILITY;
import static com.unosquare.carmigo.security.UserStatus.ADMIN;
import static com.unosquare.carmigo.service.DriverService.DRIVER_NOT_FOUND;
import static com.unosquare.carmigo.service.PassengerService.PASSENGER_NOT_FOUND;
import static com.unosquare.carmigo.util.CommonBehaviours.findEntityById;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.request.DistanceRequest;
import com.unosquare.carmigo.dto.request.JourneyRequest;
import com.unosquare.carmigo.dto.request.SearchJourneysRequest;
import com.unosquare.carmigo.dto.response.DistanceResponse;
import com.unosquare.carmigo.dto.response.JourneyResponse;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.exception.MaxPassengerLimitException;
import com.unosquare.carmigo.exception.PatchException;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.exception.UnauthorizedException;
import com.unosquare.carmigo.openfeign.DistanceApi;
import com.unosquare.carmigo.openfeign.DistanceHolder;
import com.unosquare.carmigo.openfeign.Geocode;
import com.unosquare.carmigo.repository.JourneyRepository;
import com.unosquare.carmigo.repository.PassengerJourneyRepository;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.util.MapperUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles requests regarding the {@link Journey} entity.
 */
@Service
@RequiredArgsConstructor
public class JourneyService {

  private static final String JOURNEY_NOT_FOUND = "Journey not found";

  private final JourneyRepository journeyRepository;
  private final PassengerRepository passengerRepository;
  private final PassengerJourneyRepository passengerJourneyRepository;
  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;
  private final EntityManager entityManager;
  private final DistanceApi distanceApi;
  private final AppUser appUser;

  /**
   * Fetches a journey.
   *
   * @param journeyId the journey id to search for.
   * @return a {@link JourneyResponse}.
   */
  @Cacheable(value = JOURNEY_CACHE, key = "#journeyId")
  public JourneyResponse getJourneyById(final int journeyId) {
    final Journey journey = findEntityById(journeyId, journeyRepository, JOURNEY_NOT_FOUND);
    final JourneyResponse journeyResponse = modelMapper.map(journey, JourneyResponse.class);
    checkJourneyAvailability(List.of(journeyResponse));
    return journeyResponse;
  }

  /**
   * Searches for journeys.
   *
   * @param searchJourneysRequest the search criteria as {@link SearchJourneysRequest}.
   * @return a List of {@link JourneyResponse}.
   */
  public List<JourneyResponse> searchJourneys(final SearchJourneysRequest searchJourneysRequest) {
    final List<Journey> result = journeyRepository.findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
        searchJourneysRequest.getLocationIdFrom(), searchJourneysRequest.getLocationIdTo(),
        searchJourneysRequest.getDateTimeFrom(), searchJourneysRequest.getDateTimeTo());
    if (result.isEmpty()) {
      throw new ResourceNotFoundException("No journeys found for this route. " + searchJourneysRequest);
    }
    final var journeys = MapperUtils.mapList(result, JourneyResponse.class, modelMapper);
    checkJourneyAvailability(journeys);
    hidePassengerAndMaybeDriverFields(journeys, true);
    return journeys;
  }

  /**
   * Fetches journeys of a driver.
   *
   * @param driverId the driver id to fetch their journeys.
   * @return a List of {@link JourneyResponse}.
   */
  public List<JourneyResponse> getJourneysByDriverId(final int driverId) {
    final List<Journey> result = journeyRepository.findJourneysByDriverId(driverId);
    if (result.isEmpty()) {
      throw new ResourceNotFoundException("No journeys found for this driver.");
    }
    final var journeys = MapperUtils.mapList(result, JourneyResponse.class, modelMapper);
    checkJourneyAvailability(journeys);
    return journeys;
  }

  /**
   * Fetches journeys of a passenger.
   *
   * @param passengerId the passenger id to fetch their journeys.
   * @return a List of {@link JourneyResponse}.
   */
  public List<JourneyResponse> getJourneysByPassengersId(final int passengerId) {
    final List<Journey> result = journeyRepository.findJourneysByPassengersId(passengerId);
    if (result.isEmpty()) {
      throw new ResourceNotFoundException("No journeys found for this passenger.");
    }
    final var journeys = MapperUtils.mapList(result, JourneyResponse.class, modelMapper);
    hidePassengerAndMaybeDriverFields(journeys, false);
    return journeys;
  }

  /**
   * Only drivers can create journeys.
   *
   * @param driverId       the driver id to create a journey for.
   * @param journeyRequest the requirements as {@link JourneyRequest}.
   * @return a {@link JourneyResponse}.
   */
  @CachePut(value = JOURNEY_CACHE, key = "#result.id")
  public JourneyResponse createJourney(final int driverId, final JourneyRequest journeyRequest) {
    final Journey journey = modelMapper.map(journeyRequest, Journey.class);
    journey.setLocationFrom(entityManager.getReference(Location.class, journeyRequest.getLocationIdFrom()));
    journey.setLocationTo(entityManager.getReference(Location.class, journeyRequest.getLocationIdTo()));
    journey.setDriver(entityManager.getReference(Driver.class, driverId));
    journey.setMaxPassengers(journeyRequest.getMaxPassengers());
    journey.setCreatedDate(Instant.now());
    final Journey savedJourney;
    try {
      savedJourney = journeyRepository.save(journey);
    } catch (final DataIntegrityViolationException ex) {
      throw new EntityNotFoundException(DRIVER_NOT_FOUND);
    }
    savedJourney.setDriver(null);
    return modelMapper.map(savedJourney, JourneyResponse.class);
  }

  /**
   * Enables the user to be a passenger of a journey.
   *
   * @param journeyId   the journey id to add this passenger.
   * @param passengerId the passenger id.
   */
  @CacheEvict(value = JOURNEY_CACHE, key = "#journeyId")
  public void addPassengerToJourney(final int journeyId, final int passengerId) {
    /* Check if passenger exists first. If not, no need to find journeys and carry on*/
    final Passenger passenger = findEntityById(passengerId, passengerRepository, PASSENGER_NOT_FOUND);
    final Journey journey = findEntityById(journeyId, journeyRepository, JOURNEY_NOT_FOUND);
    if (journey.getPassengers().size() >= journey.getMaxPassengers()) {
      throw new MaxPassengerLimitException(NO_AVAILABILITY);
    }
    if (journey.getDriver().getId() == passengerId) {
      throw new IllegalStateException("Driver cannot be passenger.");
    }
    final List<Passenger> passengers = journey.getPassengers();
    passengers.forEach(
        p -> {
          if (p.getId() == passengerId) {
            throw new EntityExistsException("Passenger is in this journey already.");
          }
        });
    passengers.add(passenger);
    journey.setPassengers(passengers);
    journeyRepository.save(journey);
  }

  /**
   * Corrects journey information.<br>
   * Pass an array of a {@link JsonPatch} body with operation, the path and the value.<br>
   * Accepted operation values are “add”, "remove", "replace", "move", "copy" and "test".<br>
   * Here is an example which updates a journey to take up to 3 passengers, and it is going to destination id 5:<br>
   * <pre>
   *   [
   *     {
   *       "op": "replace",
   *       "path": "/maxPassengers",
   *       "value": "3"
   *     },
   *     {
   *       "op": "replace",
   *       "path": "/locationTo/id",
   *       "value": "5"
   *     }
   *   ]
   * </pre>
   *
   * @param journeyId the journey id to be updated.
   * @param patch     a {@link JsonPatch}.
   * @return a {@link JourneyResponse}.
   */
  @CachePut(value = JOURNEY_CACHE, key = "#journeyId")
  public JourneyResponse patchJourney(final int journeyId, final JsonPatch patch) {
    final var journey = findEntityById(journeyId, journeyRepository, JOURNEY_NOT_FOUND);
    final var response = modelMapper.map(journey, JourneyResponse.class);
    verifyUserAuthorization(response.getDriver().getId());
    try {
      final JsonNode journeyNode = patch.apply(objectMapper.convertValue(response, JsonNode.class));
      final Journey patchedJourney = objectMapper.treeToValue(journeyNode, Journey.class);
      return modelMapper.map(journeyRepository.save(patchedJourney), JourneyResponse.class);
    } catch (final JsonPatchException | JsonProcessingException ex) {
      throw new PatchException("Error updating journey id %d - %s".formatted(journeyId, ex.getMessage()));
    }
  }

  /**
   * Deletes a journey.
   *
   * @param journeyId the journey id to be deleted.
   */
  @CacheEvict(value = JOURNEY_CACHE, key = "#journeyId")
  public void deleteJourneyById(final int journeyId) {
    final Journey journey = findEntityById(journeyId, journeyRepository, JOURNEY_NOT_FOUND);
    verifyUserAuthorization(journey.getDriver().getId());
    journeyRepository.deleteById(journeyId);
  }

  /**
   * Enables the user to no longer be a passenger of a journey.
   *
   * @param journeyId   the journey id to remove this passenger.
   * @param passengerId the passenger id.
   */
  @Transactional
  @CacheEvict(value = JOURNEY_CACHE, key = "#journeyId")
  public void removePassengerFromJourney(final int journeyId, final int passengerId) {
    final Journey journey = findEntityById(journeyId, journeyRepository, JOURNEY_NOT_FOUND);
    final List<Passenger> passengers = journey.getPassengers();
    for (Passenger p : passengers) {
      if (p.getId() == passengerId) {
        passengerJourneyRepository.deleteByJourneyIdAndPassengerId(journeyId, passengerId);
        journeyRepository.save(journey);
        return;
      }
    }
    throw new EntityNotFoundException("Passenger is not in this journey.");
  }

  /**
   * Searches for the distance between two locations.
   *
   * @param distanceRequest the search criteria as {@link DistanceRequest}.
   * @return a {@link DistanceResponse}.
   */
  public DistanceResponse calculateDistance(final DistanceRequest distanceRequest) {
    final String request = prepareRequestToDistanceApi(distanceRequest);
    final DistanceHolder distanceHolder = distanceApi.getDistance(request);
    return convertDistanceHolderDistanceResponse(distanceHolder);
  }

  private void checkJourneyAvailability(List<JourneyResponse> journeys) {
    for (JourneyResponse j : journeys) {
      int availability = j.getMaxPassengers() - j.getPassengers().size();
      j.setAvailability(availability);
    }
  }

  private void verifyUserAuthorization(final int userId) {
    if (!(appUser.get().getId() == userId || appUser.get().getUserAccessStatus().equals(ADMIN))) {
      throw new UnauthorizedException(NOT_PERMITTED);
    }
  }

  private void hidePassengerAndMaybeDriverFields(final List<JourneyResponse> journeys, final boolean hideDriver) {
    for (final JourneyResponse j : journeys) {
      j.setPassengers(null);
      if (hideDriver) {
        j.setDriver(null);
      }
    }
  }

  private String prepareRequestToDistanceApi(final DistanceRequest criteria) {
    return "[{\"t\":\"" + criteria.getLocationFrom() + "," + criteria.getCountryFrom() + "\"},{\"t\":\""
        + criteria.getLocationTo() + "," + criteria.getCountryTo() + "\"}]";
  }

  private DistanceResponse convertDistanceHolderDistanceResponse(final DistanceHolder distanceHolder) {
    if (distanceHolder.getPoints().size() > 1) {
      final DistanceResponse response = new DistanceResponse();
      response.setLocationFrom(convertToDistanceResponseLocation(
          distanceHolder.getPoints().get(0).getProperties().getGeocode()));
      response.setLocationTo(convertToDistanceResponseLocation(
          distanceHolder.getPoints().get(1).getProperties().getGeocode()));
      response.setDistance(convertToDistanceResponseDistance(
          distanceHolder.getSteps().get(0).getDistance().getGreatCircle()));
      return response;
    }
    throw new NoResultException("DistanceHolder is empty.");
  }

  private DistanceResponse.Location convertToDistanceResponseLocation(final Geocode geocode) {
    final DistanceResponse.Coordinate coordinates = new DistanceResponse.Coordinate();
    coordinates.setLatitude(geocode.getLatitude());
    coordinates.setLongitude(geocode.getLongitude());
    final DistanceResponse.Location location = new DistanceResponse.Location();
    location.setLocation(geocode.getName());
    location.setCoordinates(coordinates);
    return location;
  }

  private DistanceResponse.Distance convertToDistanceResponseDistance(final double km) {
    final DistanceResponse.Distance distance = new DistanceResponse.Distance();
    distance.setKm(Math.round(km * 10d) / 10d);
    distance.setMi(Math.round(convertKmToMi(km) * 10d) / 10d);
    return distance;
  }

  private double convertKmToMi(final double km) {
    final double conversionFactor = 1.609344;
    return km / conversionFactor;
  }
}
