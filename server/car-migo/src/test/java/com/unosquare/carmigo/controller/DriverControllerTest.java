package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.ResourceUtility.convertObjectToJsonBytes;
import static com.unosquare.carmigo.util.ResourceUtility.getObjectResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.dto.request.DriverRequest;
import com.unosquare.carmigo.dto.response.DriverResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.DriverService;
import com.unosquare.carmigo.util.AuthenticationBeansTestCase;
import jakarta.persistence.EntityExistsException;
import java.util.LinkedHashMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = DriverController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
public class DriverControllerTest {

  private static final String API_LEADING = "/v1/drivers";
  private static final String ERROR_MSG = "%s do not match";

  @Autowired private MockMvc mockMvc;
  @MockBean private DriverService driverServiceMock;
  @MockBean private AppUser appUserMock;
  @Fixture private AppUser.CurrentAppUser currentAppUserFixture;
  @Fixture private DriverRequest driverRequestFixture;
  @Fixture private DriverResponse driverResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);

    when(appUserMock.get()).thenReturn(currentAppUserFixture);
  }

  @SneakyThrows
  @Test
  void createDriverTest() {
    when(driverServiceMock.createDriverById(anyInt(), any(DriverRequest.class))).thenReturn(driverResponseFixture);
    final var response = mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(driverRequestFixture)))
        .andExpect(status().isCreated()).andReturn();

    callAssertions(response);
    verify(driverServiceMock).createDriverById(anyInt(), any(DriverRequest.class));
  }

  @SneakyThrows
  @Test
  void createDriverAgainTest() {
    doThrow(EntityExistsException.class).when(driverServiceMock).createDriverById(anyInt(), any(DriverRequest.class));
    mockMvc.perform(post(API_LEADING + "/create")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(driverRequestFixture)))
        .andExpect(status().isConflict()).andReturn();

    verify(driverServiceMock).createDriverById(anyInt(), any(DriverRequest.class));
  }

  @SneakyThrows
  @Test
  void createDriverByIdTest() {
    when(driverServiceMock.createDriverById(anyInt(), any(DriverRequest.class))).thenReturn(driverResponseFixture);
    final var response = mockMvc.perform(post(API_LEADING + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(driverRequestFixture)))
        .andExpect(status().isCreated()).andReturn();

    callAssertions(response);
    verify(driverServiceMock).createDriverById(anyInt(), any(DriverRequest.class));
  }


  @SneakyThrows
  @Test
  void createDriverByIdAgainTest() {
    doThrow(EntityExistsException.class).when(driverServiceMock).createDriverById(anyInt(), any(DriverRequest.class));
    mockMvc.perform(post(API_LEADING + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(convertObjectToJsonBytes(driverRequestFixture)))
        .andExpect(status().isConflict()).andReturn();

    verify(driverServiceMock).createDriverById(anyInt(), any(DriverRequest.class));
  }

  @SneakyThrows
  @Test
  void getCurrentDriverProfileTest() {
    when(driverServiceMock.getDriverById(anyInt())).thenReturn(driverResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/profile")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    callAssertions(response);
    verify(driverServiceMock).getDriverById(anyInt());
  }

  @SneakyThrows
  @Test
  void getDriverByIdTest() {
    when(driverServiceMock.getDriverById(anyInt())).thenReturn(driverResponseFixture);
    final var response = mockMvc.perform(get(API_LEADING + "/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    callAssertions(response);
    verify(driverServiceMock).getDriverById(anyInt());
  }

  @SneakyThrows
  @Test
  void deleteCurrentDriverTest() {
    doNothing().when(driverServiceMock).deleteDriverById(anyInt());
    mockMvc.perform(delete(API_LEADING))
        .andExpect(status().isNoContent()).andReturn();

    verify(driverServiceMock).deleteDriverById(anyInt());
  }

  @SneakyThrows
  @SuppressWarnings("unchecked cast")
  private void callAssertions(final MvcResult response) {
    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("id"), driverResponseFixture.getId(), ERROR_MSG.formatted("Driver ids"));
    assertEquals(content.get("licenseNumber"), driverResponseFixture.getLicenseNumber(),
        ERROR_MSG.formatted("License numbers"));

    final LinkedHashMap<String, Object> platformUserResponse =
        (LinkedHashMap<String, Object>) content.get("platformUser");
    assertEquals(platformUserResponse.get("id"), driverResponseFixture.getPlatformUser().getId(),
        ERROR_MSG.formatted("Platform User Ids"));
    assertEquals(platformUserResponse.get("createdDate"),
        driverResponseFixture.getPlatformUser().getCreatedDate().toString(), ERROR_MSG.formatted("Created dates"));
    assertEquals(platformUserResponse.get("firstName"), driverResponseFixture.getPlatformUser().getFirstName(),
        ERROR_MSG.formatted("First Names"));
    assertEquals(platformUserResponse.get("lastName"), driverResponseFixture.getPlatformUser().getLastName(),
        ERROR_MSG.formatted("Last names"));
    assertEquals(platformUserResponse.get("dob"), driverResponseFixture.getPlatformUser().getDob().toString(),
        ERROR_MSG.formatted("Dobs"));
    assertEquals(platformUserResponse.get("email"), driverResponseFixture.getPlatformUser().getEmail(),
        ERROR_MSG.formatted("emails"));
    assertEquals(platformUserResponse.get("phoneNumber"), driverResponseFixture.getPlatformUser().getPhoneNumber(),
        ERROR_MSG.formatted("Phone Numbers"));

    final LinkedHashMap<String, Object> userAccessStatusResponse =
        (LinkedHashMap<String, Object>) platformUserResponse.get("userAccessStatus");
    assertEquals(userAccessStatusResponse.get("id"),
        driverResponseFixture.getPlatformUser().getUserAccessStatus().getId(),
        ERROR_MSG.formatted("User access status ids"));
    assertEquals(userAccessStatusResponse.get("status"),
        driverResponseFixture.getPlatformUser().getUserAccessStatus().getStatus(),
        ERROR_MSG.formatted("User access statuses"));
  }
}
