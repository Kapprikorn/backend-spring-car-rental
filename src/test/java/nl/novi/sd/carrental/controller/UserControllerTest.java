package nl.novi.sd.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.sd.carrental.dto.UserDto;
import nl.novi.sd.carrental.model.User;
import nl.novi.sd.carrental.model.UserRole;
import nl.novi.sd.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    // API endpoints
    private static final String USERS_ENDPOINT = "/users";
    private static final String USER_BY_ID_ENDPOINT = "/users/1";

    // Test data IDs
    private static final Long USER_ID = 1L;
    private static final Long ADMIN_ID = 2L;

    // Test user data
    private static final String TEST_USER_NAME = "Test User";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_USER_PASSWORD = "Password1@";
    private static final String TEST_USER_EMAIL = "test@example.com";
    private static final String TEST_USER_ROLE = "USER";

    // Test admin data
    private static final String TEST_ADMIN_NAME = "Test Admin";
    private static final String TEST_ADMIN_USERNAME = "testadmin";
    private static final String TEST_ADMIN_EMAIL = "admin@example.com";
    private static final String TEST_ADMIN_ROLE = "ADMIN";

    // Updated user data
    private static final String UPDATED_USER_NAME = "Updated User";
    private static final String UPDATED_USERNAME = "updateduser";
    private static final String UPDATED_USER_PASSWORD = "UpdatedPassword1@";
    private static final String UPDATED_USER_EMAIL = "updated@example.com";

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private User testUser;
    private User testAdmin;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Create test user
        testUser = new User();
        testUser.setId(USER_ID);
        testUser.setName(TEST_USER_NAME);
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_USER_PASSWORD);
        testUser.setEmail(TEST_USER_EMAIL);
        testUser.setRole(UserRole.USER);

        // Create test admin
        testAdmin = new User();
        testAdmin.setId(ADMIN_ID);
        testAdmin.setName(TEST_ADMIN_NAME);
        testAdmin.setUsername(TEST_ADMIN_USERNAME);
        testAdmin.setPassword(TEST_USER_PASSWORD); // Using same password for simplicity
        testAdmin.setEmail(TEST_ADMIN_EMAIL);
        testAdmin.setRole(UserRole.ADMIN);

        // Create test user DTO
        testUserDto = new UserDto();
        testUserDto.setId(USER_ID);
        testUserDto.setName(TEST_USER_NAME);
        testUserDto.setUsername(TEST_USERNAME);
        testUserDto.setPassword(TEST_USER_PASSWORD);
        testUserDto.setEmail(TEST_USER_EMAIL);
        testUserDto.setRole(TEST_USER_ROLE);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        // Arrange
        List<User> users = List.of(testUser, testAdmin);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get(USERS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].name").value(TEST_USER_NAME))
                .andExpect(jsonPath("$[0].username").value(TEST_USERNAME))
                .andExpect(jsonPath("$[0].email").value(TEST_USER_EMAIL))
                .andExpect(jsonPath("$[0].role").value(TEST_USER_ROLE))
                .andExpect(jsonPath("$[1].id").value(ADMIN_ID))
                .andExpect(jsonPath("$[1].name").value(TEST_ADMIN_NAME))
                .andExpect(jsonPath("$[1].username").value(TEST_ADMIN_USERNAME))
                .andExpect(jsonPath("$[1].email").value(TEST_ADMIN_EMAIL))
                .andExpect(jsonPath("$[1].role").value(TEST_ADMIN_ROLE));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // Arrange
        when(userService.getUser(USER_ID)).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(get(USER_BY_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(TEST_USER_NAME))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
                .andExpect(jsonPath("$.role").value(TEST_USER_ROLE));

        verify(userService, times(1)).getUser(USER_ID);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Arrange
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(TEST_USER_NAME))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
                .andExpect(jsonPath("$.role").value(TEST_USER_ROLE));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(USER_ID);
        updatedUserDto.setName(UPDATED_USER_NAME);
        updatedUserDto.setUsername(UPDATED_USERNAME);
        updatedUserDto.setPassword(UPDATED_USER_PASSWORD);
        updatedUserDto.setEmail(UPDATED_USER_EMAIL);
        updatedUserDto.setRole(TEST_ADMIN_ROLE);

        User updatedUser = new User();
        updatedUser.setId(USER_ID);
        updatedUser.setName(UPDATED_USER_NAME);
        updatedUser.setUsername(UPDATED_USERNAME);
        updatedUser.setPassword(UPDATED_USER_PASSWORD);
        updatedUser.setEmail(UPDATED_USER_EMAIL);
        updatedUser.setRole(UserRole.ADMIN);

        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(put(USER_BY_ID_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(UPDATED_USER_NAME))
                .andExpect(jsonPath("$.username").value(UPDATED_USERNAME))
                .andExpect(jsonPath("$.email").value(UPDATED_USER_EMAIL))
                .andExpect(jsonPath("$.role").value(TEST_ADMIN_ROLE));

        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(USER_ID);

        // Act & Assert
        mockMvc.perform(delete(USER_BY_ID_ENDPOINT))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(USER_ID);
    }
}
