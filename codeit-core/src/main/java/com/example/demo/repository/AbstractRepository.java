package com.example.demo.repository;

import com.example.demo.domain.common.BaseEntity;

import java.util.*;

public abstract class AbstractRepository<ENTITY extends BaseEntity> implements IRepository<Integer, ENTITY> {
    private final /* static */ Map<Integer, ENTITY> database = new HashMap<>();

    @Override
    // R 전체 조회
    public List<ENTITY> findAll() {
        return this.database.values().stream().toList();
    }

    @Override
    // R 단일 조회
    public Optional<ENTITY> findById(Integer id) {
        return Optional.ofNullable(this.database.get(id));
    }

    @Override
    // C 단일 생성
    public Optional<ENTITY> create(ENTITY entity) {
        int id = entity.getId();
        if (Objects.nonNull(this.database.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 엔티티가 이미 존재합니다 - id : " + id);
        }
        ENTITY created = this.database.put(id, entity);
        return Optional.ofNullable(created);
    }

    @Override
    // U 단일 갱신
    public Optional<ENTITY> update(ENTITY entity) {
        int id = entity.getId();
        if (Objects.isNull(this.database.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 엔티티가 존재하지 않습니다 - id : " + id);
        }
        ENTITY updated = this.database.replace(id, entity);
        return Optional.ofNullable(updated);
    }

    @Override
    // D 단일 삭제
    public void delete(Integer id) {
        if (Objects.isNull(this.database.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 엔티티가 존재하지 않습니다 - id : " + id);
        }
        this.database.remove(id);
    }
}
