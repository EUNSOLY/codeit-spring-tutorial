package com.example.demo.controller.web;

import com.example.demo.application.user.UserAdminApplication;
import com.example.demo.controller.api.dto.UserAdminResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * UserWebController
 * - 쿠팡 내부 MD 직원들이나 개발자 등이 상품이나 유저를 등록하고 삭제하기 위함 = 어드민 기능
 * 1. 그 중에서 "User"WebController 유저를 등록하고 삭제하기 위한 HTML 페이지 (SSR)
 * 2. 스프링 서버에서 Thymeleaf 통해 페이지를 만들어 반환한다는 뜻 =
 * - 페이지 제공용이기 때문에 @Controller 사용
 */
@Controller
@RequiredArgsConstructor
public class UserWebController {
    private final UserAdminApplication userAdminApplication;

    @RequestMapping(method = RequestMethod.GET, value = "/admin/web/users")
    public String users(Model model) {
        List<UserAdminResponseDto> users = userAdminApplication.retrieve();
        model.addAttribute("users", users);
        return "/users/list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/web/users/{id}")
    public String user(@RequestParam Integer id, Model model) {
        UserAdminResponseDto user = userAdminApplication.retrieve(id);
        model.addAttribute("id", user.getId());
        model.addAttribute("name", user.getName());
        model.addAttribute("grade", user.getGrade());
        model.addAttribute("point", user.getPoint());
        model.addAttribute("deleted", user.isDeleted());
        return "/users/detail";
    }
}
