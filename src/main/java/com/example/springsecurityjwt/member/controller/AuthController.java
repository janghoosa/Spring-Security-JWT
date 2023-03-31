package com.example.springsecurityjwt.member.controller;

import com.example.springsecurityjwt.common.config.jwt.JwtTokenUtil;
import com.example.springsecurityjwt.common.response.JwtResponse;
import com.example.springsecurityjwt.common.request.LoginRequest;
import com.example.springsecurityjwt.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final MemberService memberService;

  public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
      MemberService memberService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.memberService = memberService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
    // 사용자 인증 수행
    log.info(loginRequest.getUsername());
    log.info(loginRequest.getPassword());

    authenticate(loginRequest.getUsername(), loginRequest.getPassword());

    // 사용자 정보 조회
    UserDetails user = memberService.loadUserByUsername(loginRequest.getUsername());

    // JWT 토큰 생성
    String token = jwtTokenUtil.generateToken(user);

    // JWT 토큰 반환
    return ResponseEntity.ok(new JwtResponse(token));
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}
