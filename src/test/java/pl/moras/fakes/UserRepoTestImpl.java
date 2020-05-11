package pl.moras.fakes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public Optional<User> findByName(String name) {
        switch (name){
            case "non-existing-user":
                return Optional.empty();
            case "friend":
                return Optional.of(friend());
            default:
                return Optional.of(user(name));
        }
    }

    @Override
    public List<User> findOnlineFriends(String name) {
        return Collections.singletonList(friend());
    }

    @Override
    public boolean existsByName(String username) {
        return username.equals("user");
    }

    @Override
    public <S extends User> S save(S s, int i) {
        return null;
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> iterable, int i) {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong, int i) {
        return Optional.empty();
    }

    @Override
    public <S extends User> S save(S s) {
        return s;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Iterable<User> findAll(int i) {
        return null;
    }

    @Override
    public Iterable<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Iterable<User> findAll(Sort sort, int i) {
        return null;
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> iterable, int i) {
        return null;
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> iterable, Sort sort) {
        return null;
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> iterable, Sort sort, int i) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable, int i) {
        return null;
    }
}
