package com.demo.service;

import com.demo.dto.MemberPutRequestDto;
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
        for (MemberUpsertRequestDto request : requests) {
            Member newMember = Member.toEntity(request);
            Member savedMember = memberRepository.create(newMember);
            memberResponse.add(MemberResponseDto.from(savedMember));
        }

        return memberResponse;
    }


    public MemberResponseDto create(MemberUpsertRequestDto request) {
        Member newMember = Member.toEntity(request);
        Member savedMember = memberRepository.create(newMember);

        return MemberResponseDto.from(savedMember);
    }

    public List<MemberResponseDto> readAll() {
        List<Member> members = memberRepository.readAll();

        return members.stream().map(MemberResponseDto::from).toList();
    }

    public MemberResponseDto read(Integer id) {
        return memberRepository.read(id).map(MemberResponseDto::from)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
    }

    public MemberResponseDto update(Integer id, MemberUpsertRequestDto request) {
        Member originMember = memberRepository.read(id)
                .map(member -> member.updateMember(request.getName(), request.getAge(), request.getJob(), request.getEmail()))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        memberRepository.update(originMember);

        return MemberResponseDto.from(originMember);
    }

    public MemberResponseDto update(Integer id, MemberPutRequestDto request) {
        Member originMember = memberRepository.read(id)
                .map(member -> member.updateMember(request.getName(), request.getAge(), request.getJob(), request.getEmail()))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        memberRepository.update(originMember);

        return MemberResponseDto.from(originMember);
    }

    public MemberResponseDto delete(Integer id) {
        Member deleteMember = memberRepository.delete(id).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        
        return MemberResponseDto.from(deleteMember);
//
    }
}
