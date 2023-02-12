package callbusLab.zaritalk.Assignment.domain.board.repository;

import callbusLab.zaritalk.Assignment.domain.board.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Transactional(readOnly = true)
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    @Transactional(readOnly = true)
    Optional<Likes> findByBoardIdAndUserId(Long boardId, Long userId);
}
