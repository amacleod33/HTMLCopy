package test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class HTMLStringsTest {

    private static final Class<?> CLASS = HTMLStrings.class;

    private static PrintStream    CONSOLE;
    private static PrintStream    ERROR;

    @BeforeAll
    public static void beforeTestsBegin() {
        CONSOLE = System.out;
        ERROR = System.err;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
    }


    @AfterAll
    public static void afterTestsEnd() {
        System.setOut(CONSOLE);
        System.setErr(ERROR);
    }


    @Test
    public void testAllFieldsPrivateAndNoneStatic() {
        Field[] fields = CLASS.getDeclaredFields();

        for (Field f : fields) {
            if (!f.isSynthetic()) {
                assertTrue(
                    Modifier.isPrivate(f.getModifiers()),
                    () -> "Field \"" + f.getName() + "\" should be private");
                assertFalse(
                    Modifier.isStatic(f.getModifiers()),
                    () -> "Field \"" + f.getName() + "\" cannot be static");
            }
        }
    }


    @ParameterizedTest
    @MethodSource("dataFunctionalTests")
    void testFunctionalTests(String input, String expected) {
        String actual = HTMLStrings.copy(input);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }


    static Stream<Arguments> dataFunctionalTests() {
        return Stream.of(
            Arguments.of("0,0,", ""),
            Arguments.of("1,4,abcdefg", "bcd"),
            Arguments.of("0,15,Testing<b>!</b>", "Testing<b>!</b>"),
            Arguments.of(
                "16,21,<foo>yippy!<bar>bleep<blah>yay!</blah></bar></foo>",
                "<foo><bar>bleep</bar></foo>"),
            Arguments.of("4,4,<b>123</b>", "<b></b>"),
            Arguments.of("0,16,  :-/ :-> :-) :-<->", "  :-/ :-> :-) :-"),
            Arguments.of(
                "0,10,a<b>cd<i>e</i>f<tt>gh</tt>i</b>jk",
                "a<b>cd<i>e</i></b>"),
            Arguments.of(
                "19,33,a<b>cd<i>e</i>f<tt>gh</tt>i</b>jk",
                "<b><tt>gh</tt>i</b>jk"),
            Arguments.of(
                "10,33,a<b>cd<i>e</i>f<tt>gh</tt>i</b>jk",
                "<b><i></i>f<tt>gh</tt>i</b>jk"),
            Arguments.of(
                "9,33,a<b>cd<i>e</i>f<tt>gh</tt>i</b>jk",
                "<b><i>e</i>f<tt>gh</tt>i</b>jk"),
            Arguments.of(
                "10,19,a<b>cd<i>e</i>f<tt>gh</tt>i</b>jk",
                "<b><i></i>f<tt></tt></b>"),
            Arguments.of(
                "34,50,a<b>c<w> </w>d<z><i>e</i></z>f<tt>gh</tt> i </b>jk",
                "<b><tt>gh</tt> i </b>jk"));
    }
}
