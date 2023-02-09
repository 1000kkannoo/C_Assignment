package callbusLab.zaritalk.Assignment.domain.user.controller;

import callbusLab.zaritalk.Assignment.domain.user.dto.UserDto;
import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import callbusLab.zaritalk.Assignment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("auth")
    public ResponseEntity<UserDto.RegisterDto> register(
            @RequestBody UserDto.RegisterDto request
    ) {
        return userService.register(request);
    }

    // 로그인
    @PostMapping("auth/login")
    public ResponseEntity<UserDto.LoginDto> login(
            @RequestBody UserDto.LoginDto request
    ) {
        return userService.login(request);
    }
}

