package in.learnatorium.auth.api;
import in.learnatorium.auth.application.AuthService; import jakarta.validation.Valid; import jakarta.validation.constraints.*; import java.util.UUID;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
 private final AuthService auth; public AuthController(AuthService auth){this.auth=auth;}
 @PostMapping("/login") public ResponseEntity<AuthService.Tokens> login(@Valid @RequestBody LoginRequest r){return ResponseEntity.ok(auth.login(r.schoolId(),r.email(),r.password()));}
 @PostMapping("/refresh") public ResponseEntity<AuthService.Tokens> refresh(@Valid @RequestBody RefreshRequest r){return ResponseEntity.ok(auth.refresh(r.refreshToken()));}
 public record LoginRequest(@NotNull UUID schoolId,@Email @NotBlank String email,@NotBlank @Size(max=200) String password){}
 public record RefreshRequest(@NotBlank String refreshToken){}
}

