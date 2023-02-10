package callbusLab.zaritalk.Assignment.domain.board.controller;

import callbusLab.zaritalk.Assignment.domain.board.dto.BoardDto;
import callbusLab.zaritalk.Assignment.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("board")
    @PreAuthorize("hasAnyRole('LESSOR','REALTOR','LESSEE')")
    public ResponseEntity<BoardDto.CreateDto> boardAdd(
            @RequestBody BoardDto.CreateDto request
    ) {
        return boardService.addBoard(request);
    }

    @GetMapping("board")
    public ResponseEntity<Page<BoardDto.PostsListDto>> boardList(
            @RequestParam Integer page, @RequestParam Integer limit, @RequestParam String filter, @RequestParam String arrange
    ) {
        return boardService.findListBoard(page, limit, filter, arrange);
    }
}
