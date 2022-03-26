package com.unosquare.carmigo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.configuration.MapperConfiguration;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.repository.JourneyRepository;
import com.unosquare.carmigo.util.ResourceUtility;
import java.time.Instant;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class JourneyServiceTest
{
    private static final String PATCH_JOURNEY_VALID_JSON =
        ResourceUtility.generateStringFromResource("requestJson/PatchJourneyValid.json");
    @Mock
    private JourneyRepository journeyRepositoryMock;
    @Mock
    private ModelMapper modelMapperMock;
    @Mock
    private ObjectMapper objectMapperMock;
    @Mock
    private EntityManager entityManagerMock;

    @InjectMocks
    private JourneyService journeyService;

    @Fixture
    private GrabJourneyDTO grabJourneyDTOFixture;

    @Fixture
    private Journey journeyFixture;

    @Fixture
    private CreateJourneyDTO createJourneyDTOFixture;

    @Fixture
    private List<Journey> journeyFixtureList;

    final ObjectMapper objectMapper = new MapperConfiguration().objectMapper();

    @Before
    public void setUp()
    {
        final JFixture jFixture = new JFixture();
        jFixture.customise().circularDependencyBehaviour().omitSpecimen();
        FixtureAnnotations.initFixtures(this, jFixture);
    }

    @Test
    public void get_Journey_By_Id_Returns_GrabJourneyDTO() throws Exception
    {
        when(journeyRepositoryMock.findJourneyById(anyInt())).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
        final GrabJourneyDTO grabJourneyDTO = journeyService.getJourneyById(anyInt());

        assertThat(grabJourneyDTO.getId()).isEqualTo(grabJourneyDTOFixture.getId());
        assertThat(grabJourneyDTO.getCreatedDate()).isEqualTo(grabJourneyDTOFixture.getCreatedDate());
        assertThat(grabJourneyDTO.getLocationFrom()).isEqualTo(grabJourneyDTOFixture.getLocationFrom());
        assertThat(grabJourneyDTO.getLocationTo()).isEqualTo(grabJourneyDTOFixture.getLocationTo());
        assertThat(grabJourneyDTO.getMaxPassengers()).isEqualTo(grabJourneyDTOFixture.getMaxPassengers());
        assertThat(grabJourneyDTO.getDateTime()).isEqualTo(grabJourneyDTOFixture.getDateTime());
        assertThat(grabJourneyDTO.getDriver()).isEqualTo(grabJourneyDTOFixture.getDriver());
        verify(journeyRepositoryMock).findJourneyById(anyInt());
    }

    @Test
    public void create_Journey_Returns_GrabJourneyDTO() throws Exception
    {
        final Journey spyJourney = spy(new Journey());
        when(modelMapperMock.map(createJourneyDTOFixture, Journey.class)).thenReturn(spyJourney);
        spyJourney.setCreatedDate(any(Instant.class));
        spyJourney.setLocationFrom(any(Location.class));
        spyJourney.setLocationTo(any(Location.class));
        spyJourney.setDriver(any(Driver.class));
        when(journeyRepositoryMock.save(spyJourney)).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
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
        when(journeyRepositoryMock.findJourneyByDriverId(anyInt())).thenReturn(journeyFixtureList);
        journeyFixtureList.add(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
        final JsonPatch patch = jsonPatch();
        final JsonNode journeyNode = jsonNodeJourney(patch);
        when(objectMapperMock.convertValue(grabJourneyDTOFixture, JsonNode.class)).thenReturn(journeyNode);
        when(objectMapperMock.treeToValue(journeyNode, Journey.class)).thenReturn(journeyFixture);
        when(journeyRepositoryMock.save(journeyFixture)).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
        final GrabJourneyDTO grabJourneyDTO = journeyService.patchJourney(
            journeyFixture.getId(), journeyFixture.getDriver().getId(), patch);

        assertThat(grabJourneyDTO.getMaxPassengers()).isEqualTo(grabJourneyDTOFixture.getMaxPassengers());
        assertThat(grabJourneyDTO.getLocationFrom().getId()).isEqualTo(grabJourneyDTOFixture.getLocationFrom().getId());
        verify(journeyRepositoryMock).findJourneyByDriverId(anyInt());
        verify(journeyRepositoryMock).save(any(Journey.class));
    }

    @Test
    public void delete_Journey_By_Id_Returns_Void() throws Exception
    {
        journeyService.deleteJourneyById(anyInt());
        verify(journeyRepositoryMock).deleteById(anyInt());
    }

    private JsonNode jsonNodeJourney(final JsonPatch jsonPatch) throws Exception
    {
        return jsonPatch.apply(objectMapper.convertValue(journeyFixture, JsonNode.class));
    }

    private JsonPatch jsonPatch() throws Exception
    {
        return JsonPatch.fromJson(objectMapper.readTree(PATCH_JOURNEY_VALID_JSON));
    }
}
