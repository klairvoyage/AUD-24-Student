package p1.sort.radix;

/**
 * Extracts an index from a value at a given position.
 *
 * <p>It is used for mapping an element of a given value to an index in the range of 0 (inclusive) to
 * {@link #getRadix()} (exclusive). A position is given to determine which element of the value should be used.
 * The lowest position is 0. It is implementation specific how the element is extracted from the value.
 *
 * <p>A typical implementation for base-10 integer values would be to extract the digit at the given position of the number
 * and return it. The radix would be 10 in this case, and it would result in these outputs, assuming the least significant
 * digit interpretation of the position:
 * <lu>
 *     <li>{@code extractKey(789, 0) -> 9}</li>
 *     <li>{@code extractKey(789, 1) -> 8}</li>
 *     <li>{@code extractKey(789, 2) -> 7}</li>
 * </lu>
 *
 * <p>{@link IntegerIndexExtractor} is an example of this, which has been extended to support any radix.
 *
 * @param <T>
 */
public interface  RadixIndexExtractor<T> {

    /**
     * Extracts an index from the given value at the given position.
     *
     * <p>It maps the element of the given value at the given position to an index in the range of 0 (inclusive) to
     * {@link #getRadix()} (exclusive).
     *
     * <p>The lowest valid position is 0. If the position is less than 0, an {@link IndexOutOfBoundsException} is thrown.
     * A non-negative position always results in a valid index. If the position is greater than the amount of elements
     * in the value a padding value, which does not influence the sorting, has to be used instead.
     *
     * @param value The value to extract the index from.
     * @param position The position of the element to map to an index. The lowest valid position is 0.
     * @return An index in the range of 0 (inclusive) to {@link #getRadix()} (exclusive).
     * @throws IndexOutOfBoundsException if the position is less than 0.
     */
    int extractIndex(T value, int position);

    /**
     * Returns the radix of the extractor.
     *
     * <p>The radix is the amount of different indices that can be extracted from a value. It is typically equal to the
     * amount of different elements that can be extracted from a value at a given position (e.q. 10 for base-10 integer
     * values), but is not limited to that.
     *
     * @return The radix of the extractor.
     */
    int getRadix();



}
