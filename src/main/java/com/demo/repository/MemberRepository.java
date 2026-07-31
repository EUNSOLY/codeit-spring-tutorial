package com.demo.repository;

import com.demo.entity.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MemberRepository implements IRepository, IDGenerator {
    private static final Map<Integer, Member> DATABASE = new HashMap<>();
    private static Integer ID = 0;


    @Override
    public Member create(Member entity) {
        DATABASE.put(entity.getId(), entity);
        this.nextId();
        return null;
    }

    @Override
    public List<Member> readAll() {
        return DATABASE.values().stream().toList();
    }

    @Override
    public Member read(Integer id) {
        Member member = DATABASE.get(id);
        if (Objects.isNull(member)) {
            throw new RuntimeException("찾으시는 회원이 존재하지 않습니다.");
        }
        return member;
    }

    @Override
    public Member update(Member entity) {
        Member member = DATABASE.get(entity.getId());
        if (Objects.isNull(member)) {
            throw new RuntimeException("수정을 원하는 회원이 존재하지 않습니다.");
        }
        DATABASE.put(entity.getId(), entity);
        return this.read(entity.getId());
    }

    @Override
    public void delete(Integer id) {
        Member member = DATABASE.get(id);
        if (Objects.isNull(member)) {
            throw new RuntimeException("삭제를 원하는 회원이 존재하지 않습니다.");
        }
        DATABASE.remove(id);
        this.previousId();
    }

    @Override
    public Integer nextId() {
        return ID++;
    }

    @Override
    public Integer previousId() {
        return ID--;
    }
}
