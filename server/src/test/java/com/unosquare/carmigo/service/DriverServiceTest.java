package com.unosquare.carmigo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.model.request.DriverRequest;
import com.unosquare.carmigo.model.response.DriverResponse;
import com.unosquare.carmigo.repository.DriverRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

  @Mock private DriverRepository driverRepositoryMock;
  @Mock private ModelMapper modelMapperMock;
  @Mock private EntityManager entityManagerMock;
  @InjectMocks private DriverService driverService;

  @Fixture private PlatformUser platformUserFixture;
  @Fixture private Driver driverFixture;
  @Fixture private DriverRequest driverRequestFixture;
  @Fixture private DriverResponse driverResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @Test
  public void get_Driver_By_Id_Returns_DriverResponse() {
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
    when(modelMapperMock.map(driverFixture, DriverResponse.class)).thenReturn(driverResponseFixture);
    final var response = driverService.getDriverById(1);

    assertThat(response.getId()).isEqualTo(driverResponseFixture.getId());
    assertThat(response.getLicenseNumber()).isEqualTo(driverResponseFixture.getLicenseNumber());
    assertThat(response.getPlatformUser()).isEqualTo(driverResponseFixture.getPlatformUser());
    verify(driverRepositoryMock).findById(anyInt());
  }

  @Test
  public void get_Driver_By_Id_Throws_EntityNotFoundException() {
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class,
      () -> driverService.getDriverById(anyInt()),
      "EntityNotFoundException is expected.");
    verify(driverRepositoryMock).findById(anyInt());
  }

  @Test
  public void create_Driver_Returns_DriverResponse() {
    when(modelMapperMock.map(driverRequestFixture, Driver.class)).thenReturn(driverFixture);
    when(entityManagerMock.getReference(eq(PlatformUser.class), anyInt())).thenReturn(platformUserFixture);
    when(driverRepositoryMock.save(driverFixture)).thenReturn(driverFixture);
    when(modelMapperMock.map(driverFixture, DriverResponse.class)).thenReturn(driverResponseFixture);
    final var response = driverService.createDriverById(1, driverRequestFixture);

    assertThat(response.getLicenseNumber()).isEqualTo(driverResponseFixture.getLicenseNumber());
    assertThat(response.getPlatformUser()).isEqualTo(driverResponseFixture.getPlatformUser());
    verify(driverRepositoryMock).save(any(Driver.class));
  }

  @Test
  public void create_Driver_Throws_EntityNotFoundException() {
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    when(modelMapperMock.map(driverRequestFixture, Driver.class)).thenReturn(driverFixture);
    doThrow(EntityNotFoundException.class).when(driverRepositoryMock).save(driverFixture);
    assertThrows(EntityNotFoundException.class,
      () -> driverService.createDriverById(anyInt(), driverRequestFixture),
      "EntityNotFoundException is expected.");
    verify(driverRepositoryMock).save(any(Driver.class));
  }

  @Test
  public void create_Driver_Throws_EntityExistsException() {
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
    assertThrows(EntityExistsException.class,
      () -> driverService.createDriverById(anyInt(), driverRequestFixture),
      "EntityExistsException is expected");
    verify(driverRepositoryMock, times(0)).save(any(Driver.class));
  }

  @Test
  public void delete_Driver_By_Id_Returns_Void() {
    driverService.deleteDriverById(anyInt());
    verify(driverRepositoryMock).deleteById(anyInt());
  }
}
