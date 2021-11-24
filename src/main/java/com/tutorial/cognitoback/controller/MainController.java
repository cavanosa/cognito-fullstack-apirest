package com.tutorial.cognitoback.controller;

import com.tutorial.cognitoback.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MainController {

    @GetMapping("/hello")
    public ResponseEntity<MessageDto> hello() {
        return ResponseEntity.ok(new MessageDto("hola desde cognito"));
    }
}
