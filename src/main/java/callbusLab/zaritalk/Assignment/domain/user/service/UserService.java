package callbusLab.zaritalk.Assignment.domain.user.service;

import callbusLab.zaritalk.Assignment.domain.user.dto.UserDto;
import callbusLab.zaritalk.Assignment.domain.user.entity.Authority;
import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import callbusLab.zaritalk.Assignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Service
    // 회원가입
    @Transactional
    public ResponseEntity<User> register(UserDto.register request, HttpServletResponse response) {

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

}
