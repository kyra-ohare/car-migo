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
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.dto.response.PassengerResponse;
import com.unosquare.carmigo.repository.PassengerRepository;
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
public class PassengerServiceTest {

  @Mock private PassengerRepository passengerRepositoryMock;
  @Mock private ModelMapper modelMapperMock;
  @Mock private EntityManager entityManagerMock;
  @InjectMocks private PassengerService passengerService;

  @Fixture private PlatformUser platformUserFixture;
  @Fixture private Passenger passengerFixture;
  @Fixture private PassengerResponse passengerResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @Test
  public void get_Passenger_By_Id_Returns_PassengerResponse() {
    when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.of(passengerFixture));
    when(modelMapperMock.map(passengerFixture, PassengerResponse.class)).thenReturn(passengerResponseFixture);
    final var response = passengerService.getPassengerById(1);

    assertThat(response.getId()).isEqualTo(passengerResponseFixture.getId());
    assertThat(response.getPlatformUser()).isEqualTo(passengerResponseFixture.getPlatformUser());
    verify(passengerRepositoryMock).findById(anyInt());
  }

  @Test
  public void get_Passenger_By_Id_Throws_EntityNotFoundException() {
    when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class,
      () -> passengerService.getPassengerById(anyInt()),
      "EntityNotFoundException is expected.");
    verify(passengerRepositoryMock).findById(anyInt());
  }

  @Test
  public void create_Passenger_Returns_PassengerResponse() {
    when(entityManagerMock.getReference(eq(PlatformUser.class), anyInt())).thenReturn(platformUserFixture);
    passengerService.createPassengerById(anyInt());
    verify(passengerRepositoryMock).save(any(Passenger.class));
  }

  @Test
  public void create_Passenger_Throws_EntityNotFoundException() {
    when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    doThrow(EntityNotFoundException.class).when(passengerRepositoryMock).save(any(Passenger.class));
    assertThrows(EntityNotFoundException.class,
      () -> passengerService.createPassengerById(anyInt()),
      "EntityNotFoundException is expected.");
    verify(passengerRepositoryMock).save(any(Passenger.class));
  }

  @Test
  public void create_Passenger_Throws_EntityExistsException() {
    when(passengerRepositoryMock.findById(anyInt())).thenReturn(Optional.of(passengerFixture));
    assertThrows(EntityExistsException.class,
      () -> passengerService.createPassengerById(anyInt()),
      "EntityExistsException is expected");
    verify(passengerRepositoryMock, times(0)).save(any(Passenger.class));
  }

  @Test
  public void delete_Passenger_By_Id_Returns_Void() {
    passengerService.deletePassengerById(anyInt());
    verify(passengerRepositoryMock).deleteById(anyInt());
  }
}
