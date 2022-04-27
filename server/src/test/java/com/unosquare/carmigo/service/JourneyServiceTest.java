package com.unosquare.carmigo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.repository.JourneyRepository;
import com.unosquare.carmigo.repository.PassengerJourneyRepository;
import com.unosquare.carmigo.util.MapperUtils;
import com.unosquare.carmigo.util.PatchUtility;
import com.unosquare.carmigo.util.ResourceUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JourneyServiceTest
{
    private static final String PATCH_JOURNEY_VALID_JSON =
            ResourceUtility.generateStringFromResource("requestJson/PatchJourneyValid.json");
    @Mock private JourneyRepository journeyRepositoryMock;
    @Mock private PassengerJourneyRepository passengerJourneyRepositoryMock;
    @Mock private ModelMapper modelMapperMock;
    @Mock private ObjectMapper objectMapperMock;
    @Mock private EntityManager entityManagerMock;
    @InjectMocks private JourneyService journeyService;

    @Fixture private GrabJourneyDTO grabJourneyDTOFixture;
    @Fixture private Journey journeyFixture;
    @Fixture private CreateJourneyDTO createJourneyDTOFixture;
    @Fixture private List<Journey> journeyFixtureList;

    @BeforeEach
    public void setUp()
    {
        final JFixture jFixture = new JFixture();
        jFixture.customise().circularDependencyBehaviour().omitSpecimen();
        FixtureAnnotations.initFixtures(this, jFixture);
    }

    @Test
    public void get_Journey_By_Id_Returns_GrabJourneyDTO()
    {
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
    public void get_Journeys_Returns_List_of_GrabJourneyDTO()
    {
        when(journeyRepositoryMock.findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
                anyInt(), anyInt(), any(Instant.class), any(Instant.class))).thenReturn(journeyFixtureList);
        final List<GrabJourneyDTO> grabJourneyDTOList =
                MapperUtils.mapList(journeyFixtureList, GrabJourneyDTO.class, modelMapperMock);
        final Map<String, String> params = new HashMap<>();
            params.put("locationIdFrom", "1");
            params.put("locationIdTo", "2");
            params.put("dateTimeFrom", "2022-12-01T09:00:00Z");
            params.put("dateTimeTo", "2023-12-01T09:00:00Z");
        final List<GrabJourneyDTO> journeyList = journeyService.searchJourneys(params);

        assertThat(journeyList.size()).isEqualTo(grabJourneyDTOList.size());
        verify(journeyRepositoryMock).findJourneysByLocationFromIdAndLocationToIdAndDateTimeBetween(
                anyInt(), anyInt(), any(Instant.class), any(Instant.class));
    }

    @Test
    public void get_Journeys_By_Driver_Id_Returns_List_Of_GrabJourneyDTO()
    {
        when(journeyRepositoryMock.findJourneysByDriverId(anyInt())).thenReturn(journeyFixtureList);
        final List<GrabJourneyDTO> grabJourneyDTOList =
                MapperUtils.mapList(journeyFixtureList, GrabJourneyDTO.class, modelMapperMock);
        final List<GrabJourneyDTO> journeyDriverList = journeyService.getJourneysByDriverId(anyInt());

        assertThat(journeyDriverList.size()).isEqualTo(grabJourneyDTOList.size());
        verify(journeyRepositoryMock).findJourneysByDriverId(anyInt());
    }

    @Test
    public void get_Journeys_By_Passengers_Id_Returns_List_Of_GrabJourneyDTO()
    {
        when(journeyRepositoryMock.findJourneysByPassengersId(anyInt())).thenReturn(journeyFixtureList);
        final List<GrabJourneyDTO> grabJourneyDTOList =
                MapperUtils.mapList(journeyFixtureList, GrabJourneyDTO.class, modelMapperMock);
        final List<GrabJourneyDTO> journeyList = journeyService.getJourneysByPassengersId(anyInt());

        assertThat(journeyList.size()).isEqualTo(grabJourneyDTOList.size());
        verify(journeyRepositoryMock).findJourneysByPassengersId(anyInt());
    }

    @Test
    public void create_Journey_Returns_GrabJourneyDTO()
    {
        final Journey spyJourney = spy(new Journey());
        when(modelMapperMock.map(createJourneyDTOFixture, Journey.class)).thenReturn(spyJourney);
        when(journeyRepositoryMock.save(spyJourney)).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
        spyJourney.setCreatedDate(any(Instant.class));
        spyJourney.setLocationFrom(any(Location.class));
        spyJourney.setLocationTo(any(Location.class));
        spyJourney.setDriver(any(Driver.class));
        final GrabJourneyDTO grabJourneyDTO = journeyService.createJourney(createJourneyDTOFixture);

        assertThat(grabJourneyDTO.getCreatedDate()).isEqualTo(grabJourneyDTOFixture.getCreatedDate());
        assertThat(grabJourneyDTO.getLocationFrom()).isEqualTo(grabJourneyDTOFixture.getLocationFrom());
        assertThat(grabJourneyDTO.getLocationTo()).isEqualTo(grabJourneyDTOFixture.getLocationTo());
        assertThat(grabJourneyDTO.getDriver()).isEqualTo(grabJourneyDTOFixture.getDriver());
        verify(journeyRepositoryMock).save(any(Journey.class));
    }

    @Test
    public void patch_Journey_Returns_GrabJourneyDTO() throws Exception
    {
        when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
        final JsonPatch patch = PatchUtility.jsonPatch(PATCH_JOURNEY_VALID_JSON);
        final JsonNode journeyNode = PatchUtility.jsonNode(journeyFixture, patch);
        when(objectMapperMock.convertValue(grabJourneyDTOFixture, JsonNode.class)).thenReturn(journeyNode);
        when(objectMapperMock.treeToValue(journeyNode, Journey.class)).thenReturn(journeyFixture);
        when(journeyRepositoryMock.save(journeyFixture)).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
        final GrabJourneyDTO grabJourneyDTO = journeyService.patchJourney(
                journeyFixture.getId(), patch);

        assertThat(grabJourneyDTO.getMaxPassengers()).isEqualTo(grabJourneyDTOFixture.getMaxPassengers());
        assertThat(grabJourneyDTO.getLocationFrom().getId()).isEqualTo(grabJourneyDTOFixture.getLocationFrom().getId());
        verify(journeyRepositoryMock).findById(anyInt());
        verify(journeyRepositoryMock).save(any(Journey.class));
    }

    @Test
    public void delete_Journey_By_Id_Returns_Void()
    {
        journeyService.deleteJourneyById(anyInt());
        verify(journeyRepositoryMock).deleteById(anyInt());
    }

    @Test
    public void delete_By_JourneyId_And_PassengerId_Returns_Void()
    {
        journeyService.deleteByJourneyIdAndPassengerId(anyInt(), anyInt());
        verify(passengerJourneyRepositoryMock).deleteByJourneyIdAndPassengerId(anyInt(), anyInt());
    }
}
