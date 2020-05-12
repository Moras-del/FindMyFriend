package pl.moras.tracker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByName(s).block();//.orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkonika "+s));
        return new MyUserDetails(user);
    }
}
