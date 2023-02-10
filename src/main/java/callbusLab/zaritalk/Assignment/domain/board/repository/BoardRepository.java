package callbusLab.zaritalk.Assignment.domain.board.repository;

import callbusLab.zaritalk.Assignment.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    void deleteByIdAndUserId(Long boardId,Long userId);
}
