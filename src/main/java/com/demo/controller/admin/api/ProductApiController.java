package com.demo.controller.admin.api;

import com.demo.application.product.ProductAdminApplication;
import com.demo.common.UserContext;
import com.demo.controller.admin.dto.ProductAdminResponseDto;
import com.demo.controller.internal.dto.RequestingUserDto;
import com.demo.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductApiController
 * - 쿠팡 내부 MD 직원들이나 개발자 등이 상품이나 유저를 등록하고 삭제하기 위함 = 어드민 기능
 * 1. 그 중에서 "Product"ApiController 상품을 등록하고 삭제하기 위한 API
 * - API이기 때문에 @Controller + @ResponseBody을 같이 쓴 것과 같은 @RestController 사용
 */
@RestController
@RequiredArgsConstructor
public class ProductApiController {
    private final ProductAdminApplication productAdminApplication;

    @GetMapping(value = "/admin/api/products")
    public List<ProductAdminResponseDto> retrieve() {
        return productAdminApplication.retrieve();
    }

    @GetMapping(value = "/admin/api/products/{id}")
    public ProductAdminResponseDto retrieve(@PathVariable Integer id) {
        return productAdminApplication.retrieve(id);
    }

    @PostMapping(value = "/admin/api/products")
    public ProductAdminResponseDto create(@RequestBody ProductAdminUpsertRequestDto request) {
        Product creating = request.toEntity();
        return productAdminApplication.create(creating);
    }

    @PutMapping(value = "/admin/api/products/{id}")
    public ProductAdminResponseDto update(@PathVariable Integer id, @RequestBody ProductAdminUpsertRequestDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            return productAdminApplication.update(id, request);
        }
    }

    @PatchMapping(value = "/admin/api/products/{id}/active")
    public void active(@PathVariable Integer id, @RequestBody RequestingUserDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            productAdminApplication.active(id);
        }
    }

    @PatchMapping(value = "/admin/api/products/{id}/soft-delete")
    public void softDelete(@PathVariable Integer id, @RequestBody RequestingUserDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            productAdminApplication.softDelete(id);
        }
    }

    @DeleteMapping(value = "/admin/api/products/{id}/hard-delete")
    public void hardDelete(@PathVariable Integer id, @RequestBody RequestingUserDto request) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            productAdminApplication.hardDelete(id);
        }
    }
}
