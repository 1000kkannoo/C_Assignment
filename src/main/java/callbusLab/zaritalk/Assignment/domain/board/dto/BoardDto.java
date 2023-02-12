package callbusLab.zaritalk.Assignment.domain.board.dto;

import callbusLab.zaritalk.Assignment.domain.board.entity.Board;
import lombok.*;

import javax.validation.constraints.NotNull;
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
        private String boardName;
        @NotNull(message = "제목을 입력하지 않았습니다.")
        private String title;
        @NotNull(message = "내용을 입력하지 않았습니다.")
        private String note;
        private String boardImageUrl;
        private Long likeAll;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        public static BoardDto.CreateDto response(Board board) {
            return CreateDto.builder()
                    .id(board.getId())
                    .userId(board.getUser().getId())
                    .boardName(board.getBoardName())
                    .title(board.getTitle())
                    .note(board.getNote())
                    .boardImageUrl(board.getBoardImageUrl())
                    .likeAll(board.getLikeAll())
                    .createAt(board.getCreateAt())
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
        private String boardName;
        private String title;
        private String note;
        private String boardImageUrl;
        private Long likeAll;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
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
                    .userId(board.getUser().getId())
                    .boardName(board.getBoardName() + accountType)
                    .title(board.getTitle())
                    .note(board.getNote())
                    .boardImageUrl(board.getBoardImageUrl())
                    .likeAll(board.getLikeAll())
                    .createAt(board.getCreateAt())
                    .updateAt(board.getUpdateAt())
                    .likeAdd(likeAdd)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class DeleteDto {
        @NotNull(message = "게시글 id를 요청받지 못했습니다.")
        private Long id;
        private String status;

        public static BoardDto.DeleteDto response(Long id, String status) {
            return DeleteDto.builder()
                    .id(id)
                    .status(status)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class UpdateDto {
        @NotNull(message = "게시글 id를 요청받지 못했습니다.")
        private Long id;
        private Long userId;
        private String boardName;
        @NotNull(message = "제목을 입력하지 않았습니다.")
        private String title;
        @NotNull(message = "내용을 입력하지 않았습니다.")
        private String note;
        private String boardImageUrl;
        private Long likeAll;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        public static BoardDto.UpdateDto response(Board board) {
            return UpdateDto.builder()
                    .id(board.getId())
                    .likeAll(board.getLikeAll())
                    .userId(board.getUser().getId())
                    .boardName(board.getBoardName())
                    .title(board.getTitle())
                    .note(board.getNote())
                    .boardImageUrl(board.getBoardImageUrl())
                    .createAt(board.getCreateAt())
                    .updateAt(board.getUpdateAt())
                    .build();
        }
    }

}
