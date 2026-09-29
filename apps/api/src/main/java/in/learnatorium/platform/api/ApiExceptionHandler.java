package in.learnatorium.platform.api;
import jakarta.servlet.http.HttpServletRequest; import java.net.URI; import org.springframework.http.*; import org.springframework.security.authentication.BadCredentialsException; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(BadCredentialsException.class) ResponseEntity<ProblemDetail> credentials(HttpServletRequest request){return problem(HttpStatus.UNAUTHORIZED,"Authentication failed",request);}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ProblemDetail> validation(HttpServletRequest request){return problem(HttpStatus.BAD_REQUEST,"Request validation failed",request);}
 private ResponseEntity<ProblemDetail> problem(HttpStatus status,String detail,HttpServletRequest request){ProblemDetail p=ProblemDetail.forStatusAndDetail(status,detail);p.setInstance(URI.create(request.getRequestURI()));p.setProperty("correlationId",request.getAttribute("correlationId"));return ResponseEntity.status(status).body(p);}
}

