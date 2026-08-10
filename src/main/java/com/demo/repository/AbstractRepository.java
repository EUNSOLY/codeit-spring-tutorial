package com.demo.repository;

import com.demo.domain.common.BaseEntity;

import java.util.*;

public abstract class AbstractRepository<ENTITY extends BaseEntity> implements IRepository<Integer, ENTITY> {
    private final Map<Integer, ENTITY> database = new HashMap<>();

    // R : 전체 조회
    @Override
    public List<ENTITY> findAll() {
        return database.values().stream().toList();
    }

    // R : 단일 조회
    @Override
    public Optional<ENTITY> findById(Integer id) {
        return Optional.ofNullable(database.get(id));
    }

    // C : 단일 생성
    @Override
    public Optional<ENTITY> create(ENTITY entity) {
        int id = entity.getId();
        if (Objects.nonNull(database.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 엔티티가 이미 존재합니다 - id : " + id);
        }

        ENTITY created = database.put(id, entity);
        return Optional.ofNullable(created);
    }

    // U : 댠일 갱신
    @Override
    public Optional<ENTITY> update(ENTITY entity) {
        int id = entity.getId();
        if (Objects.isNull(database.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 엔티티가 존재하지 않습니다 - id : " + id);
        }
        ENTITY updated = database.replace(id, entity);
        return Optional.ofNullable(updated);

    }

    // D : 단일 삭제
    @Override
    public void delete(Integer id) {
        if (Objects.isNull(database.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 엔티티가 존재하지 않습니다 - id : " + id);
        }
        database.remove(id);
    }
}
