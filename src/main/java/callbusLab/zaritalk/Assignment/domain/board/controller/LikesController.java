package callbusLab.zaritalk.Assignment.domain.board.controller;

import callbusLab.zaritalk.Assignment.domain.board.dto.LikesDto;
import callbusLab.zaritalk.Assignment.domain.board.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;
    // 좋아요
    @PostMapping("board/like")
    @PreAuthorize("hasAnyRole('LESSOR','REALTOR','LESSEE')")
    public ResponseEntity<LikesDto.addDto> likeSave(
            @Valid @RequestBody final LikesDto.addDto request
    ) {
        return likesService.saveLike(request);
    }
}
