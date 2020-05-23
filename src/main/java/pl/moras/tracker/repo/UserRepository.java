package pl.moras.tracker.repo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import pl.moras.tracker.model.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {

    Optional<User> findByName(String name);

    boolean existsByName(String username);

    List<User> findAll();
}
