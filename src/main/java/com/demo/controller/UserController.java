package com.demo.controller;

import com.demo.dto.UserCreateRequestDto;
import com.demo.entity.User;
import com.demo.service.UserServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor // 필수
public class UserController {
    private final ApplicationContext applicationContext;
    private final UserServiceInterface userService;

    @GetMapping("")
    public String userPage(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "/users/list";
    }

    @GetMapping("/detail")
    public String detailPage(
            @RequestParam Integer id,
            Model model
    ) {
        User user = userService.findById(id);
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
        return userService.findById(id);
    }

    @GetMapping("/bean")
    @ResponseBody
    public String bean() {
        return applicationContext.getBean("AUserService", UserServiceInterface.class).toString();
    }


    // POST
    @PostMapping(value = "")
    @ResponseBody
    public User save(
            @RequestBody @Valid UserCreateRequestDto request
    ) {
        return userService.save(request.getName(), request.getAge(), request.getJob(), request.getSpecialty());
    }
}
