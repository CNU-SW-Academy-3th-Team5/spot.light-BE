package com.example.demo.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.example.demo.model.Information;
import com.example.demo.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultInformationService implements InformationService {
  private final InformationRepository informationRepository;
  private final String fileDir;

  public DefaultInformationService(InformationRepository informationRepository, @Value("${spring.servlet.multipart.location}") String fileDir) {
    this.informationRepository = informationRepository;
    this.fileDir = fileDir;
  }

  @Override
  public List<Information> getAllInformations() {
    return informationRepository.findAll();
  }

  @Override
  public Information createInformation(MultipartFile files) throws IOException, ImageProcessingException {
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

  @Override
  public Optional<Information> getInformationById(String uuid) {
    return informationRepository.findById(uuid);
  }

}