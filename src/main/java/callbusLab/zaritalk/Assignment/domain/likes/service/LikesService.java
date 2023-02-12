package callbusLab.zaritalk.Assignment.domain.likes.service;

import callbusLab.zaritalk.Assignment.domain.likes.dto.LikesDto;
import callbusLab.zaritalk.Assignment.domain.board.entity.Board;
import callbusLab.zaritalk.Assignment.domain.likes.entity.Likes;
import callbusLab.zaritalk.Assignment.domain.board.repository.BoardRepository;
import callbusLab.zaritalk.Assignment.domain.likes.repository.LikesRepository;
import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import callbusLab.zaritalk.Assignment.domain.user.repository.UserRepository;
import callbusLab.zaritalk.Assignment.global.config.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikesService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    //Service
    @Transactional
    public ResponseEntity<LikesDto.addDto> saveLike(
            LikesDto.addDto request
    ) {
        boolean existsLike = likesRepository.existsByBoardIdAndUserId(
                request.getId(), getUserInfo().getId()
        );
        Board board = getBoardInfo(request.getId());
        User user = getUserInfo();

        // 좋아요가 눌려있는지 여부
        if (!existsLike) {
            likesRepository.save(
                    addLikesFromRequest(board, user)
            );
            boardRepository.save(
                    saveLikesBoardFromRequest(
                            board, board.getLikeAll() + 1
                    )
            );
            return new ResponseEntity<>(LikesDto.addDto.response(
                    "ADD_LIKE_SUCCESS"
            ), HttpStatus.CREATED);
        } else {
            likesRepository.delete(
                    validateDeleteAndGetLikes(request)
            );
            boardRepository.save(
                    saveLikesBoardFromRequest(board, board.getLikeAll() - 1)
            );
            return new ResponseEntity<>(LikesDto.addDto.response(
                    "DELETE_LIKE_SUCCESS"
            ), HttpStatus.OK);
        }
    }

    // Validate
    private Likes validateDeleteAndGetLikes(LikesDto.addDto request) {
        Likes likes = likesRepository.findByBoardIdAndUserId(
                request.getId(), getUserInfo().getId()
                ).orElseThrow(
                        () -> new CustomException(NOT_ADD_LIKES)
                );
        return likes;
    }

    // Method
    private static Likes addLikesFromRequest(
            Board board, User user
    ) {
        return Likes.builder()
                .board(board)
                .user(user)
                .build();
    }

    private static Board saveLikesBoardFromRequest(
            Board board, Long likeAll
    ) {
        return Board.builder()
                .id(board.getId())
                .user(board.getUser())
                .boardName(board.getBoardName())
                .title(board.getTitle())
                .note(board.getNote())
                .boardImageUrl(board.getBoardImageUrl())
                .likeAll(likeAll)
                .createAt(board.getCreateAt())
                .updateAt(board.getUpdateAt())
                .build();
    }

    private Board getBoardInfo(
            Long boardId
    ) {
        return boardRepository.findById(
                boardId
        ).orElseThrow(
                () -> new CustomException(INTERNAL_SERVER_ERROR)
        );
    }

    private User getUserInfo() {
        return userRepository.findByEmail(
                SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        ).orElseThrow(
                () -> new CustomException(INTERNAL_SERVER_ERROR)
        );
    }
}
