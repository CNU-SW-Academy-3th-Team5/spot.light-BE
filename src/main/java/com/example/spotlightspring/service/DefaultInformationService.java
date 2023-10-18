package com.example.spotlightspring.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import com.example.spotlightspring.entity.InformationEntity;
import com.example.spotlightspring.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultInformationService implements InformationService {
    private final InformationRepository informationRepository;
    private final String fileDir;

    @Autowired
    public DefaultInformationService(InformationRepository informationRepository, @Value("${spring.servlet.multipart.location}") String fileDir) {
        this.informationRepository = informationRepository;
        this.fileDir = fileDir;
    }

    @Override
    public List<InformationEntity> getAllInformations() {
        return informationRepository.findAll();
    }

    @Transactional
    @Override
    public InformationEntity createInformation(MultipartFile files, String address, String tip) throws IOException {
        UUID informationUuid = UUID.randomUUID();
        String extension = files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));
        String savedName = informationUuid + extension;
        String savedPath = fileDir + savedName;
        String loadPath = "http://49.50.175.53:8080" + "/img/" + savedName;
        files.transferTo(new File(savedPath));

        double latitude = 0;
        double longitude = 0;

        File file = new File(fileDir + savedName);

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if(gpsDirectory != null) {
                latitude = gpsDirectory.getGeoLocation().getLatitude();
                longitude = gpsDirectory.getGeoLocation().getLongitude();
            }

            ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            String camera = null;
            Date photoTime = null;

            if(exifIFD0Directory != null) {
                camera = exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL);
                photoTime = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
            }

            InformationEntity informationEntity = new InformationEntity();
            informationEntity.setInformationUuid(informationUuid);
            informationEntity.setExtension(extension);
            informationEntity.setSavedName(savedName);
            informationEntity.setSavedPath(savedPath);
            informationEntity.setLoadPath(loadPath);
            informationEntity.setLatitude(latitude);
            informationEntity.setLongitude(longitude);
            informationEntity.setUploadTime(new Date());
            informationEntity.setCamera(camera);
            informationEntity.setPhotoTime(photoTime);
            informationEntity.setAddress(address);
            informationEntity.setTip(tip);

            return informationRepository.save(informationEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<InformationEntity> getInformationById(UUID informationUuid) {
        return informationRepository.findByInformationUuid(informationUuid);
    }
}

