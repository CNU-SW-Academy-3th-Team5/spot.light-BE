package com.example.demo.controller;

import com.drew.imaging.ImageProcessingException;
import com.example.demo.service.InformationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class InformationController {

  private final InformationService informationService;

  public InformationController(InformationService informationService) {
    this.informationService = informationService;
  }

  @GetMapping("/information")
  public String InformationPage(Model model) {
    var information = informationService.getAllInformations();
    model.addAttribute("informations", information);
    return "information-list";
  }

  @GetMapping("/new-information")
  public String newInformationPage() {
    return "new-information";
  }

  @PostMapping("/information")
  public String newInformation(@RequestParam("imagePath") MultipartFile file) throws IOException, ImageProcessingException {
    informationService.createInformation(file);
    return "redirect:/information";
  }

}
