package callbusLab.zaritalk.Assignment.domain.user.dto;

import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDto implements Serializable {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class registerDto {
        private Long id;
        private String nickname;
        private String email;
        private String pw;
        private String accountType;
        private Boolean quit;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    public static class loginDto {
        @NotNull private String atk;
        @NotNull private String email;
        @NotNull private String nickname;
        @NotNull private String accountType;
        @NotNull private Boolean quit;
        @NotNull private String pw;
        public static loginDto response(User user, String atk) {
            return loginDto.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .accountType(user.getAccountType())
                    .quit(user.getQuit())
                    .atk(atk)
                    .build();
        }
    }
}
