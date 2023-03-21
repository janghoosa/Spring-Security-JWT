package com.example.springsecurityjwt.member.controller;

import com.example.springsecurityjwt.member.entity.Member;
import com.example.springsecurityjwt.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("")
    public List<Member> getMemberList(Pageable pageable) {
        return memberService.getMemberList(pageable);
    }
}
