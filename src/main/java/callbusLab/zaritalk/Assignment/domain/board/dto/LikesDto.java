package callbusLab.zaritalk.Assignment.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;

public class LikesDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class addDto {
        @NotNull(message = "게시글 id를 요청받지 못했습니다.")
        private Long id;
        private String status;

        public static LikesDto.addDto response(String status) {
            return addDto.builder()
                    .status(status)
                    .build();
        }
    }
}
