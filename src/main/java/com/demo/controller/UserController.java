package com.demo.controller;

import com.demo.entity.User;
import com.demo.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor // 필수
public class UserController {
    private final ApplicationContext applicationContext;
    private final UserServiceInterface AUserService;

    @GetMapping("")
    public String userPage(Model model) {
        List<User> users = AUserService.findAll();
        model.addAttribute("users", users);
        return "/users/list";

    }

    @GetMapping("/detail")
    public String detailPage(
            @RequestParam Integer id,
            Model model
    ) {
        User user = AUserService.findById(id);
        model.addAttribute("id", user.getId());
        model.addAttribute("name", user.getName());
        model.addAttribute("age", user.getAge());
        model.addAttribute("job", user.getJob());
        model.addAttribute("specialty", user.getSpecialty());

        return "/users/detail";
    }

    @GetMapping("/data")
    @ResponseBody
    public User detailData(
            @RequestParam(required = true, defaultValue = "1") Integer id
    ) {
        return AUserService.findById(id);
    }

    @GetMapping("/bean")
    @ResponseBody
    public String bean() {
        return applicationContext.getBean("AUserService", UserServiceInterface.class).toString();
    }
}
