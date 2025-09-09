import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LessonThreeTest {

    @Test
    public void checkPhraseLength() {
        String hello = "Hello, world";
        assertTrue(hello.length()>15, "The phrase \"" + hello + "\" is too short. Its length is " + hello.length() + " symbols");
    }
}
