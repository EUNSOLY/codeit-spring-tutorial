package com.example.demo.controller.api;

import com.example.demo.application.product.ProductAdminApplication;
import com.example.demo.common.context.UserContext;
import com.example.demo.controller.api.dto.ProductAdminResponseDto;
import com.example.demo.controller.api.dto.ProductAdminUpsertRequestDto;
import com.example.demo.controller.api.dto.RequestingUserDto;
import com.example.demo.exception.CodeitRuntimeException;
import com.example.demo.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ProductApiController
 * - 쿠팡 내부 MD 직원들이나 개발자 등이 상품이나 유저를 등록하고 삭제하기 위함 = 어드민 기능
 * 1. 그 중에서 "Product"ApiController 상품을 등록하고 삭제하기 위한 API
 * - API이기 때문에 @Controller + @ResponseBody을 같이 쓴 것과 같은 @RestController 사용
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductApiController {
    private final ProductAdminApplication productAdminApplication;

    @GetMapping(value = "/admin/api/products")
    public List<ProductAdminResponseDto> retrieve() {
        return productAdminApplication.retrieve();
    }

    // 응답에 상태코드를 넣는 방법 2.
    // 2. 직접 ResponseEntity 반환 객체를 만들어서 반환
    @GetMapping(value = "/admin/api/products/{id}")
    public ResponseEntity<ProductAdminResponseDto> retrieve(@PathVariable Integer id) {
        try {
            ProductAdminResponseDto response = productAdminApplication.retrieve(id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(response);
        } catch (CodeitRuntimeException exception) {
            // 내가 알고있거나 / 명시적으로 처리하고싶어하는 예외 상황에 대해 이렇게 구체적인 예외 클래스를 명시해서 처리
            ExceptionType exceptionType = exception.getExceptionType(); // 예외에 정의된 타입(로그 레벨 등 메타정보 포함) 조회
            log.makeLoggingEventBuilder(exceptionType.getLevel())   // ExceptionType이 갖고 있는 로그 레벨(Enum)로 로깅 이벤트 빌더 생성
                    .setCause(exception) // 원본 예외를 cause로 설정 (스택트레이스 함께 기록됨)
                    .log(exception.getMessage());  // 메시지 설정과 동시에 빌더를 종료하며 실제 로그를 출력 (build+log를 한번에 수행)

            return ResponseEntity
                    .status(exceptionType.getStatus())
                    .build();
        } catch (RuntimeException e /* 클래스 다형성에 의해 우리가 만드는 예외 Exception 들이 모두 RuntimeException 상속받기에 여기로 다 들어옴 */) {
            // 세상에는 (라이브러리, 프레임워크 등) 너무 다양한 예외들이 존재하기에 우리가 catch 하지 못하고 놓친 예외에 대해 꼭 마지막까지 처리해줘야한다
            // = switch 구문에서 default 와 거의 같은 목적의 코드라고 보면 된다
            log.error("우리가 커버하지 못한 예외 발생", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    // 응답에 상태코드를 넣는 방법 1.
    // 1. 메서드 상단 어노테이션을 통해 명시 - 쉽지만 문제는 그 메서드에서 나가는 모든 응답에 그 상태코드가 들어감 = 익셉션에 따른 다른 상태코드를 반환하고싶을때 어쩔도리가 없음
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/admin/api/products")
    public ProductAdminResponseDto create(
            @RequestPart ProductAdminUpsertRequestDto request,
            @RequestPart(required = false) MultipartFile thumbnail
    ) {
        return productAdminApplication.create(request, thumbnail);
    }

    @PutMapping(value = "/admin/api/products/{id}")
    public ProductAdminResponseDto update(
            @PathVariable Integer id,
            @RequestPart ProductAdminUpsertRequestDto request,
            @RequestPart(required = false) MultipartFile thumbnail
    ) {
        Integer requestedUserId = request.getRequestUserId();
        try (UserContext.ContextScope ignored = UserContext.withUser(requestedUserId)) {
            return productAdminApplication.update(id, request, thumbnail);
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
