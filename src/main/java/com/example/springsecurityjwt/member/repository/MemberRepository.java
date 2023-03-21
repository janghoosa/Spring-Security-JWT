package com.example.springsecurityjwt.member.repository;

import com.example.springsecurityjwt.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
}
