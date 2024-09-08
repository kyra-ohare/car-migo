package com.unosquare.carmigo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import com.unosquare.carmigo.dto.PlatformUserDto;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.dto.request.PlatformUserRequest;
import com.unosquare.carmigo.dto.response.PlatformUserResponse;
import com.unosquare.carmigo.repository.DriverRepository;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import com.unosquare.carmigo.util.PatchUtility;
import com.unosquare.carmigo.util.ResourceUtility;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class PlatformUserServiceTest {

  private static final String PATCH_PLATFORM_USER_VALID_JSON =
      ResourceUtility.generateStringFromResource("jsonAssets/PatchPlatformUserValid.json");

  @Mock private PlatformUserRepository platformUserRepositoryMock;
  @Mock private DriverRepository driverRepositoryMock;
  @Mock private PassengerRepository passengerRepositoryMock;
  @Mock private ModelMapper modelMapperMock;
  @Mock private ObjectMapper objectMapperMock;
  @Mock private EntityManager entityManagerMock;
  @Mock private BCryptPasswordEncoder bCryptPasswordEncoderMock;
  @InjectMocks private PlatformUserService platformUserService;

  @Fixture private PlatformUser platformUserFixture;
  @Fixture private Driver driverFixture;
  @Fixture private Passenger passengerFixture;
  @Fixture private PlatformUserDto platformUserDtoFixture;
  @Fixture private PlatformUserRequest platformUserRequestFixture;
  @Fixture private PlatformUserResponse platformUserResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @Test
  public void confirm_Email_Returns_Void() {
    when(platformUserRepositoryMock.findPlatformUserByEmail(anyString())).thenReturn(Optional.of(platformUserFixture));
    platformUserService.confirmEmail(anyString());
    verify(platformUserRepositoryMock).findPlatformUserByEmail(anyString());
    verify(platformUserRepositoryMock).save(any(PlatformUser.class));
  }

  @Test
  public void confirm_Email_Throws_IllegalStateException() {
    when(platformUserRepositoryMock.findPlatformUserByEmail(anyString())).thenReturn(Optional.of(platformUserFixture));
    platformUserFixture.getUserAccessStatus().setStatus("ACTIVE");
    assertThrows(IllegalStateException.class,
        () -> platformUserService.confirmEmail(anyString()),
        "IllegalStateException is expected.");
    verify(platformUserRepositoryMock).findPlatformUserByEmail(anyString());
    verify(platformUserRepositoryMock, times(0)).save(any(PlatformUser.class));
  }

  @Test
  public void confirm_Email_Throws_EntityNotFoundException() {
    when(platformUserRepositoryMock.findPlatformUserByEmail(anyString())).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class,
        () -> platformUserService.confirmEmail(anyString()),
        "EntityNotFoundException is expected.");
    verify(platformUserRepositoryMock).findPlatformUserByEmail(anyString());
    verify(platformUserRepositoryMock, times(0)).save(any(PlatformUser.class));
  }

  @Test
  public void get_PlatformUser_By_Id_Returns_PlatformUserResponse() {
    when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
    when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.of(passengerFixture));
    when(modelMapperMock.map(platformUserFixture,  PlatformUserResponse.class)).thenReturn(platformUserResponseFixture);
    final var response = platformUserService.cacheableGetPlatformUserById(anyInt());

    assertThat(response.getId()).isEqualTo(platformUserResponseFixture.getId());
    assertThat(response.getCreatedDate()).isEqualTo(platformUserResponseFixture.getCreatedDate());
    assertThat(response.getDob()).isEqualTo(platformUserResponseFixture.getDob());
    assertThat(response.getEmail()).isEqualTo(platformUserResponseFixture.getEmail());
    assertThat(response.getFirstName()).isEqualTo(platformUserResponseFixture.getFirstName());
    assertThat(response.getLastName()).isEqualTo(platformUserResponseFixture.getLastName());
    assertThat(response.getPhoneNumber()).isEqualTo(platformUserResponseFixture.getPhoneNumber());
    verify(platformUserRepositoryMock).findById(anyInt());
  }

  @Test
  public void get_PlatformUser_By_Id_Throws_EntityNotFoundException() {
    when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class,
        () -> platformUserService.cacheableGetPlatformUserById(anyInt()),
        "EntityNotFoundException is expected.");
    verify(platformUserRepositoryMock).findById(anyInt());
  }

  @Test
  public void create_PlatformUser_Returns_PlatformUserResponse() {
    final PlatformUser spyPlatformUser = spy(new PlatformUser());
      spyPlatformUser.setCreatedDate(any(Instant.class));
      spyPlatformUser.setFirstName(anyString());
      spyPlatformUser.setLastName(anyString());
      spyPlatformUser.setEmail(anyString());
      spyPlatformUser.setPassword(bCryptPasswordEncoderMock.encode(anyString()));
      spyPlatformUser.setUserAccessStatus(entityManagerMock.getReference(eq(UserAccessStatus.class), anyInt()));
    when(modelMapperMock.map(platformUserRequestFixture, PlatformUser.class)).thenReturn(spyPlatformUser);
    when(platformUserRepositoryMock.save(spyPlatformUser)).thenReturn(platformUserFixture);
    when(modelMapperMock.map(platformUserFixture, PlatformUserResponse.class)).thenReturn(platformUserResponseFixture);
    final var response = platformUserService.createPlatformUser(platformUserRequestFixture);

    assertThat(response.getCreatedDate()).isEqualTo(platformUserResponseFixture.getCreatedDate());
    assertThat(response.getFirstName()).isEqualTo(platformUserResponseFixture.getFirstName());
    assertThat(response.getLastName()).isEqualTo(platformUserResponseFixture.getLastName());
    assertThat(response.getEmail()).isEqualTo(platformUserResponseFixture.getEmail());
    verify(platformUserRepositoryMock).save(any(PlatformUser.class));
  }

  @Test
  public void create_PlatformUser_Throws_EntityExistsException() {
    when(modelMapperMock.map(platformUserRequestFixture, PlatformUser.class)).thenReturn(platformUserFixture);
    doThrow(EntityExistsException.class).when(platformUserRepositoryMock).save(platformUserFixture);
    assertThrows(EntityExistsException.class,
        () -> platformUserService.createPlatformUser(platformUserRequestFixture),
        "EntityExistsException is expected.");
    verify(platformUserRepositoryMock).save(any(PlatformUser.class));
  }

  @Test
  public void patch_PlatformUser_Returns_PlatformUserResponse() throws Exception {
    when(platformUserRepositoryMock.findById(anyInt())).thenReturn(Optional.of(platformUserFixture));
    when(modelMapperMock.map(platformUserFixture, PlatformUserDto.class)).thenReturn(platformUserDtoFixture);
    final JsonPatch patch = PatchUtility.jsonPatch(PATCH_PLATFORM_USER_VALID_JSON);
    final JsonNode platformUserNode = PatchUtility.jsonNode(platformUserFixture, patch);
    when(objectMapperMock.convertValue(platformUserDtoFixture, JsonNode.class)).thenReturn(platformUserNode);
    when(objectMapperMock.treeToValue(platformUserNode, PlatformUser.class)).thenReturn(platformUserFixture);
    when(platformUserRepositoryMock.save(platformUserFixture)).thenReturn(platformUserFixture);
    when(modelMapperMock.map(platformUserFixture, PlatformUserResponse.class)).thenReturn(platformUserResponseFixture);
    final var response = platformUserService.patchPlatformUserById(platformUserFixture.getId(), patch);

    assertThat(response.getFirstName()).isEqualTo(platformUserResponseFixture.getFirstName());
    assertThat(response.getLastName()).isEqualTo(platformUserResponseFixture.getLastName());
    assertThat(response.getDob()).isEqualTo(platformUserResponseFixture.getDob());
    assertThat(response.getCreatedDate()).isEqualTo(platformUserResponseFixture.getCreatedDate());
    verify(platformUserRepositoryMock).findById(anyInt());
    verify(platformUserRepositoryMock).save(any(PlatformUser.class));
  }

  @Test
  public void delete_PlatformUser_Returns_Void() {
    platformUserService.deletePlatformUserById(anyInt());
    verify(platformUserRepositoryMock).deleteById(anyInt());
  }
}
