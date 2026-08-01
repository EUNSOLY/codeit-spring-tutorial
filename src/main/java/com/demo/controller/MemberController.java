package com.demo.controller;


import com.demo.dto.MemberResponseDto;
import com.demo.dto.MemberUpsertRequestDto;
import com.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/users")
    public String getUsersPage(Model model) {
        List<MemberResponseDto> memberResponses = memberService.readAll();
        model.addAttribute("users", memberResponses);
        return "user/list";
    }

    @GetMapping(value = "/users/{id}")
    public String getUserPage(
            @PathVariable Integer id,
            ModelMap modelMap
    ) {
        MemberResponseDto memberResponses = memberService.read(id);
        modelMap.addAttribute("name", memberResponses.getName());
        modelMap.addAttribute("age", memberResponses.getAge());
        modelMap.addAttribute("job", memberResponses.getJob());
        modelMap.addAttribute("email", memberResponses.getEmail());


        return "user/detail";
    }

    @PostMapping(value = "/api/users")
    @ResponseBody
    public MemberResponseDto createUser(
            @RequestBody MemberUpsertRequestDto request
    ) {
        return memberService.create(request);
    }

    @PostMapping(value = "/api/users-all")
    @ResponseBody
    public List<MemberResponseDto> createUsers(
            @RequestBody List<MemberUpsertRequestDto> requests
    ) {
        return memberService.createAll(requests);
    }

    @GetMapping(value = "/api/users")
    @ResponseBody
    public List<MemberResponseDto> getUsers() {
        return memberService.readAll();
    }

    @GetMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto getUser(
            @PathVariable Integer id
    ) {
        return memberService.read(id);
    }

    @PatchMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto patchUser(
            @PathVariable(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) String email
    ) {
        MemberUpsertRequestDto request = new MemberUpsertRequestDto(name, age, job, email);
        return memberService.update(id, request);
    }


    @PutMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto putUser(
            @PathVariable(required = false) Integer id,
            @ModelAttribute MemberUpsertRequestDto request
    ) {
        return memberService.update(id, request);
    }

    @DeleteMapping(value = "/api/users/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Integer id) {
        memberService.delete(id);
    }

}
