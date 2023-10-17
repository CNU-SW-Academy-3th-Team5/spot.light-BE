package com.example.spotlightspring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Information")
@Table(name = "Information")
public class InformationEntity {
    @Id
    private UUID informationUuid;

    private String extension;
    private String savedName;
    private String savedPath;
    private String loadPath;
    private double latitude;
    private double longitude;
    private String address; // 입력

    @Temporal(TemporalType.TIMESTAMP)
    private Date photoTime; // 사진 찍은 시간

    private String camera;
    private String tip; // 입력

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTime; // 업로드 시간
}
