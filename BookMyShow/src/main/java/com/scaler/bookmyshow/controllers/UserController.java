package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.dtos.SignUpRequestDto;
import com.scaler.bookmyshow.dtos.SignUpResponseDto;
import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto requestDto) {
        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        SignUpResponseDto responseDto = new SignUpResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());

        return responseDto;
    }
}
