package p1.sort.radix;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A {@link RadixIndexExtractor} for extracting the index corresponding to a character in a string.
 *
 * <p>It is case-insensitive. It works similar to {@link LatinStringIndexExtractor}, but it adds a padding to the input
 * to handle strings of different lengths. It assumes that {@link #maxInputLength} is the maximum length of an input
 * string.
 */
public class PaddingLatinStringIndexExtractor extends LatinStringIndexExtractor {

    private int maxInputLength;

    public PaddingLatinStringIndexExtractor(int maxInputLength) {
        this.maxInputLength = maxInputLength;
    }

    @Override
    public int extractIndex(String value, int position) {
        return crash(); //TODO: H3  c) - remove if implemented
    }

    @Override
    public int getRadix() {
        return crash(); //TODO: H3  c) - remove if implemented
    }

    public void setMaxInputLength(int maxInputLength) {
        this.maxInputLength = maxInputLength;
    }
}
