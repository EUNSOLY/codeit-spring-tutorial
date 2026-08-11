package com.example.demo.domain.common;

import com.example.demo.common.context.UserContext;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * BaseEntity
 * : 세상에 존재하는 모든 엔티티들은 공통적으로 아래의 필드를 갖는다
 * - id        : 고유값
 * - deleted   : 삭제 여부
 * - createdAt : 언제 '생성'되었는가?
 * - createdBy : 누가 '생성'하였는가?
 * - updatedAt : 언제 '갱신'되었는가?
 * - updatedBy : 누가 '갱신'하였는가?
 */

// public class BaseEntity { -> BaseEntity 는 개별적인 객체로 생성(new BaseEntity(...))되어서는 안됨! 단순히 템플릿 클래스로의 역할만 수행하도록
@Getter
@ToString
public abstract class BaseEntity {
    protected Integer id;
    protected boolean deleted = false;

    // Audit 필드 : 데이터의 생성과 수정이 누구로 인해 언제 이뤄졌는지를 기록
    // 누가 / 언제를 추적할 수 있도록
    protected LocalDateTime createdAt;
    protected Integer createdBy;
    protected LocalDateTime updatedAt;
    protected Integer updatedBy;

    protected BaseEntity(Integer id, Integer userId) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.createdBy = userId;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    /**
     * BaseEntity (템플릿) 추상클래스 상속받는 엔티티 내 필드들이 수정되었을때 누가, 언제 바꿨는지 기록
     * - 중요 ! 이번 예시에서는 필드가 갱신되는 엔티티는 Payment 하나에서만 발생하는것으로 진행할 것 !
     * - currentUserId 어떤 유저가 값을 바꿨는지 추적하기 위함 <- Auditing
     */
    protected void updated() {
        /* 누가 구매를 하였는지 */
        Integer currentUserId = Optional.ofNullable(UserContext.getUserId()).orElse(this.createdBy);
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = currentUserId;
    }

    public void delete() {
        this.deleted = true;
        this.updated();
    }

    public void active() {
        this.deleted = false;
        this.updated();
    }
}
