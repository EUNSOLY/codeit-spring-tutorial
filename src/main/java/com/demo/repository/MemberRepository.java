package com.demo.repository;

import com.demo.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepository extends IdGenerator implements IRepository<Member, Integer> {
    private static final Map<Integer, Member> STORAGE = new HashMap<>();

    @Override
    public Member create(Member entity) {
        Integer id = super.increase();
        entity.assignId(id);
        STORAGE.put(id, entity);

        return entity;
    }

    @Override
    public List<Member> readAll() {
        return STORAGE.values().stream().filter(member -> !member.isDeleted()).toList();
    }

    @Override
    public Optional<Member> read(Integer id) {
        return Optional.ofNullable(STORAGE.get(id))
                .filter(member -> !member.isDeleted());

    }

    @Override
    public Optional<Member> update(Member entity) {
        return Optional.ofNullable(STORAGE.get(entity.getId()))
                .map(existing -> STORAGE.replace(entity.getId(), entity));
    }

    @Override
    public Optional<Member> delete(Integer id) {
        return Optional.ofNullable(STORAGE.get(id))
                .map(existing -> STORAGE.remove(id));
    }
}
