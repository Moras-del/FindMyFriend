package pl.moras.fakes;

import org.reactivestreams.Publisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class UserRepoTestImpl implements UserRepo {

    private User friend(){
        User user = new User();
        user.setName("friend");
        user.setPassword("password");
        user.setTrackEnabled(true);
        return user;
    }


    private User user(String name){
        User user = new User();
        User friend = friend();
        user.setName(name);
        user.setPassword("password");
        user.addFriend(friend);
        friend.addFriend(user);
        return user;
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
    public Mono<List<User>> findOnlineFriends(String name) {
        return Mono.just(Collections.singletonList(friend()));
    }

    @Override
    public Mono<Boolean> existsByName(String username) {
        return Mono.just(username.equals("user"));
    }

    @Override
    public <S extends User> Mono<S> findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> Flux<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> Flux<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends User> Mono<Long> count(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> Mono<Boolean> exists(Example<S> example) {
        return null;
    }

    @Override
    public Flux<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public <S extends User> Mono<S> save(S s) {
        return Mono.just(s);
    }

    @Override
    public <S extends User> Flux<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public <S extends User> Flux<S> saveAll(Publisher<S> publisher) {
        return null;
    }

    @Override
    public Mono<User> findById(Long aLong) {
        return null;
    }

    @Override
    public Mono<User> findById(Publisher<Long> publisher) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Long aLong) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<Long> publisher) {
        return null;
    }

    @Override
    public Flux<User> findAll() {
        return null;
    }

    @Override
    public Flux<User> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public Flux<User> findAllById(Publisher<Long> publisher) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Long aLong) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Publisher<Long> publisher) {
        return null;
    }

    @Override
    public Mono<Void> delete(User user) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends User> iterable) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends User> publisher) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
}
