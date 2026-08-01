package com.demo.dto;


import com.demo.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MemberPutRequestDto {
    @NotBlank
    String name;
    @NotNull
    Integer age;
    @NotBlank
    String job;
    @NotBlank
    String email;


    public Member toEntity() {
        return new Member(0, this.name, this.age, this.job, this.email);
    }
}
