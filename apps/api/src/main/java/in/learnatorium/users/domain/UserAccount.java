package in.learnatorium.users.domain;

import java.util.UUID;
import jakarta.persistence.*;

@Entity @Table(name="users")
public class UserAccount {
    @Id private UUID id;
    @Column(name="school_id", nullable=false) private UUID schoolId;
    @Column(name="email_normalized", nullable=false) private String emailNormalized;
    @Column(name="password_hash", nullable=false) private String passwordHash;
    @Column(nullable=false) private String status;
    protected UserAccount() {}
    public UUID id() { return id; }
    public UUID schoolId() { return schoolId; }
    public String passwordHash() { return passwordHash; }
    public boolean active() { return "ACTIVE".equals(status); }
}

