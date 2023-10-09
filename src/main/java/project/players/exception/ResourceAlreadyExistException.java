package project.players.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Resource already exists")
public class ResourceAlreadyExistException extends Exception {

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
