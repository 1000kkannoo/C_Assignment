package callbusLab.zaritalk.Assignment.domain.board.repository;

import callbusLab.zaritalk.Assignment.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    @Transactional(readOnly = true)
    Optional<Board> findByIdAndUserId(Long boardId, Long userId);

}
