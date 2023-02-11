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
                        .bName(board.getBName())
                        .title(board.getTitle())
                        .note(board.getNote())
                        .bImg(board.getBImg())
                        .likeAll(postLikeAll)
                        .createAt(board.getCreateAt())
                        .updateAt(board.getUpdateAt())
                        .build()
        );
    }

    private Board getBoardInfo(
            Long boardId
    ) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(INTERNAL_SERVER_ERROR)
        );
    }

    private User getUserInfo() {
        return userRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        ).orElseThrow(
                () -> new CustomException(INTERNAL_SERVER_ERROR)
        );
    }

    private Page<BoardDto.PostsListDto> PostsListDto(
            Integer page, Integer limit, Sort.Direction asc, String filter, String existsMember
    ) {
        Pageable pageWithTenElements = PageRequest.of(page - 1, limit, asc, filter);
        Page<Board> list = boardRepository.findAll(pageWithTenElements);
        Page<BoardDto.PostsListDto> collect;

        if (existsMember.equals("anonymousUser")) {
            collect = list.map((Board board) ->
                    BoardDto.PostsListDto.response(board, false));
        } else {
            collect = list.map(
                    (Board board) -> BoardDto.PostsListDto.response(
                            board, likesRepository
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
                                        .bName(user.getNickname())
                                        .title(request.getTitle())
                                        .note(request.getNote())
                                        .bImg(request.getBImg())
                                        .likeAll(0L)
                                        .createAt(LocalDateTime.now().withNano(0))
                                        .updateAt(LocalDateTime.now().withNano(0))
                                        .build())
                ), HttpStatus.CREATED
        );
    }

    // 클라이언트가 요청한 글 목록 / 페이지 안의 목록 수 / 어느 순으로 정렬할지 / 오름차순 or 내림차순
    @Transactional
    public ResponseEntity<Page<BoardDto.PostsListDto>> findListBoard(
            Integer page, Integer limit, String filter, String arrange
    ) {
        String existsMember = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<BoardDto.PostsListDto> collect;

        if (arrange.equals("ASC")) {
            collect = PostsListDto(page, limit, Sort.Direction.ASC, filter, existsMember);
        } else {
            collect = PostsListDto(page, limit, Sort.Direction.DESC, filter, existsMember);
        }
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<LikesDto.addDto> saveLike(
            LikesDto.addDto request
    ) {
        boolean existsLike = likesRepository.existsByBoardIdAndUserId(
                request.getId(), getUserInfo().getId());
        Board board = getBoardInfo(request.getId());
        User user = getUserInfo();

        // 좋아요가 눌려있는지 여부
        // 리팩터링 가능해보임
        if (!existsLike) {
            likesRepository.save(
                    Likes.builder()
                            .board(board)
                            .user(user)
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

    @Transactional
    public ResponseEntity<BoardDto.DeleteDto> deleteBoard(BoardDto.DeleteDto request) {
        boardRepository.deleteByIdAndUserId(request.getId(), getUserInfo().getId());
        return new ResponseEntity<>(
                BoardDto.DeleteDto.response(
                        request.getId(), "DELETE_BOARD_TRUE"
                ), HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<BoardDto.UpdateDto> updateBoard(BoardDto.UpdateDto request) {
        Board board = getBoardInfo(request.getId());
        return new ResponseEntity<>(
                BoardDto.UpdateDto.response(
                        boardRepository.save(
                                Board.builder()
                                        .id(board.getId())
                                        .user(board.getUser())
                                        .bName(board.getBName())
                                        .title(request.getTitle())
                                        .note(request.getNote())
                                        .bImg(request.getBImg())
                                        .likeAll(board.getLikeAll())
                                        .createAt(board.getCreateAt())
                                        .updateAt(LocalDateTime.now().withNano(0))
                                        .build())
                ), HttpStatus.OK
        );
    }
}
