package pl.moras.tracker.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pl.moras.tracker.model.User;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepo extends ReactiveMongoRepository<User, String> {

    Mono<User> findByName(String name);

    //Flux<User> findOnlineFriends(String name);

    Mono<Boolean> notExistsByName(String username);
}
