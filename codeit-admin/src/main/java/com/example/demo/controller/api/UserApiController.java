package com.example.demo.controller.api;

import com.example.demo.application.user.UserAdminApplication;
import com.example.demo.common.context.UserContext;
import com.example.demo.controller.api.dto.RequestingUserDto;
import com.example.demo.controller.api.dto.UserAdminCreateRequestDto;
import com.example.demo.controller.api.dto.UserAdminResponseDto;
import com.example.demo.controller.api.dto.UserAdminUpdateRequestDto;
import com.example.demo.repository.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductApiController
 * - 쿠팡 내부 MD 직원들이나 개발자 등이 상품이나 유저를 등록하고 삭제하기 위함 = 어드민 기능
 * 1. 그 중에서 "User"ApiController 유저를 등록하고 삭제하기 위한 API
 * - API이기 때문에 @Controller + @ResponseBody을 같이 쓴 것과 같은 @RestController 사용
 */
@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserAdminApplication userAdminApplication;

    @GetMapping(value = "/admin/api/users")
    public List<UserAdminResponseDto> retrieve() {
        return userAdminApplication.retrieve();
    }

    @GetMapping(value = "/admin/api/users/{id}")
    public UserAdminResponseDto retrieve(@PathVariable Integer id) {
        return userAdminApplication.retrieve(id);
    }

    @PostMapping(value = "/admin/api/users")
    public UserAdminResponseDto create(@RequestBody UserAdminCreateRequestDto request) {
        User creating = request.toEntity();
        return userAdminApplication.create(creating);
    }

    @PutMapping(value = "/admin/api/users/{id}")
    public UserAdminResponseDto update(@PathVariable Integer id, @RequestBody UserAdminUpdateRequestDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            return userAdminApplication.update(id, request);
        }
    }

    @PatchMapping(value = "/admin/api/users/{id}/active")
    public void active(@PathVariable Integer id, @RequestBody RequestingUserDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            userAdminApplication.active(id);
        }
    }

    @PatchMapping(value = "/admin/api/users/{id}/soft-delete")
    public void softDelete(@PathVariable Integer id, @RequestBody RequestingUserDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            userAdminApplication.softDelete(id);
        }
    }

    @DeleteMapping(value = "/admin/api/users/{id}/hard-delete")
    public void hardDelete(@PathVariable Integer id, @RequestBody RequestingUserDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            userAdminApplication.hardDelete(id);
        }
    }
}
