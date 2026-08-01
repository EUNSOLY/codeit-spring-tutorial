package com.demo.service;

import com.demo.dto.MemberCreateRequestDto;
import com.demo.dto.MemberPutRequestDto;
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
        for (MemberCreateRequestDto request : requests) {
            Member newMember = request.toEntity();
            Member savedMember = memberRepository.create(newMember);
            memberResponse.add(MemberResponseDto.to(savedMember));
        }

        return memberResponse;
    }


    public MemberResponseDto create(MemberCreateRequestDto request) {
        Member newMember = request.toEntity();
        Member savedMember = memberRepository.create(newMember);

        return MemberResponseDto.to(savedMember);
    }

    public List<MemberResponseDto> readAll() {
        List<Member> members = memberRepository.readAll();

        return members.stream().map(MemberResponseDto::to).toList();
    }

    public MemberResponseDto read(Integer id) {
        Member member = memberRepository.read(id);
        return MemberResponseDto.to(member);
    }

    public MemberResponseDto update(Integer id, MemberUpdateRequestDto request) {
        Member originMember = memberRepository.read(id);
        originMember.updateMember(request.getName(), request.getAge(), request.getJob(), request.getEmail());
        memberRepository.update(originMember);

        return MemberResponseDto.to(originMember);
    }

    public MemberResponseDto update(Integer id, MemberPutRequestDto request) {
        Member originMember = memberRepository.read(id);
        originMember.updateMember(request.getName(), request.getAge(), request.getJob(), request.getEmail());
        memberRepository.update(originMember);

        return MemberResponseDto.to(originMember);
    }

    public MemberResponseDto delete(Integer id) {
        Member deleteMember = memberRepository.delete(id);
        return MemberResponseDto.to(deleteMember);
    }
}
