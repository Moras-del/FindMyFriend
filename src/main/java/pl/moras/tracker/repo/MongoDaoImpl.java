package pl.moras.tracker.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import pl.moras.tracker.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class MongoDaoImpl implements MongoDao {

    @Autowired
    ReactiveMongoTemplate mongoTemplate;


    @Override
    public Mono<User> save(User user) {
        return mongoTemplate.save(user);
    }

    @Override
    public Mono<User> findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, User.class)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("siema")));
    }


    @Override
    public Flux<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public Mono<Boolean> notExists(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.exists(query, User.class)
                .map(bool -> !bool);
    }

}
