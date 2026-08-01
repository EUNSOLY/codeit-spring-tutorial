package com.demo.dto;


import com.demo.entity.Member;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MemberCreateRequestDto {
    @NotBlank
    String name;
    @NotNull
    Integer age;
    @Nullable
    String job;
    @Nullable
    String email;


    public Member toEntity() {
        return new Member(0, this.name, this.age, this.job, this.email);
    }
}
