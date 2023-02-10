package callbusLab.zaritalk.Assignment.domain.board.service;

import callbusLab.zaritalk.Assignment.domain.board.dto.BoardDto;
import callbusLab.zaritalk.Assignment.domain.board.dto.LikesDto;
import callbusLab.zaritalk.Assignment.domain.board.entity.Board;
import callbusLab.zaritalk.Assignment.domain.board.entity.Likes;
import callbusLab.zaritalk.Assignment.domain.board.repository.BoardRepository;
import callbusLab.zaritalk.Assignment.domain.board.repository.LikesRepository;
import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import callbusLab.zaritalk.Assignment.domain.user.repository.UserRepository;
import callbusLab.zaritalk.Assignment.global.config.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    // method
    private void likeAllSaveBoard(
            Board board, Long postLikeAll
    ) {
        boardRepository.save(
                Board.builder()
                        .id(board.getId())
                        .user(board.getUser())
                        .title(board.getTitle())
                        .bName(board.getBName())
                        .note(board.getNote())
                        .bImg(board.getBImg())
                        .likeAll(postLikeAll)
                        .createAt(board.getCreateAt())
                        .updateAt(board.getUpdateAt())
                        .build()
        );
    }

    private Board verificationOptionalBoard(
            LikesDto.addDto request
    ) {
        Optional<Board> byId = boardRepository.findById(request.getId());
        if (!byId.isPresent()) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
        return byId.get();
    }

    private User getUserInfo() {
        Optional<User> byUser = userRepository.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName()
        );
        if (!byUser.isPresent()) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }

        return byUser.get();
    }

    private Page<BoardDto.PostsListDto> PostsListDto(
            Integer page, Integer limit, Sort.Direction asc, String filter, String memberCheck
    ) {
        Pageable pageWithTenElements = PageRequest.of(page - 1, limit, asc, filter);
        Page<Board> list = boardRepository.findAll(pageWithTenElements);
        Page<BoardDto.PostsListDto> collect;

        if (memberCheck.equals("anonymousUser")) {
            collect = list.map((Board board) ->
                    BoardDto.PostsListDto.response(board, false));
        } else {
            collect = list.map(
                    (Board board) -> BoardDto.PostsListDto.response(
                            board,
                            likesRepository
                                    .existsByBoardIdAndUserId(board.getId(), getUserInfo().getId())
                    )
            );
        }
        return collect;

    }
    // Service

    @Transactional
    public ResponseEntity<BoardDto.CreateDto> addBoard(
            BoardDto.CreateDto request
    ) {
        User user = getUserInfo();

        return new ResponseEntity<>(
                BoardDto.CreateDto.response(
                        boardRepository.save(
                                Board.builder()
                                        .user(user)
                                        .title(request.getTitle())
                                        .bName(user.getNickname())
                                        .note(request.getNote())
                                        .bImg(request.getBImg())
                                        .likeAll(0L)
                                        .createAt(LocalDateTime.now().withNano(0))
                                        .updateAt(LocalDateTime.now().withNano(0))
                                        .build())
                ), HttpStatus.CREATED
        );
    }
    // 클라이언트가 요청한 페이지 / 페이지 안의 목록 수 / 어느 순으로 정렬할지 / 오름차순 or 내림차순

    @Transactional
    public ResponseEntity<Page<BoardDto.PostsListDto>> findListBoard(
            Integer page, Integer limit, String filter, String arrange
    ) {
        String memberCheck = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<BoardDto.PostsListDto> collect;
        if (arrange.equals("ASC")) {
            collect = PostsListDto(page, limit, Sort.Direction.ASC, filter, memberCheck);
        } else {
            collect = PostsListDto(page, limit, Sort.Direction.DESC, filter, memberCheck);
        }
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<LikesDto.addDto> saveLike(
            LikesDto.addDto request
    ) {
        boolean existsLike = likesRepository.existsByBoardIdAndUserId(
                request.getId(), getUserInfo().getId());
        Board board = verificationOptionalBoard(request);

        // 좋아요가 눌려있는지 여부
        if (!existsLike) {
            likesRepository.save(
                    Likes.builder()
                            .board(board)
                            .user(getUserInfo())
                            .build()
            );
            likeAllSaveBoard(board, board.getLikeAll() + 1);
            return new ResponseEntity<>(
                    LikesDto.addDto.response(
                            "ADD_LIKE_SUCCESS"
                    ), HttpStatus.CREATED);
        } else {
            likesRepository.deleteByBoardIdAndUserId(request.getId(), getUserInfo().getId());
            likeAllSaveBoard(board, board.getLikeAll() - 1);
            return new ResponseEntity<>(
                    LikesDto.addDto.response(
                            "DELETE_LIKE_SUCCESS"
                    ), HttpStatus.OK);
        }
    }
}
