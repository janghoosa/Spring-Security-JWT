package com.example.springsecurityjwt.member.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MemberDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
