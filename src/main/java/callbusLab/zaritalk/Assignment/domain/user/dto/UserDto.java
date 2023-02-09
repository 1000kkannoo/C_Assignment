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
        private String nickname;
        private String email;
        private String pw;
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
        @NotNull
        private String atk;
        @NotNull
        private String email;
        @NotNull
        private String nickname;
        @NotNull
        private String accountType;
        @NotNull
        private Boolean quit;
        @NotNull
        private String pw;

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
