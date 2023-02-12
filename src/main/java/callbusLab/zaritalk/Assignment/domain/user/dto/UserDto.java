package callbusLab.zaritalk.Assignment.domain.user.dto;

import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDto implements Serializable {

    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Builder
    public static class RegisterDto {
        private Long id;
        @NotNull(message = "닉네임이 입력되지 않았습니다.")
        private String nickname;
        @NotNull(message = "이메일이 입력되지 않았습니다.")
        private String email;
        @NotNull(message = "비밀번호가 입력되지 않았습니다.")
        private String pw;
        @NotNull(message = "계정 유형이 등록되지 않았습니다.")
        private String accountType;
        private Boolean quit;

        public static RegisterDto response(User user) {
            return RegisterDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .accountType(user.getAccountType())
                    .quit(user.getQuit())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    public static class LoginDto {
        private String nickname;
        @NotNull(message = "이메일이 입력되지 않았습니다.")
        private String email;
        @NotNull(message = "비밀번호가 입력되지 않았습니다.")
        private String pw;
        private String accountType;
        private Boolean quit;
        private String atk;

        public static LoginDto response(User user, String atk) {
            return LoginDto.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .accountType(user.getAccountType())
                    .quit(user.getQuit())
                    .atk(atk)
                    .build();
        }
    }
}
