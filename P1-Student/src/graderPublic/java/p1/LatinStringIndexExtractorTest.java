package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.radix.LatinStringIndexExtractor;
import p1.transformers.MethodInterceptor;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class LatinStringIndexExtractorTest {

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods("^java/lang/String.+", "^java/lang/Character.+");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H6_LatinStringIndexExtractorTests.json", data = "validIndexTest")
    public void testExtractValidIndex(@Property("value") String value,
                                      @Property("position") int position,
                                      @Property("expected") int expected) {
        Context context = contextBuilder()
            .subject("LatinStringIndexExtractor#extractIndex")
            .add("value", value)
            .add("position", position)
            .add("expected", expected)
            .build();


        assertEquals(expected, new LatinStringIndexExtractor().extractIndex(value, position),
            context, result -> "The method extractIndex did not return the expected value.");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H6_LatinStringIndexExtractorTests.json", data = "invalidCharTest")
    public void testExtractInvalidChar(@Property("value") String value,
                                       @Property("position") int position) {
        Context context = contextBuilder()
            .subject("LatinStringIndexExtractor#extractIndex")
            .build();

        assertEquals(0, new LatinStringIndexExtractor().extractIndex(value, position),
            context, result -> "The method extractIndex did not return the correct value for an invalid character.");
    }
}
