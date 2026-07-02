package lv.bootcamp.shelter.task23;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tasks 2 & 3: Parameterized tests and exception tests
 * Practice:
 * - @ParameterizedTest with @CsvSource
 * - @ValueSource and @NullAndEmptySource
 * - assertThrows with message checks
 * - AssertJ assertThatThrownBy
 */
@DisplayName("AnimalValidator")
class AnimalValidatorTest {

    private AnimalValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AnimalValidator();
    }

    // ==================== Task 2: Parameterized tests ====================

    @Nested
    @DisplayName("validateName")
    class ValidateName {

        @ParameterizedTest
        @ValueSource(strings = {"Buddy", "Luna", "Mr. Whiskers", "X"})
        @DisplayName("accepts valid names")
        void shouldAcceptValidNames(String name) {
            // Call validator.validateName(name) — it should NOT throw
            assertDoesNotThrow(() -> validator.validateName(name));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("rejects blank or null names")
        void shouldRejectBlankNames(String name) {
            // Verify that validateName throws IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateName(name));
            // Check the message contains "must not be blank"
            assertTrue(exception.getMessage().contains("must not be blank"));
        }

        @Test
        @DisplayName("rejects name longer than 100 characters")
        void shouldRejectOverlyLongName() {
            // Create a string longer than 100 characters
            String longName = "a".repeat(101);
            // Verify that validateName throws IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateName(longName));
            // Check the message contains "100 characters"
            assertTrue(exception.getMessage().contains("100 characters"));
        }
    }

    @Nested
    @DisplayName("validateAge")
    class ValidateAge {

        @ParameterizedTest
        @CsvSource({
                "0",
                "1",
                "10",
                "50"
        })
        @DisplayName("accepts valid ages")
        void shouldAcceptValidAges(int age) {
            // Call validator.validateAge(age) — it should NOT throw
            assertDoesNotThrow(()->validator.validateAge(age));
        }

        @ParameterizedTest
        @CsvSource({
                "-1, must not be negative",
                "-100, must not be negative",
                "51, seems unrealistic",
                "999, seems unrealistic"
        })
        @DisplayName("rejects invalid ages with correct message")
        void shouldRejectInvalidAges(int age, String expectedMessagePart) {
            // Verify that validateAge(age) throws IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateAge(age));

            // Check that the exception message contains expectedMessagePart
            assertTrue(exception.getMessage().contains(expectedMessagePart));
        }
    }

    // ==================== Task 3: Exception tests ====================

    @Nested
    @DisplayName("validate (full animal)")
    class ValidateFullAnimal {

        @Test
        @DisplayName("throws NullPointerException for null animal")
        void shouldThrowForNullAnimal() {
            // Call validator.validate(null)
            // Verify it throws NullPointerException
            NullPointerException exception = assertThrows(NullPointerException.class, () -> validator.validate(null));
            // Check message contains "must not be null"
            assertTrue(exception.getMessage().contains("must not be null"));
        }

//        @ParameterizedTest
//        @CsvSource({
//                "'', Cat, 2, must not be blank",
//                "Tommy, '', 2, must not be blank",
//                "Tommy, Cat, -1, negative"
//        })
//        @DisplayName("throws for invalid animal fields")
//        void shouldRejectInvalidAnimals(
//                String name,
//                String species,
//                int age,
//                String expectedMessage) {
//
//            Animal animal = new Animal(
//                    name,
//                    species,
//                    age,
//                    true,
//                    LocalDate.of(2026, 1, 15));
//
//            IllegalArgumentException exception = assertThrows(
//                    IllegalArgumentException.class,
//                    () -> validator.validate(animal));
//
//            assertThat(exception.getMessage())
//                    .contains(expectedMessage);
//        }

        @Test
        @DisplayName("throws for animal with blank name")
        void shouldThrowForBlankName() {
            // Create an Animal with blank name
            Animal animal = new Animal("", "Cat", 2, true, LocalDate.of(2026, 1, 15));

            // Verify validate() throws IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));

            assertThat(exception.getMessage()).contains("must not be blank");
        }

        @Test
        @DisplayName("throws for animal with blank species")
        void shouldThrowForBlankSpecies() {
            // Create an Animal with valid name but blank species
            Animal animal = new Animal("Tommy", "", 2, true, LocalDate.of(2026, 1, 15));

            // Verify validate() throws IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));

            assertThat(exception.getMessage()).contains("must not be blank");
        }

        @Test
        @DisplayName("throws for animal with negative age")
        void shouldThrowForNegativeAge() {
            // Create an Animal with negative age
            // Create an Animal with valid name but blank species
            Animal animal = new Animal("Tommy", "Cat", -2, true, LocalDate.of(2026, 1, 15));

            // Verify validate() throws IllegalArgumentException
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));

            assertThat(exception.getMessage()).contains("negative");
        }

        @Test
        @DisplayName("does not throw for fully valid animal")
        void shouldPassForValidAnimal() {
            // Create a valid Animal (name="Buddy", species="Dog", age=3, vaccinated=true, date=today)
            Animal animal = new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15));

            // Call validate() and verify no exception is thrown
            assertDoesNotThrow(()->validator.validate(animal));
        }
    }
}
