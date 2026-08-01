package com.demo.repository;

import com.demo.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemberRepository extends IDAbstractGenerator implements IRepository<Integer, Member> {
    private static final Map<Integer, Member> DATABASE = new HashMap<>();

    @Override
    public Member create(Member entity) {
        Integer id = super.nextId();
        Member newEntity = new Member(id, entity.getName(), entity.getAge(), entity.getJob(), entity.getEmail());
        DATABASE.put(id, newEntity);
        return DATABASE.get(id);
    }

    @Override
    public List<Member> readAll() {
        return DATABASE.values().stream().filter(member -> !member.isDeleted()).toList();
    }

    @Override
    public Member read(Integer id) {
        return Optional.of(DATABASE.get(id))
                .filter(member -> !member.isDeleted())
                .orElseThrow(() -> new RuntimeException("찾으시는 회원이 존재하지 않습니다."));
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
//        this.previousId();
    }

}
