package pl.moras.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionsHandler {

    @ResponseBody
    @ExceptionHandler({UsernameAlreadyExists.class, UsernameNotFoundException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String exception(Exception exception){
        return exception.getMessage();
    }
}
