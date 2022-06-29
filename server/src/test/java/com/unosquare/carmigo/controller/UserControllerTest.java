package com.unosquare.carmigo.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.exception.UnauthorizedException;
import com.unosquare.carmigo.service.UserService;
import com.unosquare.carmigo.util.ResourceUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  private static final String API_LEADING = "/v1/users/";
  private static final String POST_AUTHENTICATION_VALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostAuthenticationValid.json");
  private static final String POST_AUTHENTICATION_INVALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostAuthenticationInvalid.json");
  private static final String POST_PLATFORM_USER_VALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostPlatformUserValid.json");
  private static final String POST_PLATFORM_USER_INVALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostPlatformUserInvalid.json");
  private static final String PATCH_PLATFORM_USER_VALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PatchPlatformUserValid.json");
  private static final String PATCH_PLATFORM_USER_INVALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PatchPlatformUserInvalid.json");
  private static final String POST_DRIVER_VALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostDriverValid.json");
  private static final String POST_DRIVER_INVALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostDriverInvalid.json");

  private MockMvc mockMvc;

  @Mock private ModelMapper modelMapperMock;
  @Mock private UserService userServiceMock;
  @Mock private UserControllerHelper userControllerHelperMock;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);

    mockMvc = MockMvcBuilders.standaloneSetup(
            new UserController(modelMapperMock, userServiceMock, userControllerHelperMock))
        .build();
  }

  @Test
  public void post_Create_Authentication_Token_Returns_HttpStatus_Created() throws Exception {
    mockMvc.perform(post(API_LEADING + "authenticate")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_AUTHENTICATION_VALID_JSON))
        .andExpect(status().isCreated());
    verify(userServiceMock).createAuthenticationToken(any());
  }

  @Test
  public void post_Create_Authentication_Token_Returns_HttpStatus_BadRequest() throws Exception {
    mockMvc.perform(post(API_LEADING + "authenticate")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_AUTHENTICATION_INVALID_JSON))
        .andExpect(status().isBadRequest());
    verify(userServiceMock, times(0)).createAuthenticationToken(any());
  }

  @Test
  public void get_Profile_Returns_HttpStatus_Ok() throws Exception {
    mockMvc.perform(get(API_LEADING + "profile").
            contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
    verify(userControllerHelperMock).getPlatformUserById(anyInt());
  }

  @Test
  public void get_Profile_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      when(userControllerHelperMock.getPlatformUserById(anyInt())).thenThrow(UnauthorizedException.class);
      mockMvc.perform(get(API_LEADING + "profile").contentType(MediaType.APPLICATION_JSON_VALUE));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).getPlatformUserById(anyInt());
  }

  @Test
  public void post_PlatformUser_Returns_HttpStatus_Created() throws Exception {
    mockMvc.perform(post(API_LEADING)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_PLATFORM_USER_VALID_JSON))
        .andExpect(status().isCreated());
    verify(userServiceMock).createPlatformUser(any());
  }

  @Test
  public void post_PlatformUser_Returns_HttpStatus_BadRequest() throws Exception {
    mockMvc.perform(post(API_LEADING)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_PLATFORM_USER_INVALID_JSON))
        .andExpect(status().isBadRequest());
    verify(userServiceMock, times(0)).createPlatformUser(any());
  }

  @Test
  public void patch_PlatformUser_Returns_HttpStatus_Accepted() throws Exception {
    mockMvc.perform(patch(API_LEADING)
            .contentType("application/json-patch+json").content(PATCH_PLATFORM_USER_VALID_JSON))
        .andExpect(status().isAccepted());
    verify(userControllerHelperMock).patchPlatformUserById(anyInt(), any(JsonPatch.class));
  }

  @Test
  public void patch_PlatformUser_Returns_HttpStatus_BadRequest() throws Exception {
    mockMvc.perform(patch(API_LEADING)
            .contentType("application/json-patch+json").content(PATCH_PLATFORM_USER_INVALID_JSON))
        .andExpect(status().isBadRequest());
    verify(userControllerHelperMock, times(0)).patchPlatformUserById(anyInt(), any(JsonPatch.class));
  }

  @Test
  public void patch_PlatformUser_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      when(userControllerHelperMock.patchPlatformUserById(anyInt(), any(JsonPatch.class)))
          .thenThrow(UnauthorizedException.class);
      mockMvc.perform(patch(API_LEADING)
          .contentType("application/json-patch+json").content(PATCH_PLATFORM_USER_VALID_JSON));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).patchPlatformUserById(anyInt(), any(JsonPatch.class));
  }

  @Test
  public void delete_PlatformUser_Returns_HttpStatus_No_Content() throws Exception {
    doNothing().when(userControllerHelperMock).deletePlatformUserById(anyInt());
    mockMvc.perform(delete(API_LEADING)).andExpect(status().isNoContent());
    verify(userControllerHelperMock).deletePlatformUserById(anyInt());
  }

  @Test
  public void delete_PlatformUser_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      doThrow(UnauthorizedException.class).when(userControllerHelperMock).deletePlatformUserById(anyInt());
      mockMvc.perform(delete(API_LEADING));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).deletePlatformUserById(anyInt());
  }

  @Test
  public void get_Driver_By_Id_Returns_HttpStatus_Ok() throws Exception {
    mockMvc.perform(get(API_LEADING + "drivers/profile")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
    verify(userControllerHelperMock).getDriverById(anyInt());
  }

  @Test
  public void get_Driver_By_Id_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      when(userControllerHelperMock.getDriverById(anyInt())).thenThrow(UnauthorizedException.class);
      mockMvc.perform(get(API_LEADING + "drivers/profile").contentType(MediaType.APPLICATION_JSON_VALUE));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).getDriverById(anyInt());
  }

  @Test
  public void post_Driver_Returns_HttpStatus_Created() throws Exception {
    mockMvc.perform(post(API_LEADING + "drivers")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_DRIVER_VALID_JSON))
        .andExpect(status().isCreated());
    verify(userControllerHelperMock).createDriverById(anyInt(), any());
  }

  @Test
  public void post_Driver_Returns_HttpStatus_BadRequest() throws Exception {
    mockMvc.perform(post(API_LEADING + "drivers")
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_DRIVER_INVALID_JSON))
        .andExpect(status().isBadRequest());
    verify(userControllerHelperMock, times(0)).createDriverById(anyInt(), any());
  }

  @Test
  public void post_Driver_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      when(userControllerHelperMock.createDriverById(anyInt(), any())).thenThrow(UnauthorizedException.class);
      mockMvc.perform(post(API_LEADING + "drivers")
          .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_DRIVER_VALID_JSON));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).createDriverById(anyInt(), any());
  }

  @Test
  public void delete_Driver_Returns_HttpStatus_No_Content() throws Exception {
    doNothing().when(userControllerHelperMock).deleteDriverById(anyInt());
    mockMvc.perform(delete(API_LEADING + "/drivers")).andExpect(status().isNoContent());
    verify(userControllerHelperMock).deleteDriverById(anyInt());
  }

  @Test
  public void delete_Driver_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      doThrow(UnauthorizedException.class).when(userControllerHelperMock).deleteDriverById(anyInt());
      mockMvc.perform(delete(API_LEADING + "drivers"));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).deleteDriverById(anyInt());
  }

  @Test
  public void get_Passenger_By_Id_Returns_HttpStatus_Ok() throws Exception {
    mockMvc.perform(get(API_LEADING + "passengers/profile")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
    verify(userControllerHelperMock).getPassengerById(anyInt());
  }

  @Test
  public void get_Passenger_By_Id_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      when(userControllerHelperMock.getPassengerById(anyInt())).thenThrow(UnauthorizedException.class);
      mockMvc.perform(get(API_LEADING + "passengers/profile").contentType(MediaType.APPLICATION_JSON_VALUE));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).getPassengerById(anyInt());
  }

  @Test
  public void post_Passenger_Returns_HttpStatus_Created() throws Exception {
    mockMvc.perform(post(API_LEADING + "passengers")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());
    verify(userControllerHelperMock).createPassengerById(anyInt());
  }

  @Test
  public void post_Passenger_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      when(userControllerHelperMock.createPassengerById(anyInt())).thenThrow(UnauthorizedException.class);
      mockMvc.perform(post(API_LEADING + "passengers").contentType(MediaType.APPLICATION_JSON_VALUE));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).createPassengerById(anyInt());
  }

  @Test
  public void delete_Passenger_Returns_HttpStatus_No_Content() throws Exception {
    doNothing().when(userControllerHelperMock).deletePassengerById(anyInt());
    mockMvc.perform(delete(API_LEADING + "/passengers")).andExpect(status().isNoContent());
    verify(userControllerHelperMock).deletePassengerById(anyInt());
  }

  @Test
  public void delete_Passenger_Throws_UnauthorizedException() {
    Exception ex = null;
    try {
      doThrow(UnauthorizedException.class).when(userControllerHelperMock).deletePassengerById(anyInt());
      mockMvc.perform(delete(API_LEADING + "/passengers"));
    } catch (final Exception e) {
      ex = e;
    }
    assert ex != null;
    assertTrue(ex.getCause() instanceof UnauthorizedException);
    verify(userControllerHelperMock).deletePassengerById(anyInt());
  }
}
