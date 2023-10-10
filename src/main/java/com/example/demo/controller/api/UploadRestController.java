package com.example.demo.controller.api;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.example.demo.model.Information;
import com.example.demo.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UploadRestController {
  private final InformationRepository informationRepository;
  private final String fileDir;

  public UploadRestController(InformationRepository informationRepository, @Value("${spring.servlet.multipart.location}") String fileDir) {
    this.informationRepository = informationRepository;
    this.fileDir = fileDir;
  }

  @PostMapping("/api/v1/upload")
  public Information createInformation(@RequestParam("image") MultipartFile files) throws IOException, ImageProcessingException {
    String origName = files.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String extension = origName.substring(origName.lastIndexOf("."));
    String savedName = uuid + extension;
    String savedPath = fileDir + savedName;
    String loadPath = "http://49.50.175.53:8080" + "/img/" + savedName;
    files.transferTo(new File(savedName));

    double latitude = 0;    //위도
    double longitude = 0;    //경도

    File file = new File(fileDir + savedName);
    Metadata metadata = ImageMetadataReader.readMetadata(file);
    GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

    latitude = gpsDirectory.getGeoLocation().getLatitude();
    longitude = gpsDirectory.getGeoLocation().getLongitude();

    var information = new Information(origName, uuid, extension, savedName, savedPath, loadPath, latitude, longitude);
    return informationRepository.insert(information);
  }
}
