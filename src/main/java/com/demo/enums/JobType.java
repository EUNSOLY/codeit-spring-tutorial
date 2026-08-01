package com.demo.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum JobType {
    DEVELOPER("Developer"),
    DESIGNER("Designer"),
    ENGINEER("Engineer");

    String jobType;
    
    public static JobType of(String jobType) {
        for (JobType job : JobType.values()) {
            if (job.getJobType().equals(jobType)) {
                return job;
            }
        }
        throw new RuntimeException("요청하신 JobType이 존재하지않습니다.");
    }
}
