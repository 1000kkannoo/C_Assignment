package callbusLab.zaritalk.Assignment.domain.board.dto;

import callbusLab.zaritalk.Assignment.domain.board.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

public class BoardDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class CreateDto {
        private Long id;
        private Long userId;
        private String bName;
        private String title;
        private String note;
        private String bImg;
        private Long likeAll;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
        private LocalDateTime deleteAt;

        public static BoardDto.CreateDto response(Board board) {
            return CreateDto.builder()
                    .id(board.getId())
                    .likeAll(board.getLikeAll())
                    .userId(board.getUser().getId())
                    .bName(board.getBName())
                    .title(board.getTitle())
                    .note(board.getNote())
                    .bImg(board.getBImg())
                    .createAt(board.getCreateAt())
                    .deleteAt(board.getDeleteAt())
                    .updateAt(board.getUpdateAt())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class PostsListDto {
        private Long id;
        private Long userId;
        private String bName;
        private String title;
        private String note;
        private String bImg;
        private Long likeAll;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
        private LocalDateTime deleteAt;
        private Boolean likeAdd;

        public static BoardDto.PostsListDto response(Board board, Boolean likeAdd) {
            String accountType = board.getUser().getAccountType();
            switch (accountType) {
                case "LESSOR":
                    accountType = "(임대인)";
                    break;
                case "LESSEE":
                    accountType = "(임차인)";
                    break;
                default:
                    accountType = "(공인중개사)";
                    break;
            }

            return PostsListDto.builder()
                    .id(board.getId())
                    .likeAdd(likeAdd)
                    .userId(board.getUser().getId())
                    .bName(board.getBName() + accountType)
                    .title(board.getTitle())
                    .note(board.getNote())
                    .bImg(board.getBImg())
                    .likeAll(board.getLikeAll())
                    .createAt(board.getCreateAt())
                    .deleteAt(board.getDeleteAt())
                    .updateAt(board.getUpdateAt())
                    .build();
        }
    }

}
