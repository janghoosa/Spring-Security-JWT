package com.example.springsecurityjwt.member.dto.Request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateDto {
  @NotNull
  private String username;
  @NotNull
  private String password;
}
