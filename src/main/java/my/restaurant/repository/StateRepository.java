package my.restaurant.repository;

import my.restaurant.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    Optional<State> findStateByName(String name);
    List<State> findStatesByCountryCountryId(long id);
    List<State> findStatesByCountryName(String name);
}
