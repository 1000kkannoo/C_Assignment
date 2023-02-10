package callbusLab.zaritalk.Assignment.domain.board.service;

import callbusLab.zaritalk.Assignment.domain.board.dto.BoardDto;
import callbusLab.zaritalk.Assignment.domain.board.entity.Board;
import callbusLab.zaritalk.Assignment.domain.board.repository.BoardRepository;
import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import callbusLab.zaritalk.Assignment.domain.user.repository.UserRepository;
import callbusLab.zaritalk.Assignment.global.config.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    private User getUserInfo() {
        Optional<User> byUser = userRepository.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName()
        );

        if (!byUser.isPresent()) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }

        User user = byUser.get();
        return user;
    }

    @Transactional
    public ResponseEntity<BoardDto.CreateDto> addBoard(BoardDto.CreateDto request) {
        User user = getUserInfo();
        Board board = boardRepository.save(
                Board.builder()
                        .user(user)
                        .title(request.getTitle())
                        .user(user)
                        .bName(user.getNickname())
                        .note(request.getNote())
                        .bImg(request.getBImg())
                        .quit(false)
                        .build()
        );
        return new ResponseEntity<>(
                BoardDto.CreateDto.response(
                        board
                ), HttpStatus.CREATED
        );
    }

    @Transactional
    public ResponseEntity<Page<BoardDto.PostsListDto>> findListBoard(Integer page, Integer limit, String filter, String arrange) {

        if (arrange.equals("ASC")) {
            Pageable pageWithTenElements = PageRequest.of(page - 1, limit, Sort.Direction.ASC, filter);
            Page<Board> list = boardRepository.findAll(pageWithTenElements);
            Page<BoardDto.PostsListDto> collect = list.map(BoardDto.PostsListDto::response);
            return new ResponseEntity<>(collect, HttpStatus.OK);
        } else {
            Pageable pageWithTenElements = PageRequest.of(page - 1, limit, Sort.Direction.DESC, filter);
            Page<Board> list = boardRepository.findAll(pageWithTenElements);
            Page<BoardDto.PostsListDto> collect = list.map(BoardDto.PostsListDto::response);
            return new ResponseEntity<>(collect, HttpStatus.OK);
        }

    }
}
