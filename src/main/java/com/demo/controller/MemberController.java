package com.demo.controller;


import com.demo.dto.MemberCreateRequestDto;
import com.demo.dto.MemberPutRequestDto;
import com.demo.dto.MemberResponseDto;
import com.demo.dto.MemberUpdateRequestDto;
import com.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/users")
    public String getUsersPage(Model model) {
        List<MemberResponseDto> memberResponses = memberService.readAll();
        model.addAttribute("users", memberResponses);
        return "users/list";
    }

    @GetMapping(value = "/users/{id}")
    public String getUserPage(
            @PathVariable(required = true) Integer id,
            ModelMap modelMap
    ) {
        MemberResponseDto memberResponses = memberService.read(id);
        modelMap.addAttribute("name", memberResponses.getName());
        modelMap.addAttribute("age", memberResponses.getAge());
        modelMap.addAttribute("job", memberResponses.getJob());
        modelMap.addAttribute("email", memberResponses.getEmail());

        return "users/detail";
    }

    @PostMapping(value = "/api/users")
    @ResponseBody
    public MemberResponseDto createUser(
            @RequestBody MemberCreateRequestDto request
    ) {
        MemberResponseDto memberResponse = memberService.create(request);
        log.info("사용자 생성 :  User (id={}, name={}, age={}, job={}, email={})",
                memberResponse.getId(), memberResponse.getName(),
                memberResponse.getAge(), memberResponse.getJob(),
                memberResponse.getEmail());
        return memberResponse;
    }

    @PostMapping(value = "/api/users-all")
    @ResponseBody
    public List<MemberResponseDto> createUsers(
            @RequestBody @Valid List<MemberCreateRequestDto> requests
    ) {
        List<MemberResponseDto> memberResponses = memberService.createAll(requests);
        memberResponses.forEach(memberResponse -> {
            log.info("사용자 다중 생성 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());
        });
        return memberResponses;
    }

    @GetMapping(value = "/api/users")
    @ResponseBody
    public List<MemberResponseDto> getUsers() {
        List<MemberResponseDto> memberResponses = memberService.readAll();
        memberResponses.forEach(memberResponse -> {
            log.info("사용자 전체 조회 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());
        });

        return memberResponses;
    }

    @GetMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto getUser(
            @PathVariable(required = true) Integer id
    ) {
        MemberResponseDto memberResponse = memberService.read(id);
        log.info("사용자 1건 조회 :  User (id={}, name={}, age={}, job={}, email={})",
                memberResponse.getId(), memberResponse.getName(),
                memberResponse.getAge(), memberResponse.getJob(),
                memberResponse.getEmail());
        return memberResponse;
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
        MemberUpdateRequestDto request = new MemberUpdateRequestDto(name, age, job, email);
        MemberResponseDto memberResponse = memberService.update(id, request);
        log.info("사용자 부분 수정 :  User (id={}, name={}, age={}, job={}, email={})",
                memberResponse.getId(), memberResponse.getName(),
                memberResponse.getAge(), memberResponse.getJob(),
                memberResponse.getEmail());
        return memberResponse;
    }


    @PutMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto putUser(
            @PathVariable(required = false) Integer id,
            @ModelAttribute @Valid MemberPutRequestDto request
    ) {
        MemberResponseDto memberResponse = memberService.update(id, request);
        log.info("사용자 전체 수정 :  User (id={}, name={}, age={}, job={}, email={})",
                memberResponse.getId(), memberResponse.getName(),
                memberResponse.getAge(), memberResponse.getJob(),
                memberResponse.getEmail());
        return memberResponse;
    }

    @DeleteMapping(value = "/api/users/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable(required = true) Integer id) {
        MemberResponseDto memberResponse = memberService.delete(id);
        log.info("삭제 사용자 :  User (id={}, name={}, age={}, job={}, email={})",
                memberResponse.getId(), memberResponse.getName(),
                memberResponse.getAge(), memberResponse.getJob(),
                memberResponse.getEmail());
    }
}
