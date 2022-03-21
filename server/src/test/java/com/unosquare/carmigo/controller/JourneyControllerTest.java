package com.unosquare.carmigo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
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
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.model.response.JourneyViewModel;
import com.unosquare.carmigo.service.JourneyService;
import com.unosquare.carmigo.util.ResourceUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class JourneyControllerTest
{
    private static final String API_LEADING = "/v1/journeys/";
    private static final String POST_JOURNEY_VALID_JSON =
        ResourceUtility.generateStringFromResource("requestJson/PostJourneyValid.json");
    private static final String POST_JOURNEY_INVALID_JSON =
        ResourceUtility.generateStringFromResource("requestJson/PostJourneyInvalid.json");
    private static final String PATCH_JOURNEY_VALID_JSON =
        ResourceUtility.generateStringFromResource("requestJson/PatchJourneyValid.json");
    private static final String PATCH_JOURNEY_INVALID_JSON =
        ResourceUtility.generateStringFromResource("requestJson/PatchJourneyInvalid.json");

    private MockMvc mockMvc;

    @Mock
    private ModelMapper modelMapperMock;
    @Mock
    private JourneyService journeyServiceMock;

    @Fixture
    private GrabJourneyDTO grabJourneyDTOFixture;
    @Fixture
    private JourneyViewModel journeyViewModelFixture;

    @Before
    public void setUp() throws Exception
    {
        final JFixture jFixture = new JFixture();
        jFixture.customise().circularDependencyBehaviour().omitSpecimen();
        FixtureAnnotations.initFixtures(this, jFixture);

        mockMvc = MockMvcBuilders.standaloneSetup(
            new JourneyController(
                modelMapperMock,
                journeyServiceMock))
            .build();
    }

    @Test
    public void get_Journey_By_Id_Returns_JourneyViewModel() throws Exception
    {
        when(journeyServiceMock.getJourneyById(anyInt()))
            .thenReturn(grabJourneyDTOFixture);
        when(modelMapperMock.map(grabJourneyDTOFixture, JourneyViewModel.class))
            .thenReturn(journeyViewModelFixture);

        mockMvc.perform(get(API_LEADING + anyInt())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(journeyServiceMock).getJourneyById(anyInt());
    }

    @Test
    public void post_Journey_Returns_HttpStatus_Created() throws Exception
    {
        mockMvc.perform(post(API_LEADING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(POST_JOURNEY_VALID_JSON))
            .andExpect(status().isCreated());
        verify(journeyServiceMock).createJourney(any());
    }

    @Test
    public void post_Journey_Returns_HttpStatus_BadRequest() throws Exception
    {
        mockMvc.perform(post(API_LEADING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(POST_JOURNEY_INVALID_JSON))
            .andExpect(status().isBadRequest());
        verify(journeyServiceMock, times(0)).createJourney(any());
    }

    @Test
    public void patch_Journey_Returns_HttpStatus_Accepted() throws Exception
    {
        mockMvc.perform(patch(API_LEADING + "1/drivers/1")
                .contentType("application/json-patch+json")
                .content(PATCH_JOURNEY_VALID_JSON))
            .andExpect(status().isOk());
        verify(journeyServiceMock).patchJourney(anyInt(), anyInt(), any());
    }

    @Test
    public void patch_Journey_Returns_HttpStatus_BadRequest() throws Exception
    {
        mockMvc.perform(patch(API_LEADING + "1/drivers/1")
                .contentType("application/json-patch+json")
                .content(PATCH_JOURNEY_INVALID_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_Journey_Returns_HttpStatus_No_Content() throws Exception
    {
        doNothing().when(journeyServiceMock).deleteJourneyById(anyInt());
        mockMvc.perform(delete(API_LEADING + anyInt()))
            .andExpect(status().isNoContent());
    }
}
