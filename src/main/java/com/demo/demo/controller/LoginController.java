package com.demo.demo.controller;

import com.demo.demo.controller.dto.LoginResponseDto;
import com.demo.demo.service.LoginService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public LoginResponseDto connect(
            @RequestParam @Size(min = 4) String username,
            @RequestParam @Size(min = 4) String password
    ) {
        loginService.connect(username, password);
        return LoginResponseDto.success(username);
    }
}
