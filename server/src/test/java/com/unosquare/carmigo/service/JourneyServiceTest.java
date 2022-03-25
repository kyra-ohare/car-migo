package com.unosquare.carmigo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

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

import java.time.Instant;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class JourneyServiceTest
{
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

    private Journey journey;

    @Fixture
    private GrabJourneyDTO grabJourneyDTOFixture;

    @Fixture
    private Journey journeyFixture;

    @Fixture
    private CreateJourneyDTO createJourneyDTOFixture;

    @Before
    public void classSetUp()
    {
        final JFixture jFixture = new JFixture();
        jFixture.customise().circularDependencyBehaviour().omitSpecimen();
        FixtureAnnotations.initFixtures(this, jFixture);
    }

    @BeforeEach
    public void setUp()
    {
        journey = new Journey();
    }

    @AfterEach
    public void tearDown()
    {
        journey = null;
    }

    @Test
    public void get_Journey_By_Id_Returns_GrabJourneyDTO() throws Exception
    {
        when(journeyRepositoryMock.findJourneyById(anyInt())).thenReturn(journey);
        when(modelMapperMock.map(journey, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
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

//    @Fixture
//    private List<Journey> journeysFixture;
    @Fixture
    private List<Journey> journeyFixtureList = List.of(new Journey());
// only mock methods that are calling external calls (dependencies)
    @Fixture
    private JsonNode jsonNodeFixture;
    @Test
    public void patch_Journey_Returns_GrabJourneyDTO() throws Exception
    {
        when(journeyRepositoryMock.findJourneyByDriverId(anyInt())).thenReturn(journeyFixtureList); // size 3
        journeyFixtureList.add(journeyFixture);

//        List<Journey> journeys = List.of(new Journey());
//        when(journeysFixture.get(anyInt())).thenReturn(journeyFixture);
//        when(journeyOptionalFixture.isPresent()).thenReturn();
//        when(journeysFixture.get(anyInt())).thenReturn(journey);

        when(modelMapperMock.map(journeyFixtureList.get(0), GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);
        final String patchString = "[{ \"op\": \"replace\", \"path\": \"/maxPassengers\", \"value\": \"" + 5 + "\" }]";
        final ObjectMapper objectMapper = new MapperConfiguration().objectMapper();

//        final JsonPatch patch = JsonPatch.fromJson(objectMapper.readTree(patchString));
//        final JsonNode journeyNode = patch.apply(objectMapper.convertValue(grabJourneyDTOFixture, JsonNode.class));
//        final Journey patchedJourney = objectMapper.treeToValue(journeyNode, Journey.class);
        when(objectMapperMock.convertValue(any(GrabJourneyDTO.class), JsonNode.class)).thenReturn(jsonNodeFixture);

        when(journeyRepositoryMock.save(patchedJourney)).thenReturn(journeyFixture);
        when(modelMapperMock.map(journeyFixture, GrabJourneyDTO.class)).thenReturn(grabJourneyDTOFixture);


        final GrabJourneyDTO grabJourneyDTO = journeyService.patchJourney(journeyFixture.getId(), journeyFixture.getDriver().getId(), any(JsonPatch.class));
    }

    private JsonNode createJsonNodeJourney() {

    }
    @Test
    public void delete_Journey_By_Id_Returns_Void() throws Exception
    {
        journeyService.deleteJourneyById(anyInt());
        verify(journeyRepositoryMock).deleteById(anyInt());
    }

//    private List<JsonPatchOperation> jsonPatchOperation() {
//        final List<JsonPatchOperation> operations = new ArrayList<>();
//        JsonNode node =
//        JsonPatchOperation first =
//        operations.add();
//        return operations;
//    }
}
