package com.example.spotlightspring.controller;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.example.spotlightspring.entity.InformationEntity;
import com.example.spotlightspring.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class InformationController {

    @Autowired InformationService informationService;

    @GetMapping("/information")
    public List<InformationEntity> productList() {
        return informationService.getAllInformations();
    }
}
