package callbusLab.zaritalk.Assignment.domain.user.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDto implements Serializable {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class register {
        private Long id;
        private String nickname;
        private String email;
        private String pw;
        private String accountType;
        private Boolean quit;
    }
}
