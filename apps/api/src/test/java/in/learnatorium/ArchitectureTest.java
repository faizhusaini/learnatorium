package in.learnatorium;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
class ArchitectureTest {
 @Test void runtimeTargetsJava21(){ assertThat(Runtime.version().feature()).isGreaterThanOrEqualTo(21); }
}

