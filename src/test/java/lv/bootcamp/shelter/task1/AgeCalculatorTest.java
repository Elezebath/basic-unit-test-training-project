package lv.bootcamp.shelter.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Task 1: Pure logic tests
 * ------------------------
 * Practice:
 * - Arrange-Act-Assert pattern
 * - Good test naming
 * - assertEquals for return values
 * - assertThrows for invalid input
 *
 */
@DisplayName("AgeCalculator")
class AgeCalculatorTest {

    private AgeCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new AgeCalculator();
    }

    // --- toMonths() ---

    @Test
    @DisplayName("toMonths: 0 years returns 0 months")
    void shouldReturnZeroMonthsForZeroYears() {
        // Act — call calculator.toMonths(0)
        int months = calculator.toMonths(0);

        // Assert — assertEquals(0, result)
        assertEquals(0, months);
    }

    @Test
    @DisplayName("toMonths: positive years returns correct months")
    void shouldConvertPositiveYearsToMonths() {
        // Test that 3 years = 36 months
        int months = calculator.toMonths(3);
        assertEquals(36, months);
    }

    @Test
    @DisplayName("toMonths: max large positive years returns correct months")
    void shouldConvertPositiveMaxIntegerYearsToCorrespondingMonths() {
        // Test that max integer years = max integer value * 12 months
        int possibleMaxYear = Integer.MAX_VALUE / 12;
        int months = calculator.toMonths(possibleMaxYear);
        assertEquals(possibleMaxYear * 12, months);
    }

    @Test
    @DisplayName("toMonths: negative years throws IllegalArgumentException")
    void shouldThrowForNegativeYears() {
        // Use assertThrows to verify that toMonths(-1) throws IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.toMonths(-1)
        );

        // Optionally check the exception message contains "negative"
        assertTrue(exception.getMessage().contains("negative"));
    }

    // --- dogToHumanYears() ---

    @Test
    @DisplayName("dogToHumanYears: age 0 returns 0")
    void shouldReturnZeroHumanYearsForPuppy() {
        // Test that dogToHumanYears(0) returns 0
        int result = calculator.dogToHumanYears(0);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("dogToHumanYears: age 1 returns 15")
    void shouldReturnFifteenForOneYearOldDog() {
        // Test that dogToHumanYears(1) returns 15
        int result = calculator.dogToHumanYears(1);

        assertEquals(15, result);
    }

    @Test
    @DisplayName("dogToHumanYears: age 2 returns 24")
    void shouldReturnTwentyFourForTwoYearOldDog() {
        // Test that dogToHumanYears(2) returns 24
        int result = calculator.dogToHumanYears(2);

        assertEquals(24, result);
    }

    @Test
    @DisplayName("dogToHumanYears: age 5 returns 39")
    void shouldCalculateCorrectlyForOlderDog() {
        // Test that dogToHumanYears(5) returns 24 + (5-2)*5 = 39
        int result = calculator.dogToHumanYears(5);

        assertEquals(39, result);
    }

    @Test
    @DisplayName("dogToHumanYears: negative age throws IllegalArgumentException")
    void shouldThrowForNegativeDogAge() {
        // Use assertThrows for negative input
        // Use assertThrows to verify that toMonths(-1) throws IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.dogToHumanYears(-1)
        );

        // Optionally check the exception message contains "negative"
        assertTrue(exception.getMessage().contains("negative"));
    }

    // --- isBaby() ---

    @Test
    @DisplayName("isBaby: age 0 returns true")
    void shouldReturnTrueForAgZero() {
        // Test that isBaby(0) returns true
        boolean result = calculator.isBaby(0);

        assertTrue(result);
    }

    @Test
    @DisplayName("isBaby: age 1 returns false")
    void shouldReturnFalseForAgeOne() {
        // Test that isBaby(1) returns false
        boolean result = calculator.isBaby(1);

        assertFalse(result);
    }
}
