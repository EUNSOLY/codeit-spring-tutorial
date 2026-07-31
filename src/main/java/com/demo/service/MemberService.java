package com.demo.service;

import com.demo.dto.MemberCreateRequestDto;
import com.demo.dto.MemberResponseDto;
import com.demo.dto.MemberUpdateRequestDto;
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


    public List<MemberResponseDto> createAll(List<MemberCreateRequestDto> requests) {
        List<MemberResponseDto> memberResponse = new ArrayList<>();
        List<Member> members = requests.stream().map(MemberCreateRequestDto::toEntity).toList();
        for (Member member : members) {
            Member savedMember = memberRepository.create(member);
            memberResponse.add(MemberResponseDto.from(savedMember));
        }
        return memberResponse;
    }


    public MemberResponseDto create(MemberCreateRequestDto request) {
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

    public MemberResponseDto update(MemberUpdateRequestDto request) {
        Member updateMember = request.toEntity();
        Member savedMember = memberRepository.update(updateMember);
        return MemberResponseDto.from(savedMember);
    }

    public void delete(Integer id) {
        memberRepository.delete(id);
    }
}
