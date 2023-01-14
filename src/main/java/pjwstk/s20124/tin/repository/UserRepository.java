package pjwstk.s20124.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u.id from User u  where u.username = :username")
    Optional<Long> findIdByUsername(String username);
    boolean existsByUsername(String username);
}
