package pjwstk.s20124.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.Animal;

import java.util.Collection;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Collection<Animal> findAnimalsByUserUsername(String username);
}
