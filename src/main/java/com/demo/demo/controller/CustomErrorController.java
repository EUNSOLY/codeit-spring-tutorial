package com.demo.demo.controller;

import com.demo.demo.HttpErrorStatus;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller // 유저가 요청을 보내는것을 처리하는 Handler 입니다
public class CustomErrorController implements ErrorController {
    @RequestMapping(value = "/error", method = RequestMethod.GET)
//  Thymeleaf = Template Engine = ViewTemplate + Model 합쳐서 -> View 라는 완성된 페이지를 만들어 반환하는것
//  - String 타입으로 반환한다는것은 : Thymeleaf 에게 ViewTemplate 을 무엇을 써야하는지 파일명을 알려주는것
//  - String = ViewTemplate 명칭
//    - 지금의 실습에서 ViewTemplate 은 있는데 왜? Model 이 없죠? Model 은 어디갔을까요?
    public ModelAndView handleError(HttpServletRequest request) {
//      String + Model
//      ModelAndView 클래스로 ViewTemplate + Model 두개를 한번에 넣어 반환할 수 있다
//      ModelAndView modelAndView = new ModelAndView();
        log.error(" - 우리가 직접 만든 에러페이지로 이동합니다 !");
//      세상에는 수많은 요청이 있고 그 요청들마다 요청을 보낼때 표준화된 어떤 필드나 값들이 없어요
//      request.getAttribute("KAKAO_AUTH_CODE");
//      request.getAttribute("KAKAO_INFORMATION");
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
//          return "error/404";
            return HttpErrorStatus.NOT_FOUND.toModelAndView();
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//          return "error/500";
            return HttpErrorStatus.INTERNAL_SERVER_ERROR.toModelAndView();
        } else {
            return HttpErrorStatus.INTERNAL_SERVER_ERROR.toModelAndView();
        }
    }
}
