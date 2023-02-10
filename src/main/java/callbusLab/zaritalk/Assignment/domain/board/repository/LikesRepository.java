package callbusLab.zaritalk.Assignment.domain.board.repository;

import callbusLab.zaritalk.Assignment.domain.board.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByBoardIdAndUserId(Long bid, Long uid);
    void deleteByBoardIdAndUserId(Long bid, Long uid);
}
