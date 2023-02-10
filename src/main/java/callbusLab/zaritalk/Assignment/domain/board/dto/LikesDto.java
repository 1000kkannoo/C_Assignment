package callbusLab.zaritalk.Assignment.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

public class LikesDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class addDto {
        private Long id;
        private String status;

        public static LikesDto.addDto response(String status) {
            return addDto.builder()
                    .status(status)
                    .build();
        }
    }
}
