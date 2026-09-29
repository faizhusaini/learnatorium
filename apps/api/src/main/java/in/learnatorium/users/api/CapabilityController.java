package in.learnatorium.users.api;
import in.learnatorium.platform.security.SchoolContext; import java.util.Map; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/me")
public class CapabilityController { @GetMapping("/capabilities") public Map<String,Object> capabilities(){var p=SchoolContext.required(); return Map.of("userId",p.userId(),"schoolId",p.schoolId(),"permissions",p.permissions());} }

