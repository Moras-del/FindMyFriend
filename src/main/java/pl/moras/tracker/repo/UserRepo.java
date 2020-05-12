package pl.moras.tracker.repo;

import org.neo4j.springframework.data.repository.ReactiveNeo4jRepository;
import org.neo4j.springframework.data.repository.query.Query;
import org.springframework.stereotype.Repository;
import pl.moras.tracker.model.User;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface UserRepo extends ReactiveNeo4jRepository<User, Long> {

    Mono<User> findByName(String name);

    @Query("MATCH (n:User{name: $0})-[r:FRIENDS]->(m) WHERE m.trackEnabled=true RETURN m")
    Mono<List<User>> findOnlineFriends(String name);

    Mono<Boolean> existsByName(String username);
}
