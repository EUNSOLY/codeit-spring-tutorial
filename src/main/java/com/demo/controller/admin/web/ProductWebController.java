package com.demo.controller.admin.web;

import com.demo.application.product.ProductAdminApplication;
import com.demo.controller.admin.dto.ProductAdminResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ProductWebController
 * - 쿠팡 내부 MD 직원들이나 개발자 등이 상품이나 유저를 등록하고 삭제하기 위함 = 어드민 기능
 * 1. 그 중에서 "Product"WebController 상품을 등록하고 삭제하기 위한 HTML 페이지 (SSR)
 * 2. 스프링 서버에서 Thymeleaf 통해 페이지를 만들어 반환한다는 뜻 =
 * - 페이지 제공용이기 때문에 @Controller 사용
 */
@Controller
@RequiredArgsConstructor
public class ProductWebController {
    private final ProductAdminApplication productAdminApplication;

    @GetMapping(value = "/admin/web/products")
    public String products(Model model) {
        List<ProductAdminResponseDto> products = productAdminApplication.retrieve();
        model.addAttribute("products", products);
        return "/products/list";
    }

    @GetMapping(value = "/admin/web/products/{id}")
    public String product(@RequestParam Integer id, Model model) {
        ProductAdminResponseDto product = productAdminApplication.retrieve(id);
        model.addAttribute("id", product.getId());
        model.addAttribute("name", product.getName());
        model.addAttribute("price", product.getPrice());
        model.addAttribute("stock", product.getStock());
        model.addAttribute("deleted", product.isDeleted());
        return "/products/detail";
    }
}
