package com.demo.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller // 유저가 요청을 보내는것을 처리하는 Handler 입니다
public class CustomErrorController implements ErrorController {
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String handleError(HttpServletRequest request, Model model) {

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("status", statusCode);
        model.addAttribute("reason", HttpStatus.valueOf(statusCode).getReasonPhrase());
        return "error";
    }
}
