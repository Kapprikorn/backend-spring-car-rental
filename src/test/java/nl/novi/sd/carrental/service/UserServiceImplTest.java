package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.exception.UsernameAlreadyExistsException;
import nl.novi.sd.carrental.model.User;
import nl.novi.sd.carrental.model.UserRole;
import nl.novi.sd.carrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private User testAdmin;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setUsername("testuser");
        testUser.setPassword("Password1@");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.USER);

        // Create test admin
        testAdmin = new User();
        testAdmin.setId(2L);
        testAdmin.setName("Test Admin");
        testAdmin.setUsername("testadmin");
        testAdmin.setPassword("Password1@");
        testAdmin.setEmail("admin@example.com");
        testAdmin.setRole(UserRole.ADMIN);
    }

    @Test
    void getAllUsers_WithUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = List.of(testUser, testAdmin);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        assertEquals(testAdmin.getId(), result.get(1).getId());
    }

    @Test
    void getAllUsers_WithNoUsers_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getAllUsers()
        );
        assertEquals("No users found", exception.getMessage());
    }

    @Test
    void getUser_WithValidId_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void getUser_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUser(999L)
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getUserByUsername_WithValidUsername_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void getUserByUsername_WithInvalidUsername_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserByUsername("nonexistent")
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void createUser_WithNewUsername_ShouldReturnSavedUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.createUser(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void createUser_WithExistingUsername_ShouldThrowUsernameAlreadyExistsException() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        UsernameAlreadyExistsException exception = assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.createUser(testUser)
        );
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void updateUser_WithValidId_ShouldReturnUpdatedUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("UpdatedPassword1@");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setRole(UserRole.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUser(updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getPassword(), result.getPassword());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getRole(), result.getRole());
    }

    @Test
    void updateUser_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        User nonExistentUser = new User();
        nonExistentUser.setId(999L);
        nonExistentUser.setName("Non-existent User");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(nonExistentUser)
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUser_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}