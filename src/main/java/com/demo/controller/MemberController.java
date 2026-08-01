package com.demo.controller;

import com.demo.dto.MemberPatchRequestDto;
import com.demo.dto.MemberResponseDto;
import com.demo.dto.MemberUpsertRequestDto;
import com.demo.service.MemberService;
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
        List<MemberResponseDto> users = memberService.readAll();
        model.addAttribute("users", users);

        users.forEach(user ->
                log.info("User : id={}, name={}, age={}, job={}, email={}, ", user.getId(), user.getName(), user.getAge(), user.getJob(), user.getEmail()));

        return "users/list";
    }

    @GetMapping(value = "/users/{id}")
    public String getUserPage(
            @PathVariable Integer id,
            ModelMap modelMap
    ) {
        MemberResponseDto memberResponse = memberService.read(id);
        modelMap.addAttribute("id", memberResponse.getId());
        modelMap.addAttribute("name", memberResponse.getName());
        modelMap.addAttribute("age", memberResponse.getAge());
        modelMap.addAttribute("job", memberResponse.getJob());
        modelMap.addAttribute("email", memberResponse.getEmail());

        log.info(modelMap.toString());

        return "users/detail";
    }

    // 다건 생성
    @PostMapping(value = "/api/users-all")
    @ResponseBody
    public List<MemberResponseDto> createUsers(
            @RequestBody List<MemberUpsertRequestDto> requestDtos
    ) {
        List<MemberResponseDto> members = memberService.createAll(requestDtos);
        log.info("다건 생성");
        members.forEach(member ->
                log.info("Member : id={}, name={}, age={}, job={}, email={}, ", member.getId(), member.getName(), member.getAge(), member.getJob(), member.getEmail()));
        return members;
    }


    // 단일 생성
    @PostMapping(value = "/api/users")
    @ResponseBody
    public MemberResponseDto createUser(
            @RequestBody MemberUpsertRequestDto requestDto
    ) {
        log.info("단일 생성");
        MemberResponseDto member = memberService.create(requestDto);
        log.info("Member : id={}, name={}, age={}, job={}, email={}, ", member.getId(), member.getName(), member.getAge(), member.getJob(), member.getEmail());
        return memberService.create(requestDto);
    }

    // 전체 조회
    @GetMapping(value = "/api/users")
    @ResponseBody
    public List<MemberResponseDto> getUsers() {
        List<MemberResponseDto> members = memberService.readAll();
        log.info("전체 조회");
        members.forEach(member ->
                log.info("Member : id={}, name={}, age={}, job={}, email={}, ", member.getId(), member.getName(), member.getAge(), member.getJob(), member.getEmail()));
        return members;
    }

    // 단일 조회
    @GetMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto getMember(@PathVariable Integer id) {
        MemberResponseDto member = memberService.read(id);
        log.info("단일 조회");
        log.info("Member : id={}, name={}, age={}, job={}, email={}, ", member.getId(), member.getName(), member.getAge(), member.getJob(), member.getEmail());
        return memberService.read(id);
    }


    // 단일 부분 수정
    @PatchMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto updateUser(
            @PathVariable Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) String email
    ) {
        MemberPatchRequestDto patchRequestDto = new MemberPatchRequestDto(name, age, job, email);
        MemberResponseDto member = memberService.update(id, patchRequestDto);
        log.info("단일 부분 수정");
        log.info("수정 Member : id={}, name={}, age={}, job={}, email={}, ", member.getId(), member.getName(), member.getAge(), member.getJob(), member.getEmail());
        return member;
    }

    // 단일 전체 수정
    @PutMapping(value = "/api/users/{id}")
    @ResponseBody
    public MemberResponseDto replaceUser(
            @PathVariable Integer id,
            @ModelAttribute MemberUpsertRequestDto requestDto
    ) {
        MemberResponseDto member = memberService.update(id, requestDto);
        log.info("단일 전체 수정");
        log.info("수정 Member : id={}, name={}, age={}, job={}, email={}, ", member.getId(), member.getName(), member.getAge(), member.getJob(), member.getEmail());
        return member;
    }

    // 단일 삭제
    @DeleteMapping(value = "/api/users/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Integer id) {
        log.info("단일 삭제");
        MemberResponseDto member = memberService.read(id);
        log.info("삭제 Member : id={}, name={}, age={}, job={}, email={}, ", member.getId(), member.getName(), member.getAge(), member.getJob(), member.getEmail());
        memberService.delete(id);
    }

}
