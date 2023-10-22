package com.example.spotlightspring.controller;

import com.example.spotlightspring.entity.KakaoProfile;
import com.example.spotlightspring.entity.OAuthToken;
import com.example.spotlightspring.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")

public class HomeController {

    private final KakaoService kakaoService;

    @RequestMapping(value="/kakao", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());

        return kakaoService.getKakaoLogin();
    }
    @GetMapping("/auth")
    public @ResponseBody String kakaoAuth(String code) throws JsonProcessingException {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","6d45b34bde95e3af7ac2fb1fc9ea6552");
        params.add("redirect_uri","http://localhost:8080/api/v1/auth");
        params.add("code",code);
        params.add("client_secret","a249FfTtdg1q1txCo89jSh8haIYOuW39");
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest=
                new HttpEntity<>(params,headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(),OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 액세스 토큰 : "+oauthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization","Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile  = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("사용자 이름 : " + kakaoProfile.getProperties().getNickname());
        System.out.println("사용자 이메일 : " + kakaoProfile.getKakao_account().getEmail());






        return response2.getBody();


    }


}