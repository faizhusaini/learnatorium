package in.learnatorium.platform.security;

import java.util.Set;
import java.util.UUID;

public record SchoolPrincipal(UUID userId, UUID schoolId, Set<String> permissions) {
    public boolean has(String permission) { return permissions.contains(permission); }
}

