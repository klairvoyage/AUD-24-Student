package p1.sort.radix;

/**
 * An {@link RadixIndexExtractor} for extracting the index corresponding to a digit in an integer value.
 *
 * <p>To calculate the index, the given base 10 number is converted to its {@code #radix}-base representation and then the
 * digit at the given position is extracted. The position 0 corresponds to the least significant digit of the number.
 *
 * <p>If a position is given that is greater than the amount of digits in the number, {@code 0} is used as a padding
 * to not influence the sorting.
 */
public class IntegerIndexExtractor implements RadixIndexExtractor<Integer> {

    /**
     * The radix of this extractor. It is used to convert the given number to its radix-base representation to
     * ensure that the digits can be used as indices.
     */
    private final int radix;

    /**
     * Creates a new {@link IntegerIndexExtractor} instance.
     * @param radix The radix of the extractor.
     */
    public IntegerIndexExtractor(int radix) {
        if (radix < 1) {
            throw new IllegalArgumentException("The radix must be greater than 0.");
        }

        this.radix = radix;
    }

    @Override
    public int extractIndex(Integer value, int position) {
        return (value / (int) Math.pow(radix, position)) % radix;
    }

    @Override
    public int getRadix() {
        return radix;
    }
}
