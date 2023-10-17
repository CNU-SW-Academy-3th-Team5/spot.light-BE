package com.example.spotlightspring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 정상적으로 연결이 되나 확인
@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping("")
    public String hello() {
        return "Connection Succesful";
    }
}