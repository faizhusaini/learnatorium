package in.learnatorium.auth.api;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import in.learnatorium.auth.application.AuthService; import java.util.List; import java.util.UUID;
import org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; import org.springframework.test.context.bean.override.mockito.MockitoBean; import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
@WebMvcTest(AuthController.class)
class AuthControllerTest {
 @Autowired MockMvc mvc; @MockitoBean AuthService auth;
 @Test void rejectsInvalidEmail() throws Exception {mvc.perform(post("/api/v1/auth/login").contentType("application/json").content("{\"schoolId\":\""+UUID.randomUUID()+"\",\"email\":\"bad\",\"password\":\"x\"}")).andExpect(status().isBadRequest());}
 @Test void returnsTokens() throws Exception {UUID school=UUID.randomUUID();when(auth.login(school,"admin@example.com","correct-password")).thenReturn(new AuthService.Tokens("access","refresh",600,List.of("school:manage")));mvc.perform(post("/api/v1/auth/login").contentType("application/json").content("{\"schoolId\":\""+school+"\",\"email\":\"admin@example.com\",\"password\":\"correct-password\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.accessToken").value("access"));}
}

