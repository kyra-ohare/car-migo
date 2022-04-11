package com.unosquare.carmigo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDriverDTO;
import com.unosquare.carmigo.dto.GrabJourneyPassengerDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.repository.JourneyRepository;
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
import java.util.List;
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
    @Mock private ModelMapper modelMapperMock;
    @Mock private ObjectMapper objectMapperMock;
    @Mock private EntityManager entityManagerMock;
    @InjectMocks private JourneyService journeyService;

    @Fixture private GrabJourneyDriverDTO grabJourneyDriverDTOFixture;
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
        when(modelMapperMock.map(journeyFixture, GrabJourneyDriverDTO.class)).thenReturn(grabJourneyDriverDTOFixture);
        final GrabJourneyDriverDTO grabJourneyDriverDTO = journeyService.getJourneyById(anyInt());

        assertThat(grabJourneyDriverDTO.getId()).isEqualTo(grabJourneyDriverDTOFixture.getId());
        assertThat(grabJourneyDriverDTO.getCreatedDate()).isEqualTo(grabJourneyDriverDTOFixture.getCreatedDate());
        assertThat(grabJourneyDriverDTO.getLocationFrom()).isEqualTo(grabJourneyDriverDTOFixture.getLocationFrom());
        assertThat(grabJourneyDriverDTO.getLocationTo()).isEqualTo(grabJourneyDriverDTOFixture.getLocationTo());
        assertThat(grabJourneyDriverDTO.getMaxPassengers()).isEqualTo(grabJourneyDriverDTOFixture.getMaxPassengers());
        assertThat(grabJourneyDriverDTO.getDateTime()).isEqualTo(grabJourneyDriverDTOFixture.getDateTime());
        assertThat(grabJourneyDriverDTO.getDriver()).isEqualTo(grabJourneyDriverDTOFixture.getDriver());
        verify(journeyRepositoryMock).findById(anyInt());
    }

    @Test
    public void get_Journeys_Returns_List_of_GrabJourneyDTO()
    {
        when(journeyRepositoryMock.findAll()).thenReturn(journeyFixtureList);
        final List<GrabJourneyDriverDTO> grabJourneyDriverDTOList =
                MapperUtils.mapList(journeyFixtureList, GrabJourneyDriverDTO.class, modelMapperMock);
        final List<GrabJourneyDriverDTO> journeyList = journeyService.getJourneys();

        assertThat(journeyList.size()).isEqualTo(grabJourneyDriverDTOList.size());
        verify(journeyRepositoryMock).findAll();
    }

    @Test
    public void get_Journeys_By_Driver_Id_Returns_List_Of_GrabJourneyPassengerDTO()
    {
        when(journeyRepositoryMock.findJourneysByDriverId(anyInt())).thenReturn(journeyFixtureList);
        final List<GrabJourneyPassengerDTO> grabJourneyPassengerDTOList =
                MapperUtils.mapList(journeyFixtureList, GrabJourneyPassengerDTO.class, modelMapperMock);
        final List<GrabJourneyPassengerDTO> journeyDriverList = journeyService.getJourneysByDriverId(anyInt());

        assertThat(journeyDriverList.size()).isEqualTo(grabJourneyPassengerDTOList.size());
        verify(journeyRepositoryMock).findJourneysByDriverId(anyInt());
    }

    @Test
    public void get_Journeys_By_Passengers_Id_Returns_List_Of_GrabJourneyDriverDTO()
    {
        when(journeyRepositoryMock.findJourneysByPassengersId(anyInt())).thenReturn(journeyFixtureList);
        final List<GrabJourneyDriverDTO> grabJourneyDriverDTOList =
                MapperUtils.mapList(journeyFixtureList, GrabJourneyDriverDTO.class, modelMapperMock);
        final List<GrabJourneyDriverDTO> journeyList = journeyService.getJourneysByPassengersId(anyInt());

        assertThat(journeyList.size()).isEqualTo(grabJourneyDriverDTOList.size());
        verify(journeyRepositoryMock).findJourneysByPassengersId(anyInt());
    }

    @Test
    public void create_Journey_Returns_GrabJourneyDTO()
    {
        final Journey spyJourney = spy(new Journey());
        when(modelMapperMock.map(createJourneyDTOFixture, Journey.class)).thenReturn(spyJourney);
        when(journeyRepositoryMock.save(spyJourney)).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDriverDTO.class)).thenReturn(grabJourneyDriverDTOFixture);
        spyJourney.setCreatedDate(any(Instant.class));
        spyJourney.setLocationFrom(any(Location.class));
        spyJourney.setLocationTo(any(Location.class));
        spyJourney.setDriver(any(Driver.class));
        final GrabJourneyDriverDTO grabJourneyDriverDTO = journeyService.createJourney(createJourneyDTOFixture);

        assertThat(grabJourneyDriverDTO.getCreatedDate()).isEqualTo(grabJourneyDriverDTOFixture.getCreatedDate());
        assertThat(grabJourneyDriverDTO.getLocationFrom()).isEqualTo(grabJourneyDriverDTOFixture.getLocationFrom());
        assertThat(grabJourneyDriverDTO.getLocationTo()).isEqualTo(grabJourneyDriverDTOFixture.getLocationTo());
        assertThat(grabJourneyDriverDTO.getDriver()).isEqualTo(grabJourneyDriverDTOFixture.getDriver());
        verify(journeyRepositoryMock).save(any(Journey.class));
    }

    @Test
    public void patch_Journey_Returns_GrabJourneyDTO() throws Exception
    {
        when(journeyRepositoryMock.findById(anyInt())).thenReturn(Optional.of(journeyFixture));
        when(modelMapperMock.map(journeyFixture, GrabJourneyDriverDTO.class)).thenReturn(grabJourneyDriverDTOFixture);
        final JsonPatch patch = PatchUtility.jsonPatch(PATCH_JOURNEY_VALID_JSON);
        final JsonNode journeyNode = PatchUtility.jsonNode(journeyFixture, patch);
        when(objectMapperMock.convertValue(grabJourneyDriverDTOFixture, JsonNode.class)).thenReturn(journeyNode);
        when(objectMapperMock.treeToValue(journeyNode, Journey.class)).thenReturn(journeyFixture);
        when(journeyRepositoryMock.save(journeyFixture)).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDriverDTO.class)).thenReturn(grabJourneyDriverDTOFixture);
        final GrabJourneyDriverDTO grabJourneyDriverDTO = journeyService.patchJourney(
                journeyFixture.getId(), patch);

        assertThat(grabJourneyDriverDTO.getMaxPassengers()).isEqualTo(grabJourneyDriverDTOFixture.getMaxPassengers());
        assertThat(grabJourneyDriverDTO.getLocationFrom().getId()).isEqualTo(grabJourneyDriverDTOFixture.getLocationFrom().getId());
        verify(journeyRepositoryMock).findById(anyInt());
        verify(journeyRepositoryMock).save(any(Journey.class));
    }

    @Test
    public void delete_Journey_By_Id_Returns_Void()
    {
        journeyService.deleteJourneyById(anyInt());
        verify(journeyRepositoryMock).deleteById(anyInt());
    }
}
