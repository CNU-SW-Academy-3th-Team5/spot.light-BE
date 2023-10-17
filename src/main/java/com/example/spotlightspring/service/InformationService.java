package com.example.spotlightspring.service;

import com.drew.imaging.ImageProcessingException;
import com.example.spotlightspring.entity.InformationEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InformationService {
    List<InformationEntity> getAllInformations();
    InformationEntity createInformation(MultipartFile files, String address, String tip) throws IOException, ImageProcessingException;
    Optional<InformationEntity> getInformationById(UUID informationUuid);
}
