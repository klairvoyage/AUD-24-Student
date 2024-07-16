package p1.sort.radix;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A {@link RadixIndexExtractor} for extracting the index corresponding to a character in a string.
 *
 * <p>It is case-insensitive. It maps the characters 'a' to 'z' to the indices 0 to 25. All other characters are mapped to 0.
 * The position is interpreted as the position from the end of the string, i.e. position 0 corresponds to the last
 * character in the string.
 */
public class LatinStringIndexExtractor implements RadixIndexExtractor<String> {

    @Override
    public int extractIndex(String value, int position) {
        // Check if the given position is out of the bounds of the string length
        if (position<0 || position>=value.length()) return 0; // Position is out of bounds

        // Convert position to the actual index from the end of the string
        int index = value.length() - 1 - position;

        // Get the character at the calculated index and convert it to lowercase (uppercase is treated as lowercase!)
        char c = Character.toLowerCase(value.charAt(index));

        // Check if the character is within the range 'a' to 'z'
        if (c>='a' && c<='z') return c-'a'; // Map the characters 'a' to 'z' (97->122) to indices 0->25
        else return 0; // Return 0 for characters outside the range 'a' to 'z'
    }

    @Override
    public int getRadix() {
        return 'z' - 'a' + 1; //26
    }
}
