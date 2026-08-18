package com.example.demo.advice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvalidParameterDto {
    String parameter;           // 검증에 실패한 파라미터(필드)명 — 예: "id"
    Object actualValue;         // 실제로 들어온 값 (검증 실패를 유발한 값) — 예: 0
    Object criteriaValue;       // 제약 조건의 기준값 — 예: @Min(1)이면 1
    String criteria;            // 위반된 제약 조건(어노테이션)명 — 예: "Max", "Min", "NotNull"
    String violationMessage;    // 사용자에게 보여줄 검증 실패 메시지 — 예: "1 이상의 값이 들어가야합니다"
}

