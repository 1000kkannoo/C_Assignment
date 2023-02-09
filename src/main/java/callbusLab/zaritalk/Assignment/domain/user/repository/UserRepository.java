package callbusLab.zaritalk.Assignment.domain.user.repository;

import callbusLab.zaritalk.Assignment.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}