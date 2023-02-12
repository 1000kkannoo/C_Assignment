package callbusLab.zaritalk.Assignment.domain.board.repository;

import callbusLab.zaritalk.Assignment.domain.board.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
    Optional<Likes> findByBoardIdAndUserId(Long boardId, Long userId);
}
