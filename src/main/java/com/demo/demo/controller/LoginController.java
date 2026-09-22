package com.demo.demo.controller;

import com.demo.demo.controller.dto.LoginResponseDto;
import com.demo.demo.exception.CustomException;
import com.demo.demo.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<LoginResponseDto> connect(@RequestParam String username, @RequestParam String password) {
        try {
            loginService.connect(username, password);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(LoginResponseDto.success(username));
        } catch (CustomException e) {
            log.makeLoggingEventBuilder(e.getType().getLevel())
                    .log(e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(LoginResponseDto.failed(e.getType()))
                    ;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LoginResponseDto.failed(e.getMessage()))
                    ;
        }
    }
}
