package com.example.springsecurityjwt.member.service;

import com.example.springsecurityjwt.member.entity.Member;
import com.example.springsecurityjwt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService /*implements UserDetailsService*/ {
    private final MemberRepository memberRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Member member = memberRepository.findByUsername(username);
//        if (member == null){
//            throw new UsernameNotFoundException("Invalid username");
//        }
//        return new User(member.getUsername(), member.getPassword(), member.getAuthorities());
//    }
}