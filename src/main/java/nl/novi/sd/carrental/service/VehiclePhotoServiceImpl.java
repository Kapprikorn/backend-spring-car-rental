package nl.novi.sd.carrental.service;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.VehiclePhoto;
import nl.novi.sd.carrental.repository.VehiclePhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class VehiclePhotoServiceImpl implements VehiclePhotoService {

    private final VehiclePhotoRepository vehiclePhotoRepository;


    @Override
    public VehiclePhoto storePhoto(MultipartFile file, String url) throws IOException {
        VehiclePhoto photo = new VehiclePhoto();

        photo.setOriginalFileName(file.getOriginalFilename());
        photo.setUrl(url);
        photo.setContentType(file.getContentType());
        photo.setContents(file.getBytes());

        return vehiclePhotoRepository.save(photo);
    }

    @Override
    public VehiclePhoto getPhotoById(Long id) {
        return vehiclePhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("photo not found"));
    }
}
