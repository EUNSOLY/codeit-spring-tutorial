package com.demo.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum JobType {
    TEST("일단 테스트");

    String job;


    public static JobType toJobType(String job) {
        for (JobType jobType : JobType.values()) {
            if (jobType.getJob().equals(job)) {
                return jobType;
            }
        }
        throw new RuntimeException("일치하는 타입이 존재하지 않습니다.");
    }
}
