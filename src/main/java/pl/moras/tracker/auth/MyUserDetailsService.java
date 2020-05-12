package pl.moras.tracker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.moras.tracker.repo.UserRepo;
import reactor.core.publisher.Mono;

@Service
public class MyUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return userRepo.findByName(s)
                .map(MyUserDetails::new);
    }
}
