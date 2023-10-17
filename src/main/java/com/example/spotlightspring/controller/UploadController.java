package com.example.spotlightspring.controller;

import com.drew.imaging.ImageProcessingException;
import com.example.spotlightspring.entity.InformationEntity;
import com.example.spotlightspring.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class UploadController {
    private final InformationService informationService;

    @Autowired
    public UploadController(InformationService informationService) {
        this.informationService = informationService;
    }

    @PostMapping("/upload")
    public InformationEntity createInformation(@RequestPart("image") MultipartFile files,
                                               @RequestPart("address") String address,
                                               @RequestPart("tip") String tip) throws IOException, ImageProcessingException {
        InformationEntity information = informationService.createInformation(files, address, tip);
        return information;
    }

}
