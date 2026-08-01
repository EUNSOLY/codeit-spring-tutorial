package com.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT) 객체 형태로 직렬화 하고싶다면 사용
//@JsonValue + @JsonCreator 세트세트 : 이 두가지를 써서 value 값으로 맵핑해서 반환받느냐, JsonFormat를 사용해서 {} 객체 형태로 전체를 반환받느냐
public enum JobType {
    DEVELOPER("Developer"),
    DESIGNER("Designer"),
    ENGINEER("Engineer");

    String jobType;

    @JsonValue
    public String getJobType() {
        return this.jobType;
    }

    @JsonCreator //
    public static JobType of(String jobType) {
        for (JobType job : JobType.values()) {
            if (job.getJobType().equals(jobType)) {
                return job;
            }
        }
        throw new RuntimeException("요청하신 JobType이 존재하지않습니다.");
    }
}
