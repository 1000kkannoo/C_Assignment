package callbusLab.zaritalk.Assignment.domain.board.controller;

import callbusLab.zaritalk.Assignment.domain.board.dto.BoardDto;
import callbusLab.zaritalk.Assignment.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 글 생성
    @PostMapping("board")
    @PreAuthorize("hasAnyRole('LESSOR','REALTOR','LESSEE')")
    public ResponseEntity<BoardDto.CreateDto> boardAdd(
            @Valid @RequestBody final BoardDto.CreateDto request
    ) {
        return boardService.addBoard(request);
    }

    // 글 목록 조회
    @GetMapping("board")
    public ResponseEntity<Page<BoardDto.PostsListDto>> boardList(
            @RequestParam Integer page, @RequestParam Integer limit, @RequestParam String filter, @RequestParam String arrange
    ) {
        return boardService.findListBoard(page, limit, filter, arrange);
    }

    // 글 삭제
    @DeleteMapping("board")
    @PreAuthorize("hasAnyRole('LESSOR','REALTOR','LESSEE')")
    public ResponseEntity<BoardDto.DeleteDto> boardDelete(
            @Valid @RequestBody final BoardDto.DeleteDto request
    ) {
        return boardService.deleteBoard(request);
    }

    // 글 수정
    @PatchMapping("board")
    @PreAuthorize("hasAnyRole('LESSOR','REALTOR','LESSEE')")
    public ResponseEntity<BoardDto.UpdateDto> boardUpdate(
            @Valid @RequestBody final BoardDto.UpdateDto request
    ) {
        return boardService.updateBoard(request);
    }
}
