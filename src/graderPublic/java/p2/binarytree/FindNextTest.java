package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.SearchTree;

@TestForSubmission
public class FindNextTest extends TraversingTest {

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindNext_Root_Simple.json", customConverters = "customConverters")
    public void testFindNextRootSimple(JsonParameterSet params) throws Throwable {
        testTraversing(params, SearchTree::findNext, "findNext");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindNext_Simple.json", customConverters = "customConverters")
    public void testFindNextSimple(JsonParameterSet params) throws Throwable {
        testTraversing(params, SearchTree::findNext, "findNext");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindNext_Complex.json", customConverters = "customConverters")
    public void testFindNextComplex(JsonParameterSet params) throws Throwable {
        testTraversing(params, SearchTree::findNext, "findNext");
    }

}
