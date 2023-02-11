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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    // Service
    @Transactional
    public ResponseEntity<BoardDto.CreateDto> addBoard(
            BoardDto.CreateDto request
    ) {
        return new ResponseEntity<>(
                BoardDto.CreateDto.response(
                        boardRepository.save(addBoardFromRequest(request, getUserInfo()))
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
    public ResponseEntity<BoardDto.DeleteDto> deleteBoard(BoardDto.DeleteDto request) {
        boardRepository.deleteByIdAndUserId(
                request.getId(), getUserInfo().getId()
        );
        return new ResponseEntity<>(
                BoardDto.DeleteDto.response(request.getId(), "DELETE_BOARD_TRUE"), HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<BoardDto.UpdateDto> updateBoard(BoardDto.UpdateDto request) {
        return new ResponseEntity<>(
                BoardDto.UpdateDto.response(
                        boardRepository.save(
                                saveBoardFromRequest(request, getBoardInfo(request.getId()))
                        )
                ), HttpStatus.OK
        );
    }

    // method
    private static Board addBoardFromRequest(BoardDto.CreateDto request, User user) {
        return Board.builder()
                .user(user)
                .bName(user.getNickname())
                .title(request.getTitle())
                .note(request.getNote())
                .bImg(request.getBImg())
                .likeAll(0L)
                .createAt(LocalDateTime.now().withNano(0))
                .updateAt(LocalDateTime.now().withNano(0))
                .build();
    }

    private static Board saveBoardFromRequest(BoardDto.UpdateDto request, Board board) {
        return Board.builder()
                .id(board.getId())
                .user(board.getUser())
                .bName(board.getBName())
                .title(request.getTitle())
                .note(request.getNote())
                .bImg(request.getBImg())
                .likeAll(board.getLikeAll())
                .createAt(board.getCreateAt())
                .updateAt(LocalDateTime.now().withNano(0))
                .build();
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
        Page<Board> list = boardRepository.findAll(PageRequest.of(page - 1, limit, asc, filter));
        Page<BoardDto.PostsListDto> collect;

        if (existsMember.equals("anonymousUser")) {
            collect = list.map(
                    (Board board) -> BoardDto.PostsListDto.response(
                            board, false
                    ));
        } else {
            collect = list.map(
                    (Board board) -> BoardDto.PostsListDto.response(
                            board, likesRepository.existsByBoardIdAndUserId(board.getId(), getUserInfo().getId())
                    ));
        }
        return collect;

    }
}
