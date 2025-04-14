package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.model.VehiclePhoto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VehiclePhotoService {

    VehiclePhoto storePhoto(MultipartFile file, String url) throws IOException;

    VehiclePhoto getPhotoById(Long id);
}
