package com.example.spotlightspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageDto {
    private Long id;
    private String fileName;
    private String fileUrl;
    private double latitude;
    private double longitude;
    private Date dateTime;
}
