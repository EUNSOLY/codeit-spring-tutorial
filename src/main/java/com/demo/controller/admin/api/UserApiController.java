package com.demo.controller.admin.api;

import com.demo.application.user.UserAdminApplication;
import com.demo.common.UserContext;
import com.demo.controller.admin.dto.UserAdminCreateRequestDto;
import com.demo.controller.admin.dto.UserAdminResponseDto;
import com.demo.controller.admin.dto.UserAdminUpdateRequestDto;
import com.demo.controller.internal.dto.RequestingUserDto;
import com.demo.domain.user.User;
import com.demo.repository.user.UserRepository;
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
    private final UserRepository userRepository;
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
        User creating = request.to();
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
