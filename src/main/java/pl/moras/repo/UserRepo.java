package pl.moras.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import pl.moras.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends Neo4jRepository<User, Long> {
    Optional<User> findByName(String name);

    //daj znajomych z włączonym śledzeniem
    @Query("MATCH (n:User{name: $0})-[r:FRIENDS]->(m) WHERE m.trackEnabled=true RETURN m")
    List<User> findOnlineFriends(String name);

    boolean existsByName(String username);
}
