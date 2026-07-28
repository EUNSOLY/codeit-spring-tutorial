package com.demo.demo.controller;


import com.demo.demo.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users") // 공통으로 들어갈 경로는 클래스 자체에 명시 가능
public class UserPageController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView userPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/users/list");
        return modelAndView;
    }

    @RequestMapping(value = "/1/detail", method = RequestMethod.GET)
    public ModelAndView detailPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id", 1);
        modelAndView.addObject("name", "Aaron");
        modelAndView.addObject("age", 10);
        modelAndView.addObject("job", "Developer");
        modelAndView.addObject("specialty", "Backend");
        modelAndView.setViewName("/users/detail");
        return modelAndView;
    }
}
