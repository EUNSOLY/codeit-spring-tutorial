package com.example.demo.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message") // 동일
public class Message {
    /**
     * JPA 통한 Database 사용 시 @GeneratedValue 전략에 대해 조금 상세히 알 필요가 있다.
     * - AUTO     : ID 생성 책임이 JPA 에게 있다 (JPA 는 hibernate_sequence 라는 sequence 테이블을 만들어 활용, nextval 호출)
     * - IDENTITY : ID 생성 책임을 Database 에게 위임한다. (PostgresQL 은 Primary Key 에 대해 SERIAL 로 정의 및 DB 자체적으로 Sequence 생성)
     * > MySQL 라면 AUTO_INCREMENT 사용할것이고,
     * > PostgresQL 이라면 SERIAL + Sequence 사용 (sequence name 형식은 {tablename}_{columnname}_seq), currval 호출)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    private String message;
    /**
     * Hibernate 6+ (Spring Boot 3.x)에서 implicit naming strategy 변경으로
     * camelCase → snake_case 자동 변환이 보장되지 않아 명시적으로 컬럼명 지정
     */
    @Column(name = "created_at")
    private LocalDateTime createAt;
}
