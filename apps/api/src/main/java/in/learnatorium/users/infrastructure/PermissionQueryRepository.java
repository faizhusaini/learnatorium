package in.learnatorium.users.infrastructure;
import java.util.List; import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient; import org.springframework.stereotype.Repository;
@Repository
public class PermissionQueryRepository {
  private final JdbcClient jdbc; public PermissionQueryRepository(JdbcClient jdbc){this.jdbc=jdbc;}
  public List<String> find(UUID schoolId, UUID userId){return jdbc.sql("""
      select distinct p.code from permissions p join role_permissions rp on rp.permission_id=p.id
      join user_roles ur on ur.role_id=rp.role_id where ur.school_id=:school and ur.user_id=:user
      """).param("school",schoolId).param("user",userId).query(String.class).list();}
}

