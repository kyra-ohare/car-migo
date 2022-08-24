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
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.DriverRepository;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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
  @Fixture private CreateDriverDTO createDriverDTOFixture;
  @Fixture private GrabDriverDTO grabDriverDTOFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @Test
  public void get_Driver_By_Id_Returns_GrabDriverDTO() {
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
    when(modelMapperMock.map(driverFixture, GrabDriverDTO.class)).thenReturn(grabDriverDTOFixture);
    final GrabDriverDTO grabDriverDTO = driverService.getDriverById(1);

    assertThat(grabDriverDTO.getId()).isEqualTo(grabDriverDTOFixture.getId());
    assertThat(grabDriverDTO.getLicenseNumber()).isEqualTo(grabDriverDTOFixture.getLicenseNumber());
    assertThat(grabDriverDTO.getPlatformUser()).isEqualTo(grabDriverDTOFixture.getPlatformUser());
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
  public void create_Driver_Returns_GrabDriverDTO() {
    when(modelMapperMock.map(createDriverDTOFixture, Driver.class)).thenReturn(driverFixture);
    when(entityManagerMock.getReference(eq(PlatformUser.class), anyInt())).thenReturn(platformUserFixture);
    when(driverRepositoryMock.save(driverFixture)).thenReturn(driverFixture);
    when(modelMapperMock.map(driverFixture, GrabDriverDTO.class)).thenReturn(grabDriverDTOFixture);
    final GrabDriverDTO grabDriverDTO = driverService.createDriverById(1, createDriverDTOFixture);

    assertThat(grabDriverDTO.getLicenseNumber()).isEqualTo(grabDriverDTOFixture.getLicenseNumber());
    assertThat(grabDriverDTO.getPlatformUser()).isEqualTo(grabDriverDTOFixture.getPlatformUser());
    verify(driverRepositoryMock).save(any(Driver.class));
  }

  @Test
  public void create_Driver_Throws_EntityNotFoundException() {
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    when(modelMapperMock.map(createDriverDTOFixture, Driver.class)).thenReturn(driverFixture);
    doThrow(EntityNotFoundException.class).when(driverRepositoryMock).save(driverFixture);
    assertThrows(EntityNotFoundException.class,
        () -> driverService.createDriverById(anyInt(), createDriverDTOFixture),
        "EntityNotFoundException is expected.");
    verify(driverRepositoryMock).save(any(Driver.class));
  }

  @Test
  public void create_Driver_Throws_EntityExistsException() {
    when(driverRepositoryMock.findById(anyInt())).thenReturn(Optional.of(driverFixture));
    assertThrows(EntityExistsException.class,
        () -> driverService.createDriverById(anyInt(), createDriverDTOFixture),
        "EntityExistsException is expected");
    verify(driverRepositoryMock, times(0)).save(any(Driver.class));
  }

  @Test
  public void delete_Driver_By_Id_Returns_Void() {
    driverService.deleteDriverById(anyInt());
    verify(driverRepositoryMock).deleteById(anyInt());
  }

  @Test
  public void delete_Driver_By_Id_EntityNotFoundException() {
    doThrow(EntityNotFoundException.class).when(driverRepositoryMock).deleteById(anyInt());
    assertThrows(EntityNotFoundException.class,
        () -> driverService.deleteDriverById(anyInt()),
        "EntityNotFoundException is expected");
    verify(driverRepositoryMock).deleteById(anyInt());
  }
}
