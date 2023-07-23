package uz.dachatop.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.exp.AppForbiddenException;
import uz.dachatop.exp.GlobalException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({GlobalException.class})
    public ResponseEntity<?> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.ok(new ApiResponse<>(e.getMessage(), 400, true));
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.ok(new ApiResponse<>(e.getMessage(), 400, true));
    }

    @ExceptionHandler({AppForbiddenException.class})
    public ResponseEntity<?> handle(RuntimeException e) {
        return ResponseEntity.ok(new ApiResponse<>(e.getMessage(), HttpStatus.FORBIDDEN.value(), true));

    }

    @ExceptionHandler({AppBadRequestException.class})
    public ResponseEntity<?> handle(AppBadRequestException e) {
        return ResponseEntity.ok(new ApiResponse<>(e.getMessage(), HttpStatus.BAD_REQUEST.value(), true));

    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handle(AccessDeniedException e) {
        return ResponseEntity.ok(new ApiResponse<>(e.getMessage() + " You do not have permission or role mazgi:)", HttpStatus.FORBIDDEN.value(), true));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleNotValidException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
