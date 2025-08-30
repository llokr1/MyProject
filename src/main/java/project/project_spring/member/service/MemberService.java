package project.project_spring.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.project_spring.common.exception.GeneralException;
import project.project_spring.common.response.ErrorCode;
import project.project_spring.member.domain.Member;
import project.project_spring.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        return member;
    }

    public Member findMemberByEmail(String email){

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        return member;
    }

    public Member saveMember(Member member){

        return memberRepository.save(member);

    }
}
