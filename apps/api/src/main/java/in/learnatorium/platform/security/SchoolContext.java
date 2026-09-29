package in.learnatorium.platform.security;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SchoolContext {
    private SchoolContext() {}
    public static SchoolPrincipal required() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SchoolPrincipal principal)) {
            throw new IllegalStateException("Authenticated school context is required");
        }
        return principal;
    }
    public static UUID schoolId() { return required().schoolId(); }
}

