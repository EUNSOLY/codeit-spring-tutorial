package com.demo.repository;

import com.demo.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class MemberRepository extends IdGenerator implements IRepository {
    private static final Map<Integer, Member> STORAGE = new HashMap<>();

    @Override
    public Member create(Member entity) {
        Integer id = super.increase();
        Member newMember = new Member(id, entity.getName(), entity.getAge(), entity.getJob().getJobType(), entity.getEmail());
        STORAGE.put(id, newMember);

        return newMember;
    }

    @Override
    public List<Member> readAll() {
        return STORAGE.values().stream().toList();
    }

    @Override
    public Member read(Integer id) {
        Member member = STORAGE.get(id);
        if (Objects.isNull(member)) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        return STORAGE.get(id);
    }

    @Override
    public Member update(Member entity) {
        Member member = STORAGE.get(entity.getId());
        if (Objects.isNull(member)) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        return STORAGE.replace(entity.getId(), entity);
    }

    @Override
    public void delete(Integer id) {
        Member member = STORAGE.get(id);
        if (Objects.isNull(member)) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        STORAGE.remove(id);
    }
}
