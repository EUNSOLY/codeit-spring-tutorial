package com.demo.controller;


import com.demo.dto.MemberCreateRequestDto;
import com.demo.dto.MemberPutRequestDto;
import com.demo.dto.MemberResponseDto;
import com.demo.dto.MemberUpdateRequestDto;
import com.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MemberResponseDto> createUser(
            @RequestBody MemberCreateRequestDto request
    ) {
        try {
            MemberResponseDto memberResponse = memberService.create(request);
            log.info("사용자 생성 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());
            return ResponseEntity
                    .ok(memberResponse);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

    }

    @PostMapping(value = "/api/users-all")
    @ResponseBody
    public ResponseEntity<List<MemberResponseDto>> createUsers(
            @RequestBody @Valid List<MemberCreateRequestDto> requests
    ) {
        try {
            List<MemberResponseDto> memberResponses = memberService.createAll(requests);
            memberResponses.forEach(memberResponse -> {
                log.info("사용자 다중 생성 :  User (id={}, name={}, age={}, job={}, email={})",
                        memberResponse.getId(), memberResponse.getName(),
                        memberResponse.getAge(), memberResponse.getJob(),
                        memberResponse.getEmail());
            });
            return ResponseEntity
                    .ok(memberResponses);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

    }

    @GetMapping(value = "/api/users")
    @ResponseBody
    public ResponseEntity<List<MemberResponseDto>> getUsers() {
        try {
            List<MemberResponseDto> memberResponses = memberService.readAll();
            memberResponses.forEach(memberResponse -> {
                log.info("사용자 전체 조회 :  User (id={}, name={}, age={}, job={}, email={})",
                        memberResponse.getId(), memberResponse.getName(),
                        memberResponse.getAge(), memberResponse.getJob(),
                        memberResponse.getEmail());
            });
            // 생성자로 하는 방법
            return new ResponseEntity<>(memberResponses, HttpStatus.OK);
        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/api/users/{id}")
    @ResponseBody
    public ResponseEntity<MemberResponseDto> getUser(
            @PathVariable(required = true) Integer id
    ) {
        try {
            MemberResponseDto memberResponse = memberService.read(id);
            log.info("사용자 1건 조회 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping(value = "/api/users/{id}")
    @ResponseBody
    public ResponseEntity<MemberResponseDto> patchUser(
            @PathVariable(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) String email
    ) {
        try {
            MemberUpdateRequestDto request = new MemberUpdateRequestDto(name, age, job, email);
            MemberResponseDto memberResponse = memberService.update(id, request);
            log.info("사용자 부분 수정 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping(value = "/api/users/{id}")
    @ResponseBody
    public ResponseEntity<MemberResponseDto> putUser(
            @PathVariable(required = false) Integer id,
            @ModelAttribute @Valid MemberPutRequestDto request
    ) {
        try {
            MemberResponseDto memberResponse = memberService.update(id, request);
            log.info("사용자 전체 수정 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "/api/users/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable(required = true) Integer id) {
        try {
            MemberResponseDto memberResponse = memberService.delete(id);
            log.info("삭제 사용자 :  User (id={}, name={}, age={}, job={}, email={})",
                    memberResponse.getId(), memberResponse.getName(),
                    memberResponse.getAge(), memberResponse.getJob(),
                    memberResponse.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패.");
        }
    }
}
