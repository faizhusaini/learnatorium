package in.learnatorium.platform.security;
import java.util.Collection; import org.springframework.security.authentication.AbstractAuthenticationToken; import org.springframework.security.core.GrantedAuthority; import org.springframework.security.oauth2.jwt.Jwt;
public final class SchoolAuthenticationToken extends AbstractAuthenticationToken {
 private final SchoolPrincipal principal; private final Jwt jwt;
 public SchoolAuthenticationToken(SchoolPrincipal principal,Jwt jwt,Collection<? extends GrantedAuthority> authorities){super(authorities);this.principal=principal;this.jwt=jwt;setAuthenticated(true);}
 @Override public SchoolPrincipal getPrincipal(){return principal;} @Override public Jwt getCredentials(){return jwt;} @Override public String getName(){return principal.userId().toString();}
}
