package schneider.davi.to_do_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handleNotFoundException(NotFoundException e) {
        var error = new DefaultErrorMessage(e.getStatusCode().value(), e.getReason());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
