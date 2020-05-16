package pl.moras.tracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({UsernameAlreadyExists.class, UsernameNotFoundException.class, AccessDeniedException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> exception(Exception exception) {
        return Mono.just(exception)
                .map(Throwable::getMessage);
    }
}
