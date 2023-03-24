package com.example.springsecurityjwt.member.service;

import com.example.springsecurityjwt.member.dto.Request.MemberCreateDto;
import com.example.springsecurityjwt.member.dto.Response.MemberResponseDto;
import com.example.springsecurityjwt.member.entity.Member;
import com.example.springsecurityjwt.member.entity.Member.Role;
import com.example.springsecurityjwt.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper mapper;

  public MemberResponseDto createMember(MemberCreateDto dto) {
    Member newMember = Member.builder()
        .id(null)
        .username(dto.getUsername())
        .password(passwordEncoder.encode(dto.getPassword()))
        .role(Role.valueOf("ROLE_USER"))
        .build();
    return mapper.map(memberRepository.save(newMember), MemberResponseDto.class);
  }

  public List<Member> getMemberList(Pageable pageable) {
    return memberRepository.findAll(pageable)
        .stream()
        .collect(Collectors.toList());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByUsername(username);
    if (member == null) {
      throw new UsernameNotFoundException("Invalid username");
    }
    return new User(member.getUsername(), member.getPassword(), member.getAuthorities());
  }
}