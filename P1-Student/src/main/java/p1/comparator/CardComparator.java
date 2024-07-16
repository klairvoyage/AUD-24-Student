package p1.comparator;

import p1.card.Card;
import p1.card.CardColor;

import java.util.Comparator;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Compares two {@linkplain Card Cards}.
 * <p>
 * The cards are first compared by their value and then by their {@link CardColor}.
 *
 * @see Card
 * @see CardColor
 */
public class CardComparator implements Comparator<Card> {

    /**
     * Compares two {@linkplain Card Cards}.
     * <p>
     * The cards are first compared by their value and then by their {@link CardColor}.
     * <p>
     * The value of the cards compared by the natural order of the {@link Integer} class.
     * <p>
     * The color of the cards compared using the following order: {@link CardColor#CLUBS} > {@link CardColor#SPADES} >.{@link CardColor#HEARTS} > {@link CardColor#DIAMONDS}.
     *
     * @param o1 the first {@link Card} to compare.
     * @param o2 the second {@link Card} to compare.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * @throws NullPointerException if either of the {@linkplain Card Cards} is null.
     *
     * @see Card
     * @see CardColor
     * @see Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Card o1, Card o2) {
        // Check if either of the card objects is null and throw a NullPointerException if true
        if (o1 == null || o2 == null) throw new NullPointerException();
        // Compare the card values using the functional method int compare (from the functional interface Comparator)
        int comparisonValue = Integer.compare(o1.cardValue(), o2.cardValue());
        // If the card values are not equal, return the comparison result
        if (comparisonValue != 0) return comparisonValue;
        // If the card values are equal, compare their colors; the getValue method converts the CardColor enum to an int
        else return Integer.compare(getValue(o1.cardColor()), getValue(o2.cardColor()));
    }
    private int getValue(CardColor cardColor) {
        // Check if the cardColor is null and throw an IllegalArgumentException if true
        if (cardColor == null) throw new IllegalArgumentException();
        // Use a switch statement to assign an integer value to each card color
        return switch (cardColor) {
            case DIAMONDS -> 1; // Diamonds are the lowest
            case HEARTS -> 2;
            case SPADES -> 3;
            case CLUBS -> 4; // Clubs are the highest
        };
    }
}
