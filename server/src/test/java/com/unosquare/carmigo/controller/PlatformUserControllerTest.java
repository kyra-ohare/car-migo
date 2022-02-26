package com.unosquare.carmigo.controller;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.model.response.PlatformUserViewModel;
import com.unosquare.carmigo.service.DriverService;
import com.unosquare.carmigo.service.PassengerService;
import com.unosquare.carmigo.service.PlatformUserService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PlatformUserControllerTest
{

    private static final String API_LEADING = "/v1/users/";
    private static final String POST_PLATFORM_USER_JSON =
            ResourceUtility.generateStringFromResource("requestJson/PlatformUser.json");

    private MockMvc mockMvc;

    @Mock
    private ModelMapper modelMapperMock;
    @Mock
    private PlatformUserService platformUserServiceMock;
    @Mock
    private DriverService driverServiceMock;
    @Mock
    private PassengerService passengerServiceMock;

    @Fixture
    private GrabPlatformUserDTO grabPlatformUserDTOFixture;
    @Fixture
    private PlatformUserViewModel platformUserViewModelFixture;

    @Before
    public void setUp()
    {
        final JFixture jFixture = new JFixture();
        jFixture.customise().circularDependencyBehaviour().omitSpecimen();
        FixtureAnnotations.initFixtures(this, jFixture);

        mockMvc = MockMvcBuilders.standaloneSetup(
                new PlatformUserController(
                        modelMapperMock,
                        platformUserServiceMock,
                        driverServiceMock,
                        passengerServiceMock)
        ).build();
    }

    @Test
    public void get_PlatformUser_By_Id_Returns_PlatformUserViewModel() throws Exception
    {
        when(platformUserServiceMock.getPlatformUserById(anyInt())).thenReturn(
                grabPlatformUserDTOFixture);
        when(modelMapperMock.map(grabPlatformUserDTOFixture, PlatformUserViewModel.class))
                .thenReturn(platformUserViewModelFixture);

        mockMvc.perform(get(API_LEADING + anyInt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
