package com.demo.controller;

import com.demo.entity.User;
import com.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/users") // 공통으로 들어갈 경로는 클래스 자체에 명시 가능
public class UserPageController {
    private final UserService userService = new UserService();

//    @GetMapping(value = "")
//    public ModelAndView userPage() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("/users/list");
//        return modelAndView;
//    }

    @GetMapping(value = "")
    @ResponseBody
    public List<User> userPage() {
        return userService.findAll();
    }

    @GetMapping(value = "/1")
    @ResponseBody
    public User detailPage() {
        return userService.findById(1);
    }
}
