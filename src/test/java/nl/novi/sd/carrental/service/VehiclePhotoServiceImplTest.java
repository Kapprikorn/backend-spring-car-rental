package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.VehiclePhoto;
import nl.novi.sd.carrental.repository.VehiclePhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiclePhotoServiceImplTest {

    @Mock
    private VehiclePhotoRepository vehiclePhotoRepository;

    @InjectMocks
    private VehiclePhotoServiceImpl vehiclePhotoService;

    private VehiclePhoto testPhoto;
    private MultipartFile testFile;
    private String testUrl;

    @BeforeEach
    void setUp() {
        // Create test photo
        testPhoto = new VehiclePhoto();
        testPhoto.setId(1L);
        testPhoto.setUrl("http://example.com/photo.jpg");
        testPhoto.setOriginalFileName("photo.jpg");
        testPhoto.setContentType("image/jpeg");
        testPhoto.setContents(new byte[]{1, 2, 3});

        // Create test file
        testFile = new MockMultipartFile(
                "file",
                "photo.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        // Set test URL
        testUrl = "http://example.com/photo.jpg";
    }

    @Test
    void storePhoto_ShouldReturnSavedPhoto() throws IOException {
        // Arrange
        when(vehiclePhotoRepository.save(any(VehiclePhoto.class))).thenAnswer(invocation -> {
            VehiclePhoto savedPhoto = invocation.getArgument(0);
            savedPhoto.setId(1L);
            return savedPhoto;
        });

        // Act
        VehiclePhoto result = vehiclePhotoService.storePhoto(testFile, testUrl);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(testUrl, result.getUrl());
        assertEquals("photo.jpg", result.getOriginalFileName());
        assertEquals("image/jpeg", result.getContentType());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getContents());
        verify(vehiclePhotoRepository, times(1)).save(any(VehiclePhoto.class));
    }

    @Test
    void getPhotoById_WithValidId_ShouldReturnPhoto() {
        // Arrange
        when(vehiclePhotoRepository.findById(1L)).thenReturn(Optional.of(testPhoto));

        // Act
        VehiclePhoto result = vehiclePhotoService.getPhotoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testPhoto.getId(), result.getId());
        assertEquals(testPhoto.getUrl(), result.getUrl());
        assertEquals(testPhoto.getOriginalFileName(), result.getOriginalFileName());
        assertEquals(testPhoto.getContentType(), result.getContentType());
        assertArrayEquals(testPhoto.getContents(), result.getContents());
    }

    @Test
    void getPhotoById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(vehiclePhotoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehiclePhotoService.getPhotoById(999L)
        );
        assertEquals("photo not found", exception.getMessage());
    }
}