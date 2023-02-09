package callbusLab.zaritalk.Assignment.domain.user.service;

import callbusLab.zaritalk.Assignment.domain.user.dto.UserDto;
import callbusLab.zaritalk.Assignment.domain.user.entity.Authority;
import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import callbusLab.zaritalk.Assignment.domain.user.repository.UserRepository;
import callbusLab.zaritalk.Assignment.global.config.exception.CustomException;
import callbusLab.zaritalk.Assignment.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.INTERNAL_SERVER_ERROR;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    // Validate 및 Method
    private User getUserInfo(Authentication authentication) {
        Optional<User> byUser = userRepository.findByEmail(authentication.getName());
        if (!byUser.isPresent()) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
        User user = byUser.get();
        return user;
    }

    private Authentication getAuthentication(UserDto.loginDto request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPw());
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    // Service
    // 회원가입
    @Transactional
    public ResponseEntity<User> register(UserDto.registerDto request) {

        Authority authority = Authority.builder()
                .authorityName("ROLE_" + request.getAccountType())
                .build();

        return new ResponseEntity<>(userRepository.save(
                User.builder()
                        .nickname(request.getNickname())
                        .email(request.getEmail())
                        .pw(passwordEncoder.encode(request.getPw()))
                        .accountType(request.getAccountType())
                        .authorities(Collections.singleton(authority))
                        .quit(true)
                        .build()
        ), HttpStatus.CREATED);
    }

    //로그인
    @Transactional
    public ResponseEntity<UserDto.loginDto> login(UserDto.loginDto request) {

        Authentication authentication = getAuthentication(request);

        return new ResponseEntity<>(UserDto.loginDto.response(
                getUserInfo(authentication),
                tokenProvider.createToken(authentication)
        ), HttpStatus.OK);
    }


}
