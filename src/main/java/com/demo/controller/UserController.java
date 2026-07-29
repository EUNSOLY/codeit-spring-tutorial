package com.demo.controller;

import com.demo.entity.User;
import com.demo.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserController {
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
}
