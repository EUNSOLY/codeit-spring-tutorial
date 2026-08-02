package com.demo.controller;


import com.demo.common.ApiResponse;
import com.demo.dto.MemberPutRequestDto;
import com.demo.dto.MemberResponseDto;
import com.demo.dto.MemberUpsertRequestDto;
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

    /**
     * ======================================================
     * ModelAndView
     * ======================================================
     */
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


    /**
     * ======================================================
     * RestAPI
     * ======================================================
     */
    @PostMapping(value = "/api/users")
    @ResponseBody
    public ApiResponse<MemberResponseDto> createUser(
            @RequestBody MemberUpsertRequestDto request
    ) {
        try {
            MemberResponseDto memberResponse = memberService.create(request);
            log.info("사용자 생성 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());

            return ApiResponse.success(memberResponse);

        } catch (RuntimeException e) {
            return ApiResponse.fail(500, e.getMessage(), null);
        }

    }

    @PostMapping(value = "/api/users-all")
    @ResponseBody
    public ApiResponse<List<MemberResponseDto>> createUsers(
            @RequestBody @Valid List<MemberUpsertRequestDto> requests
    ) {
        try {
            List<MemberResponseDto> memberResponses = memberService.createAll(requests);
            memberResponses.forEach(memberResponse -> {
                log.info("사용자 다중 생성 :  User (id={}, name={}, age={}, job={}, email={})",
                        memberResponse.getId(), memberResponse.getName(),
                        memberResponse.getAge(), memberResponse.getJob(),
                        memberResponse.getEmail());
            });

            return ApiResponse.success(memberResponses);
        } catch (RuntimeException e) {
            return ApiResponse.fail(500, e.getMessage(), null);
        }

    }

    @GetMapping(value = "/api/users")
    @ResponseBody
    public ApiResponse<List<MemberResponseDto>> getUsers() {
        try {
            List<MemberResponseDto> memberResponses = memberService.readAll();
            memberResponses.forEach(memberResponse -> {
                log.info("사용자 전체 조회 :  User (id={}, name={}, age={}, job={}, email={})",
                        memberResponse.getId(), memberResponse.getName(),
                        memberResponse.getAge(), memberResponse.getJob(),
                        memberResponse.getEmail());
            });

            return ApiResponse.success(memberResponses);
        } catch (RuntimeException e) {
            return ApiResponse.fail(500, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/api/users/{id}")
    @ResponseBody
    public ApiResponse<MemberResponseDto> getUser(
            @PathVariable(required = true) Integer id
    ) {
        try {
            MemberResponseDto memberResponse = memberService.read(id);
            log.info("사용자 1건 조회 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());

            return ApiResponse.success(memberResponse);

        } catch (RuntimeException e) {
            return ApiResponse.fail(500, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/api/users/{id}")
    @ResponseBody
    public ApiResponse<MemberResponseDto> patchUser(
            @PathVariable(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) String email
    ) {
        try {
            MemberUpsertRequestDto request = new MemberUpsertRequestDto(name, age, job, email); // 이게 맞나요... DTO는 컨트롤러랑 친함
            MemberResponseDto memberResponse = memberService.update(id, request);
            log.info("사용자 부분 수정 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());

            return ApiResponse.success(memberResponse);

        } catch (RuntimeException e) {
            return ApiResponse.fail(500, e.getMessage(), null);
        }
    }


    @PutMapping(value = "/api/users/{id}")
    @ResponseBody
    public ApiResponse<MemberResponseDto> putUser(
            @PathVariable(required = false) Integer id,
            @ModelAttribute @Valid MemberPutRequestDto request
    ) {
        try {
            MemberResponseDto memberResponse = memberService.update(id, request);
            log.info("사용자 전체 수정 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());

            return ApiResponse.success(memberResponse);
        } catch (RuntimeException e) {

            return ApiResponse.fail(500, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/api/users/{id}")
    @ResponseBody
    public ApiResponse<MemberResponseDto> deleteUser(@PathVariable(required = true) Integer id) {
        try {
            MemberResponseDto memberResponse = memberService.delete(id);
            log.info("삭제 사용자 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());

            return ApiResponse.success(memberResponse);

        } catch (RuntimeException e) {
            return ApiResponse.fail(500, e.getMessage(), null);
        }
    }
}
