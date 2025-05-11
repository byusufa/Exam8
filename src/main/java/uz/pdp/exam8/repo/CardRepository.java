package uz.pdp.exam8.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.exam8.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {

    @Query(value = """
               SELECT *
                         FROM card c
                         WHERE c.is_completed = false
                         ORDER BY c.order_number;
            """,nativeQuery = true)
    List<Card> finByOrderNumber();


    @Query(value = """
            SELECT *
          FROM card c
          WHERE c.is_completed = true;
          """,nativeQuery = true)
    Optional<Card> findByIsCompletedTrue();

}