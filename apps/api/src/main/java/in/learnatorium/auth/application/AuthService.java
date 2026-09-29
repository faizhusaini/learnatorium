package in.learnatorium.auth.application;

import in.learnatorium.auth.domain.RefreshToken;
import in.learnatorium.auth.infrastructure.RefreshTokenRepository;
import in.learnatorium.platform.security.AuthProperties;
import in.learnatorium.users.domain.UserAccount;
import in.learnatorium.users.infrastructure.PermissionQueryRepository;
import in.learnatorium.users.infrastructure.UserAccountRepository;
import java.nio.charset.StandardCharsets; import java.security.MessageDigest; import java.security.SecureRandom;
import java.time.Instant; import java.util.Base64; import java.util.List; import java.util.UUID;
import org.springframework.security.authentication.BadCredentialsException; import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm; import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UserAccountRepository users; private final PermissionQueryRepository permissions; private final RefreshTokenRepository refreshTokens;
  private final PasswordEncoder passwords; private final JwtEncoder jwtEncoder; private final AuthProperties properties; private final SecureRandom random=new SecureRandom();
  public AuthService(UserAccountRepository u, PermissionQueryRepository p, RefreshTokenRepository r, PasswordEncoder pe, JwtEncoder j, AuthProperties ap){users=u;permissions=p;refreshTokens=r;passwords=pe;jwtEncoder=j;properties=ap;}
  @Transactional public Tokens login(UUID schoolId,String email,String password){
    UserAccount user=users.findBySchoolIdAndEmailNormalized(schoolId,email.trim().toLowerCase()).filter(UserAccount::active).orElseThrow(this::bad);
    if(!passwords.matches(password,user.passwordHash())) throw bad(); return issue(user,UUID.randomUUID());
  }
  @Transactional public Tokens refresh(String raw){
    Instant now=Instant.now(); RefreshToken old=refreshTokens.findByTokenHash(hash(raw)).orElseThrow(this::bad);
    if(!old.usable(now)){ old.revoke(now); throw bad(); } old.revoke(now);
    UserAccount user=users.findById(old.userId()).filter(u->u.active()&&u.schoolId().equals(old.schoolId())).orElseThrow(this::bad);
    return issue(user,old.familyId());
  }
  private Tokens issue(UserAccount user,UUID family){Instant now=Instant.now(); List<String> perms=permissions.find(user.schoolId(),user.id());
    JwtClaimsSet claims=JwtClaimsSet.builder().issuer(properties.issuer()).issuedAt(now).expiresAt(now.plus(properties.accessTokenTtl())).subject(user.id().toString()).claim("school_id",user.schoolId().toString()).claim("permissions",perms).build();
    String access=jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
    byte[] bytes=new byte[48]; random.nextBytes(bytes); String raw=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    refreshTokens.save(new RefreshToken(user.schoolId(),user.id(),family,hash(raw),now.plus(properties.refreshTokenTtl()))); return new Tokens(access,raw,properties.accessTokenTtl().toSeconds(),perms);}
  private String hash(String value){try{return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
  private BadCredentialsException bad(){return new BadCredentialsException("Invalid credentials");}
  public record Tokens(String accessToken,String refreshToken,long expiresIn,List<String> permissions){}
}

