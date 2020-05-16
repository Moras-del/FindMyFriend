package pl.moras.tracker.repo;

import pl.moras.tracker.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MongoDao {

    Mono<User> save(User user);

    Mono<User> findByName(String name);

    Flux<User> findAll();

    //Flux<User> findOnlineFriends(String name);

    Mono<Boolean> notExists(String name);
}
