package com.demo.demo.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Integer statusCode = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                modelAndView.addObject("title", "404 Not Found!!");
                modelAndView.addObject("description", "Page does not exist");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                modelAndView.addObject("title", "500 Internal Server Error");
                modelAndView.addObject("description", "Any exception is occurred in ");
            }else{
                modelAndView.addObject("title", "Something went wrong!");
                modelAndView.addObject("description", "Our Engineers are on it");
            }
            modelAndView.setViewName("error");

            return modelAndView;
    }
}
