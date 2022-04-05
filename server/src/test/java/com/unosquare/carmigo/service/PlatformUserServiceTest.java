package com.unosquare.carmigo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.DriverRepository;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.repository.PlatformUserRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlatformUserServiceTest
{
    private static final String PATCH_PLATFORM_USER_VALID_JSON =
            ResourceUtility.generateStringFromResource("requestJson/PatchPlatformUserValid.json");

    @Mock
    private PlatformUserRepository platformUserRepositoryMock;
    @Mock
    private DriverRepository driverRepositoryMock;
    @Mock
    private PassengerRepository passengerRepositoryMock;
    @Mock
    private ModelMapper modelMapperMock;
    @Mock
    private ObjectMapper objectMapperMock;
    @Mock
    private EntityManager entityManagerMock;

    @InjectMocks
    private PlatformUserService platformUserService;

    @Fixture
    private PlatformUser platformUserFixture;
    @Fixture
    private Driver driverFixture;
    @Fixture
    private Passenger passengerFixture;
    @Fixture
    private CreatePlatformUserDTO createPlatformUserDTOFixture;
    @Fixture
    private CreateDriverDTO createDriverDTOFixture;
    @Fixture
    private GrabPlatformUserDTO grabPlatformUserDTOFixture;
    @Fixture
    private GrabDriverDTO grabDriverDTOFixture;
    @Fixture
    private GrabPassengerDTO grabPassengerDTOFixture;

    @BeforeEach
    public void setUp()
    {
        final JFixture jFixture = new JFixture();
        jFixture.customise().circularDependencyBehaviour().omitSpecimen();
        FixtureAnnotations.initFixtures(this, jFixture);
    }

    @Test
    public void get_PlatformUser_By_Id_Returns_GrabPlatformUserDTO()
    {
        when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class)).thenReturn(grabPlatformUserDTOFixture);
        final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.getPlatformUserById(anyInt());

        assertThat(grabPlatformUserDTO.getId()).isEqualTo(grabPlatformUserDTOFixture.getId());
        assertThat(grabPlatformUserDTO.getCreatedDate()).isEqualTo(grabPlatformUserDTOFixture.getCreatedDate());
        assertThat(grabPlatformUserDTO.getDob()).isEqualTo(grabPlatformUserDTOFixture.getDob());
        assertThat(grabPlatformUserDTO.getEmail()).isEqualTo(grabPlatformUserDTOFixture.getEmail());
        assertThat(grabPlatformUserDTO.getFirstName()).isEqualTo(grabPlatformUserDTOFixture.getFirstName());
        assertThat(grabPlatformUserDTO.getLastName()).isEqualTo(grabPlatformUserDTOFixture.getLastName());
        assertThat(grabPlatformUserDTO.getPhoneNumber()).isEqualTo(grabPlatformUserDTOFixture.getPhoneNumber());
        verify(platformUserRepositoryMock).findById(anyInt());
    }

    @Test
    public void create_PlatformUser_Returns_GrabPlatformUserDTO()
    {
        final PlatformUser spyPlatformUser = spy(new PlatformUser());
        when(modelMapperMock.map(createPlatformUserDTOFixture, PlatformUser.class)).thenReturn(spyPlatformUser);
        when(platformUserRepositoryMock.save(spyPlatformUser)).thenReturn(platformUserFixture);
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class)).thenReturn(grabPlatformUserDTOFixture);
        spyPlatformUser.setCreatedDate(any(Instant.class));
        spyPlatformUser.setFirstName(any(String.class));
        spyPlatformUser.setLastName(any(String.class));
        spyPlatformUser.setEmail(any(String.class));
        final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.createPlatformUser(createPlatformUserDTOFixture);

        assertThat(grabPlatformUserDTO.getCreatedDate()).isEqualTo(grabPlatformUserDTOFixture.getCreatedDate());
        assertThat(grabPlatformUserDTO.getFirstName()).isEqualTo(grabPlatformUserDTOFixture.getFirstName());
        assertThat(grabPlatformUserDTO.getLastName()).isEqualTo(grabPlatformUserDTOFixture.getLastName());
        assertThat(grabPlatformUserDTO.getEmail()).isEqualTo(grabPlatformUserDTOFixture.getEmail());
        verify(platformUserRepositoryMock).save(any(PlatformUser.class));
    }

    @Test
    public void patch_PlatformUser_Returns_GrabPlatformUserDTO() throws Exception
    {
        when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class)).thenReturn(grabPlatformUserDTOFixture);
        final JsonPatch patch = PatchUtility.jsonPatch(PATCH_PLATFORM_USER_VALID_JSON);
        final JsonNode platformUserNode = PatchUtility.jsonNode(platformUserFixture, patch);
        when(objectMapperMock.convertValue(grabPlatformUserDTOFixture, JsonNode.class)).thenReturn(platformUserNode);
        when(objectMapperMock.treeToValue(platformUserNode, PlatformUser.class)).thenReturn(platformUserFixture);
        when(platformUserRepositoryMock.save(platformUserFixture)).thenReturn(platformUserFixture);
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class)).thenReturn(grabPlatformUserDTOFixture);
        final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.patchPlatformUser(
                platformUserFixture.getId(), patch);

        assertThat(grabPlatformUserDTO.getFirstName()).isEqualTo(grabPlatformUserDTOFixture.getFirstName());
        assertThat(grabPlatformUserDTO.getLastName()).isEqualTo(grabPlatformUserDTOFixture.getLastName());
        assertThat(grabPlatformUserDTO.getDob()).isEqualTo(grabPlatformUserDTOFixture.getDob());
        assertThat(grabPlatformUserDTO.getCreatedDate()).isEqualTo(grabPlatformUserDTOFixture.getCreatedDate());
        verify(platformUserRepositoryMock).findById(anyInt());
        verify(platformUserRepositoryMock).save(any(PlatformUser.class));
    }

    @Test
    public void delete_PlatformUser_By_Id_Returns_Void()
    {
        platformUserService.deletePlatformUserById(anyInt());
        verify(platformUserRepositoryMock).deleteById(anyInt());
    }

    @Test
    public void get_Driver_By_Id_Returns_GrabDriverDTO()
    {
        when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
        when(modelMapperMock.map(driverFixture, GrabDriverDTO.class)).thenReturn(grabDriverDTOFixture);
        final GrabDriverDTO grabDriverDTO = platformUserService.getDriverById(anyInt());

        assertThat(grabDriverDTO.getId()).isEqualTo(grabDriverDTOFixture.getId());
        assertThat(grabDriverDTO.getLicenseNumber()).isEqualTo(grabDriverDTOFixture.getLicenseNumber());
        assertThat(grabDriverDTO.getPlatformUser()).isEqualTo(grabDriverDTOFixture.getPlatformUser());
        verify(driverRepositoryMock).findById(anyInt());
    }

    @Test
    public void create_Driver_Returns_GrabDriverDTO()
    {
        final Driver spyDriver = spy(new Driver());
        when(modelMapperMock.map(createDriverDTOFixture, Driver.class)).thenReturn(spyDriver);
        when(driverRepositoryMock.save(spyDriver)).thenReturn(driverFixture);
        when(modelMapperMock.map(driverFixture, GrabDriverDTO.class)).thenReturn(grabDriverDTOFixture);
        spyDriver.setLicenseNumber(any(String.class));
        spyDriver.setPlatformUser(any(PlatformUser.class));
        final GrabDriverDTO grabDriverDTO = platformUserService.createDriver(1, createDriverDTOFixture);

        assertThat(grabDriverDTO.getLicenseNumber()).isEqualTo(grabDriverDTOFixture.getLicenseNumber());
        assertThat(grabDriverDTO.getPlatformUser()).isEqualTo(grabDriverDTOFixture.getPlatformUser());
        verify(driverRepositoryMock).save(any(Driver.class));
    }

    @Test
    public void delete_Driver_By_Id_Returns_Void()
    {
        platformUserService.deleteDriverById(anyInt());
        verify(driverRepositoryMock).deleteById(anyInt());
    }

    @Test
    public void get_Passenger_By_Id_Returns_GrabPassengerDTO()
    {
        when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.of(passengerFixture));
        when(modelMapperMock.map(passengerFixture, GrabPassengerDTO.class)).thenReturn(grabPassengerDTOFixture);
        final GrabPassengerDTO grabPassengerDTO = platformUserService.getPassengerById(anyInt());

        assertThat(grabPassengerDTO.getId()).isEqualTo(grabPassengerDTOFixture.getId());
        assertThat(grabPassengerDTO.getPlatformUser()).isEqualTo(grabPassengerDTOFixture.getPlatformUser());
        verify(passengerRepositoryMock).findById(anyInt());
    }

    @Test
    public void create_Passenger_Returns_GrabPassengerDTO()
    {
        platformUserService.createPassenger(1);
        verify(passengerRepositoryMock).save(any(Passenger.class));
    }

    @Test
    public void delete_Passenger_By_Id_Returns_Void()
    {
        platformUserService.deletePassengerById(anyInt());
        verify(passengerRepositoryMock).deleteById(anyInt());
    }
}
