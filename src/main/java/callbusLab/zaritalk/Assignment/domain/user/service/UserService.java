package callbusLab.zaritalk.Assignment.domain.user.service;

import callbusLab.zaritalk.Assignment.domain.user.dto.UserDto;
import callbusLab.zaritalk.Assignment.domain.user.entity.Authority;
import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import callbusLab.zaritalk.Assignment.domain.user.repository.UserRepository;
import callbusLab.zaritalk.Assignment.global.config.exception.CustomException;
import callbusLab.zaritalk.Assignment.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    // Service
    // 회원가입
    @Transactional
    public ResponseEntity<UserDto.RegisterDto> register(UserDto.RegisterDto request) {

        Authority authority = Authority.builder()
                .authorityName("ROLE_" + request.getAccountType())
                .build();

        return new ResponseEntity<>(UserDto.RegisterDto.response(
                userRepository.save(
                        registerUserFromRequest(request, authority)
                )
        ), HttpStatus.CREATED);
    }

    //로그인
    @Transactional
    public ResponseEntity<UserDto.LoginDto> login(UserDto.LoginDto request) {
        validateLogin(request);

        Authentication authentication = getAuthentication(request);

        return new ResponseEntity<>(UserDto.LoginDto.response(
                getUserInfo(authentication),
                tokenProvider.createToken(authentication)
        ), HttpStatus.OK);
    }

    // Validate
    private void validateLogin(UserDto.LoginDto request) {
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new CustomException(NOT_EXISTS_EMAIL)
                );

        if (!passwordEncoder.matches(
                request.getPw(),
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(
                                () -> new CustomException(NOT_MATCHED_ID_OR_PASSWORD)
                        ).getPw())
        ) {
            throw new CustomException(NOT_MATCHED_ID_OR_PASSWORD);
        }
    }

    // Method
    private User getUserInfo(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(
                        () -> new CustomException(INTERNAL_SERVER_ERROR)
                );
    }

    private Authentication getAuthentication(UserDto.LoginDto request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPw());
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private User registerUserFromRequest(UserDto.RegisterDto request, Authority authority) {
        return User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .pw(passwordEncoder.encode(request.getPw()))
                .accountType(request.getAccountType())
                .quit(false)
                .authorities(Collections.singleton(authority))
                .build();
    }


}
