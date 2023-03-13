package companyx.ResourceServer.controller;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handler that intercepts exceptions thrown by controllers
 * and generalizes the response http status.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * All {@link IllegalArgumentException}s should be converted to a
     * bad request http response with the error message inside the body of the response.
     *
     * @param exception the {@link IllegalArgumentException} exception.
     * @return the bad request response containing the exception message.
     */
    @NonNull
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(@NonNull final IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
