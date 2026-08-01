package com.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // 직렬화 시 객체의 toString 출력 반환
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum JobType {
    DEVELOPER("Developer"),
    DESIGNER("Designer"),
    ENGINEER("Engineer");

    String job;

    @JsonValue // 직렬화 시 반환할 필드 지정
    public String getJob() {
        return this.job;
    }

    @JsonCreator // 역직렬화 시 유저로부터 받은 값 기반으로 Enum 선택
    public static JobType toJobType(String job) {
        for (JobType jobType : JobType.values()) {
            if (jobType.getJob().equals(job)) {
                return jobType;
            }
        }
        throw new RuntimeException("일치하는 타입이 존재하지 않습니다.");
    }
}
