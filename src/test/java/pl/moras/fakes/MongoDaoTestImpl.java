package pl.moras.fakes;

import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.MongoDao;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MongoDaoTestImpl implements MongoDao {

    private User friend() {
        User friend = new User();
        User user = user("principal");
        friend.setName("friend");
        friend.setPassword("password");
        friend.setTrackEnabled(true);
        friend.addFriend(user);
        user.addFriend(friend);
        return friend;
    }


    private User user(String name) {
        User user = new User();
        user.setName(name);
        user.setPassword("password");

        return user;
    }


    @Override
    public Mono<User> save(User user) {
        return Mono.just(user);
    }

    @Override
    public Mono<User> findByName(String name) {
        if (name.equals("non-existing-user"))
            return Mono.empty();
        if (name.equals("friend"))
            return Mono.just(friend());
        return Mono.just(user(name));
    }

    @Override
    public Flux<User> findAll() {
        return Flux.just(friend(), friend(), friend());
    }


    @Override
    public Mono<Boolean> notExists(String name) {
        return Mono.just(!name.equals("user"));
    }

}
