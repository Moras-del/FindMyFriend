package pl.moras.tracker.exceptions;

public class UsernameAlreadyExists extends RuntimeException {

    public UsernameAlreadyExists(String message) {
        super("Nazwa użytkownika "+message+" jest zajęta");
    }
}
