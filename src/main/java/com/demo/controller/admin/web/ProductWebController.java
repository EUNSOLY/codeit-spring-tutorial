package com.demo.controller.admin.web;

import com.demo.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

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
    private final ProductRepository productRepository;

}
