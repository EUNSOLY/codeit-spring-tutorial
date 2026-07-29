package com.demo.controller;

import com.demo.entity.User;
import com.demo.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private List<UserServiceInterface> userService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private UserServiceInterface AUserService;

    @GetMapping("")
    public String userPage(Model model) {
        List<User> users = AUserService.findAll();
        model.addAttribute("users", users);
        return "/users/list";

    }

    @GetMapping("/1/detail")
    public String detailPage(Model model) {
        User user = AUserService.findById(1);
        model.addAttribute("id", user.getId());
        model.addAttribute("name", user.getName());
        model.addAttribute("age", user.getAge());
        model.addAttribute("job", user.getJob());
        model.addAttribute("specialty", user.getSpecialty());

        return "/users/detail";
    }

    @GetMapping("/1/data")
    @ResponseBody
    public User detailData() {
        User user = AUserService.findById(1);
        return user;
    }

    @GetMapping("/bean")
    @ResponseBody
    public String bean() {
        return applicationContext.getBean("AUserService", UserServiceInterface.class).toString();
    }
}
