package com.unosquare.carmigo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateAuthenticationDTO;
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabAuthenticationDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.DriverRepository;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import com.unosquare.carmigo.util.JwtTokenUtils;
import com.unosquare.carmigo.util.PatchUtility;
import com.unosquare.carmigo.util.ResourceUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.unosquare.carmigo.contant.AppContants.ACTIVE;
import static com.unosquare.carmigo.contant.AppContants.ADMIN;
import static com.unosquare.carmigo.contant.AppContants.DEV;
import static com.unosquare.carmigo.contant.AppContants.LOCKED_OUT;
import static com.unosquare.carmigo.contant.AppContants.NO_PERMISSIONS;
import static com.unosquare.carmigo.contant.AppContants.STAGED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlatformUserServiceTest
{
    private static final String PATCH_PLATFORM_USER_VALID_JSON =
            ResourceUtility.generateStringFromResource("requestJson/PatchPlatformUserValid.json");

    @Mock private PlatformUserRepository platformUserRepositoryMock;
    @Mock private DriverRepository driverRepositoryMock;
    @Mock private PassengerRepository passengerRepositoryMock;
    @Mock private UserSecurityService userSecurityServiceMock;
    @Mock private ModelMapper modelMapperMock;
    @Mock private ObjectMapper objectMapperMock;
    @Mock private EntityManager entityManagerMock;
    @Mock private AuthenticationManager authenticationManagerMock;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoderMock;
    @Mock private JwtTokenUtils jwtTokenUtilsMock;
    @InjectMocks private PlatformUserService platformUserService;

    @Fixture private PlatformUser platformUserFixture;
    @Fixture private Driver driverFixture;
    @Fixture private Passenger passengerFixture;
    @Fixture private CreateAuthenticationDTO createAuthenticationDTOFixture;
    @Fixture private CreatePlatformUserDTO createPlatformUserDTOFixture;
    @Fixture private CreateDriverDTO createDriverDTOFixture;
    @Fixture private GrabAuthenticationDTO grabAuthenticationDTOFixture;
    @Fixture private GrabPlatformUserDTO grabPlatformUserDTOFixture;
    @Fixture private GrabDriverDTO grabDriverDTOFixture;
    @Fixture private GrabPassengerDTO grabPassengerDTOFixture;

    @BeforeEach
    public void setUp()
    {
        final JFixture jFixture = new JFixture();
        jFixture.customise().circularDependencyBehaviour().omitSpecimen();
        FixtureAnnotations.initFixtures(this, jFixture);
    }

    @Test
    public void create_Authentication_Token_Returns_GrabAuthenticationDTO()
    {
        final UserDetails spyUserDetails = spy(new User("foo", "foo", new ArrayList<>()));
        when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(any());
        when(userSecurityServiceMock.loadUserByUsername(createAuthenticationDTOFixture.getEmail()))
                .thenReturn(spyUserDetails);
        when(jwtTokenUtilsMock.generateToken(spyUserDetails)).thenReturn(anyString());
        final GrabAuthenticationDTO grabAuthenticationDTO =
                platformUserService.createAuthenticationToken(createAuthenticationDTOFixture);
        grabAuthenticationDTO.setJwt(grabAuthenticationDTOFixture.getJwt());

        assertThat(grabAuthenticationDTO.getJwt()).isEqualTo(grabAuthenticationDTOFixture.getJwt());
        verify(authenticationManagerMock).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userSecurityServiceMock).loadUserByUsername(anyString());
        verify(jwtTokenUtilsMock).generateToken(any(UserDetails.class));
    }

    @Test
    public void get_PlatformUser_By_Id_As_ACTIVE_Returns_GrabPlatformUserDTO()
    {
        get_PlatformUser_By_Id_Returns_GrabPlatformUserDTO(ACTIVE);
    }

    @Test
    public void get_PlatformUser_By_Id_As_ADMIN_Returns_GrabPlatformUserDTO()
    {
        get_PlatformUser_By_Id_Returns_GrabPlatformUserDTO(ADMIN);
    }

    @Test
    public void get_PlatformUser_By_Id_As_DEV_Returns_GrabPlatformUserDTO()
    {
        get_PlatformUser_By_Id_Returns_GrabPlatformUserDTO(DEV);
    }

    @Test
    public void get_PlatformUser_By_Id_As_STAGED_Throws_AuthenticationException()
    {
        get_PlatformUser_By_Id_Throws_AuthenticationException(STAGED);
    }

    @Test
    public void get_PlatformUser_By_Id_As_LOCKED_OUT_Throws_AuthenticationException()
    {
        get_PlatformUser_By_Id_Throws_AuthenticationException(LOCKED_OUT);
    }

    @Test
    public void create_PlatformUser_Returns_GrabPlatformUserDTO()
    {
        final PlatformUser spyPlatformUser = spy(new PlatformUser());
        when(modelMapperMock.map(createPlatformUserDTOFixture, PlatformUser.class)).thenReturn(spyPlatformUser);
        when(platformUserRepositoryMock.save(spyPlatformUser)).thenReturn(platformUserFixture);
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class))
                .thenReturn(grabPlatformUserDTOFixture);
        spyPlatformUser.setCreatedDate(any(Instant.class));
        spyPlatformUser.setFirstName(anyString());
        spyPlatformUser.setLastName(anyString());
        spyPlatformUser.setEmail(anyString());
        spyPlatformUser.setPassword(bCryptPasswordEncoderMock.encode(anyString()));
        final GrabPlatformUserDTO grabPlatformUserDTO =
                platformUserService.createPlatformUser(createPlatformUserDTOFixture);

        assertThat(grabPlatformUserDTO.getCreatedDate()).isEqualTo(grabPlatformUserDTOFixture.getCreatedDate());
        assertThat(grabPlatformUserDTO.getFirstName()).isEqualTo(grabPlatformUserDTOFixture.getFirstName());
        assertThat(grabPlatformUserDTO.getLastName()).isEqualTo(grabPlatformUserDTOFixture.getLastName());
        assertThat(grabPlatformUserDTO.getEmail()).isEqualTo(grabPlatformUserDTOFixture.getEmail());
        verify(platformUserRepositoryMock).save(any(PlatformUser.class));
    }

    @Test
    public void patch_PlatformUser_As_ACTIVE_Returns_GrabPlatformUserDTO() throws Exception
    {
        when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class))
                .thenReturn(grabPlatformUserDTOFixture);
        final JsonPatch patch = PatchUtility.jsonPatch(PATCH_PLATFORM_USER_VALID_JSON);
        final JsonNode platformUserNode = PatchUtility.jsonNode(platformUserFixture, patch);
        when(objectMapperMock.convertValue(grabPlatformUserDTOFixture, JsonNode.class)).thenReturn(platformUserNode);
        when(objectMapperMock.treeToValue(platformUserNode, PlatformUser.class)).thenReturn(platformUserFixture);
        when(platformUserRepositoryMock.save(platformUserFixture)).thenReturn(platformUserFixture);
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class))
                .thenReturn(grabPlatformUserDTOFixture);
        final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.patchPlatformUser(
                platformUserFixture.getId(), patch, authentication(platformUserFixture.getEmail(), ACTIVE));

        assertThat(grabPlatformUserDTO.getFirstName()).isEqualTo(grabPlatformUserDTOFixture.getFirstName());
        assertThat(grabPlatformUserDTO.getLastName()).isEqualTo(grabPlatformUserDTOFixture.getLastName());
        assertThat(grabPlatformUserDTO.getDob()).isEqualTo(grabPlatformUserDTOFixture.getDob());
        assertThat(grabPlatformUserDTO.getCreatedDate()).isEqualTo(grabPlatformUserDTOFixture.getCreatedDate());
        verify(platformUserRepositoryMock).findById(anyInt());
        verify(platformUserRepositoryMock).save(any(PlatformUser.class));
    }

    @Test
    public void patch_PlatformUser_As_STAGED_Throws_AuthenticationException()
    {
        try {
            when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
            final JsonPatch patch = PatchUtility.jsonPatch(PATCH_PLATFORM_USER_VALID_JSON);
            platformUserService.patchPlatformUser(
                    platformUserFixture.getId(), patch, authentication(platformUserFixture.getEmail(), STAGED));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(platformUserRepositoryMock).findById(anyInt());
        verify(platformUserRepositoryMock, times(0)).save(any(PlatformUser.class));
    }

    @Test
    public void delete_PlatformUser_By_Id_As_ACTIVE_Returns_Void()
    {
        delete_PlatformUser_By_Id(ACTIVE, 1);
    }

    @Test
    public void delete_PlatformUser_By_Id_As_STAGED_Throws_AuthenticationException()
    {
        delete_PlatformUser_By_Id(STAGED, 0);
    }

    @Test
    public void get_Driver_By_Id_As_ACTIVE_Returns_GrabDriverDTO()
    {
        when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
        when(modelMapperMock.map(driverFixture, GrabDriverDTO.class)).thenReturn(grabDriverDTOFixture);
        final GrabDriverDTO grabDriverDTO = platformUserService.getDriverById(
                anyInt(), authentication(driverFixture.getPlatformUser().getEmail(), ACTIVE));

        assertThat(grabDriverDTO.getId()).isEqualTo(grabDriverDTOFixture.getId());
        assertThat(grabDriverDTO.getLicenseNumber()).isEqualTo(grabDriverDTOFixture.getLicenseNumber());
        assertThat(grabDriverDTO.getPlatformUser()).isEqualTo(grabDriverDTOFixture.getPlatformUser());
        verify(driverRepositoryMock).findById(anyInt());
    }

    @Test
    public void get_Driver_By_Id_As_STAGED_Throws_AuthenticationException()
    {
        try {
            when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
            platformUserService.getDriverById(
                    anyInt(), authentication(driverFixture.getPlatformUser().getEmail(), STAGED));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(driverRepositoryMock).findById(anyInt());
    }

    @Test
    public void create_Driver_As_ACTIVE_Returns_GrabDriverDTO()
    {
        final Driver spyDriver = spy(new Driver());
        when(modelMapperMock.map(createDriverDTOFixture, Driver.class)).thenReturn(spyDriver);
        when(entityManagerMock.getReference(eq(PlatformUser.class), anyInt())).thenReturn(platformUserFixture);
        when(driverRepositoryMock.save(spyDriver)).thenReturn(driverFixture);
        when(modelMapperMock.map(driverFixture, GrabDriverDTO.class)).thenReturn(grabDriverDTOFixture);
        final GrabDriverDTO grabDriverDTO = platformUserService.createDriver(
                1, createDriverDTOFixture, authentication(platformUserFixture.getEmail(), ACTIVE));

        assertThat(grabDriverDTO.getLicenseNumber()).isEqualTo(grabDriverDTOFixture.getLicenseNumber());
        assertThat(grabDriverDTO.getPlatformUser()).isEqualTo(grabDriverDTOFixture.getPlatformUser());
        verify(driverRepositoryMock).save(any(Driver.class));
    }

    @Test
    public void create_Driver_As_STAGED_Throws_AuthenticationException()
    {
        try {
            final Driver spyDriver = spy(new Driver());
            when(modelMapperMock.map(createDriverDTOFixture, Driver.class)).thenReturn(spyDriver);
            when(entityManagerMock.getReference(eq(PlatformUser.class), anyInt())).thenReturn(platformUserFixture);
            platformUserService.createDriver(
                    1, createDriverDTOFixture, authentication(platformUserFixture.getEmail(), STAGED));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(driverRepositoryMock, times(0)).save(any(Driver.class));
    }

    @Test
    public void delete_Driver_By_Id_As_ACTIVE_Returns_Void()
    {
        delete_Driver_By_Id(ACTIVE, 1);
    }

    @Test
    public void delete_Driver_By_Id_As_STAGED_Throws_AuthenticationException()
    {
        delete_Driver_By_Id(STAGED, 0);
    }

    @Test
    public void get_Passenger_By_Id_As_ACTIVE_Returns_GrabPassengerDTO()
    {
        when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.of(passengerFixture));
        when(modelMapperMock.map(passengerFixture, GrabPassengerDTO.class)).thenReturn(grabPassengerDTOFixture);
        final GrabPassengerDTO grabPassengerDTO = platformUserService.getPassengerById(
                anyInt(), authentication(passengerFixture.getPlatformUser().getEmail(), ACTIVE));

        assertThat(grabPassengerDTO.getId()).isEqualTo(grabPassengerDTOFixture.getId());
        assertThat(grabPassengerDTO.getPlatformUser()).isEqualTo(grabPassengerDTOFixture.getPlatformUser());
        verify(passengerRepositoryMock).findById(anyInt());
    }

    @Test
    public void get_Passenger_By_Id_As_STAGED_Throws_AuthenticationException()
    {
        try {
            when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.of(passengerFixture));
            platformUserService.getPassengerById(
                    anyInt(), authentication(passengerFixture.getPlatformUser().getEmail(), STAGED));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(passengerRepositoryMock).findById(anyInt());
    }

    @Test
    public void create_Passenger_As_ACTIVE_Returns_GrabPassengerDTO()
    {
        when(entityManagerMock.getReference(eq(PlatformUser.class), anyInt()))
                .thenReturn(platformUserFixture);

        platformUserService.createPassenger(1, authentication(
                platformUserFixture.getEmail(), ACTIVE));
        verify(passengerRepositoryMock).save(any(Passenger.class));
    }

    @Test
    public void create_Passenger_As_STAGED_Throws_AuthenticationException()
    {
        try {
            when(entityManagerMock.getReference(eq(PlatformUser.class), anyInt()))
                    .thenReturn(platformUserFixture);

            platformUserService.createPassenger(1, authentication(
                    platformUserFixture.getEmail(), STAGED));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(passengerRepositoryMock, times(0)).save(any(Passenger.class));
    }

    @Test
    public void delete_Passenger_By_Id_As_ACTIVE_Returns_Void()
    {
        delete_Passenger_By_Id(ACTIVE, 1);
    }

    @Test
    public void delete_Passenger_By_Id_As_STAGED_Throws_AuthenticationException()
    {
        delete_Passenger_By_Id(STAGED, 0);
    }

    private void get_PlatformUser_By_Id_Returns_GrabPlatformUserDTO(final String userStatus)
    {
        when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
        when(modelMapperMock.map(platformUserFixture, GrabPlatformUserDTO.class))
                .thenReturn(grabPlatformUserDTOFixture);
        final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService
                .getPlatformUserById(anyInt(), authentication(platformUserFixture.getEmail(), userStatus));

        assertThat(grabPlatformUserDTO.getId()).isEqualTo(grabPlatformUserDTOFixture.getId());
        assertThat(grabPlatformUserDTO.getCreatedDate()).isEqualTo(grabPlatformUserDTOFixture.getCreatedDate());
        assertThat(grabPlatformUserDTO.getDob()).isEqualTo(grabPlatformUserDTOFixture.getDob());
        assertThat(grabPlatformUserDTO.getEmail()).isEqualTo(grabPlatformUserDTOFixture.getEmail());
        assertThat(grabPlatformUserDTO.getFirstName()).isEqualTo(grabPlatformUserDTOFixture.getFirstName());
        assertThat(grabPlatformUserDTO.getLastName()).isEqualTo(grabPlatformUserDTOFixture.getLastName());
        assertThat(grabPlatformUserDTO.getPhoneNumber()).isEqualTo(grabPlatformUserDTOFixture.getPhoneNumber());
        verify(platformUserRepositoryMock).findById(anyInt());
    }

    private void get_PlatformUser_By_Id_Throws_AuthenticationException(final String userStatus)
    {
        try {
            when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
            platformUserService.getPlatformUserById(
                    anyInt(), authentication(platformUserFixture.getEmail(), userStatus));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(platformUserRepositoryMock).findById(anyInt());
    }

    private void delete_PlatformUser_By_Id(final String userStatus, final int wantedNumberOfInvocations)
    {
        try {
            when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
            platformUserService.deletePlatformUserById(
                    anyInt(), authentication(platformUserFixture.getEmail(), userStatus));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(platformUserRepositoryMock, times(wantedNumberOfInvocations)).deleteById(anyInt());
    }

    private void delete_Driver_By_Id(final String userStatus, final int wantedNumberOfInvocations)
    {
        try {
            when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
            platformUserService.deleteDriverById(anyInt(), authentication(
                    driverFixture.getPlatformUser().getEmail(), userStatus));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(driverRepositoryMock, times(wantedNumberOfInvocations)).deleteById(anyInt());
    }

    private void delete_Passenger_By_Id(final String userStatus, final int wantedNumberOfInvocations)
    {
        try {
            when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.of(passengerFixture));
            platformUserService.deletePassengerById(anyInt(), authentication(
                    passengerFixture.getPlatformUser().getEmail(), userStatus));
        } catch (final Exception ex) {
            assertEquals(NO_PERMISSIONS, ex.getMessage());
        }
        verify(passengerRepositoryMock, times(wantedNumberOfInvocations)).deleteById(anyInt());
    }

    private Authentication authentication(final String username, final String userStatus)
    {
        final User user = new User(username, "foo", List.of(new SimpleGrantedAuthority(userStatus)));
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
