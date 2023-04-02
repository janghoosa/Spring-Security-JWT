package com.example.springsecurityjwt.common.config.jwt;

import com.example.springsecurityjwt.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private JwtTokenUtil jwtTokenUtil;
  private MemberService memberService;

  public JwtAuthenticationFilter(
      JwtTokenUtil jwtTokenUtil,
      MemberService memberService) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.memberService = memberService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    String username = null;
    String authToken = null;
    if (header != null && header.startsWith("Bearer ")) {
      authToken = header.substring(7);
      try {
        username = jwtTokenUtil.getUsernameFromToken(authToken);
      } catch (IllegalArgumentException e) {
        logger.error("An error occurred while getting username from token", e);
      } catch (ExpiredJwtException e) {
        logger.warn("The token is expired and not valid anymore", e);
      }
    } else {
      logger.warn("Couldn't find bearer string, will ignore the header");
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = memberService.loadUserByUsername(username);
      if (jwtTokenUtil.validateToken(authToken, userDetails)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        logger.info("Authenticated user " + username + ", setting security context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    chain.doFilter(request, response);
  }
}