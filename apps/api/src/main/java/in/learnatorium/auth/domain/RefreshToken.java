package in.learnatorium.auth.domain;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.*;

@Entity @Table(name="refresh_tokens")
public class RefreshToken {
    @Id private UUID id;
    @Column(name="school_id", nullable=false) private UUID schoolId;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Column(name="family_id", nullable=false) private UUID familyId;
    @Column(name="token_hash", nullable=false, unique=true) private String tokenHash;
    @Column(name="expires_at", nullable=false) private Instant expiresAt;
    @Column(name="revoked_at") private Instant revokedAt;
    protected RefreshToken() {}
    public RefreshToken(UUID schoolId, UUID userId, UUID familyId, String tokenHash, Instant expiresAt) {
        this.id=UUID.randomUUID(); this.schoolId=schoolId; this.userId=userId; this.familyId=familyId; this.tokenHash=tokenHash; this.expiresAt=expiresAt;
    }
    public UUID userId(){return userId;} public UUID schoolId(){return schoolId;} public UUID familyId(){return familyId;}
    public boolean usable(Instant now){return revokedAt==null && expiresAt.isAfter(now);} public void revoke(Instant now){revokedAt=now;}
}

