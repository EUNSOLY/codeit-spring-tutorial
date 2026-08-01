package com.demo.service;

import com.demo.dto.MemberPatchRequestDto;
import com.demo.dto.MemberResponseDto;
import com.demo.dto.MemberUpsertRequestDto;
import com.demo.entity.Member;
import com.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    public List<MemberResponseDto> createAll(List<MemberUpsertRequestDto> requests) {
        List<MemberResponseDto> memberResponse = new ArrayList<>();
        List<Member> members = requests.stream().map(MemberUpsertRequestDto::toEntity).toList();
        for (Member member : members) {
            Member savedMember = memberRepository.create(member);
            memberResponse.add(MemberResponseDto.from(savedMember));
        }
        return memberResponse;
    }


    public MemberResponseDto create(MemberUpsertRequestDto request) {
        Member member = request.toEntity();
        Member savedMember = memberRepository.create(member);
        return MemberResponseDto.from(savedMember);
    }

    public List<MemberResponseDto> readAll() {
        return memberRepository.readAll()
                .stream()
                .map(MemberResponseDto::from)
                .toList();
    }

    public MemberResponseDto read(Integer id) {
        Member member = memberRepository.read(id);
        return MemberResponseDto.from(member);
    }

    // 부분 수정
    public MemberResponseDto update(Integer id, MemberPatchRequestDto patchRequestDto) {
        Member originMember = memberRepository.read(id);
        originMember.update(patchRequestDto.getName(), patchRequestDto.getAge(), patchRequestDto.getJob(), patchRequestDto.getEmail());
        System.out.println(originMember.getJob());
        Member newMember = memberRepository.update(originMember);
        return MemberResponseDto.from(newMember);
    }

    // 전체 수정
    public MemberResponseDto update(Integer id, MemberUpsertRequestDto request) {
        Member updateMember = request.toEntity(id);
        Member savedMember = memberRepository.update(updateMember);
        return MemberResponseDto.from(savedMember);
    }

    public void delete(Integer id) {
        memberRepository.delete(id);
    }
}
