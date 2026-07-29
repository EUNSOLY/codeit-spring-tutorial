package com.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        log.info(" - IndexController 내부에 정의한 @RequestMapping 통해 페이지 반환");
        log.info(" - HandlerMapping 우선순위에 따라 3순위인 WelcomePageHandlerMapping 까지 가지않고 1순위인 RequestMappingHandlerMapping 에서 처리");
        return "index";
    }
}
