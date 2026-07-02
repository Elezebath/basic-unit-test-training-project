package lv.bootcamp.shelter.task5;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Task 5: Nested test classes for CSV parsing
 * <p>
 * Practice:
 * - @Nested to organize by scenario
 * - @DisplayName for readable output
 * - Testing Optional results (isPresent, isEmpty)
 * - Testing file I/O with temp files
 * <p>
 * Instructions:
 * Write tests for AnimalCsvParser. Use @Nested classes to group tests by scenario.
 * For file-based tests, use Files.createTempFile() and Files.writeString() to create test data.
 */
@DisplayName("AnimalCsvParser")
class AnimalCsvParserTest {

    private AnimalCsvParser parser;

    @BeforeEach
    void setUp() {
        parser = new AnimalCsvParser();
    }

    // ==================== parseRow tests ====================

    @Nested
    @DisplayName("When parsing valid rows")
    class ValidRows {

        @Test
        @DisplayName("parses a complete row into an Animal")
        void shouldParseCompleteRow() {
            // Call parser.parseRow("Buddy,Dog,3,true,2026-01-15")
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true,2026-01-15");
            // Assert the result isPresent()
            assertThat(result).isPresent();
            Animal animal = result.get();
            // Assert the animal's name is "Buddy", species is "Dog", age is 3, etc.
            assertThat(animal.getName()).isEqualTo("Buddy");
            assertThat(animal.getSpecies()).isEqualTo("Dog");
            assertThat(animal.getAge()).isEqualTo(3);
            assertThat(animal.isVaccinated()).isTrue();
            assertThat(animal.getIntakeDate()).isEqualTo(LocalDate.of(2026, 1, 15));
        }

        @Test
        @DisplayName("trims whitespace from fields")
        void shouldTrimWhitespace() {
            // Call parser.parseRow("  Buddy , Dog , 3 , true , 2026-01-15 ")
            Optional<Animal> result = parser.parseRow("  Buddy , Dog , 3 , true , 2026-01-15 ");
            Animal animal = result.get();
            // Assert the parsed name is "Buddy" (trimmed)
            assertThat(animal.getName()).isEqualTo("Buddy");
        }

        @Test
        @DisplayName("parses vaccinated=false correctly")
        void shouldParseFalseVaccination() {
            // Parse a row with "false" for vaccinated
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,false,2026-01-15");
            // Assert animal.isVaccinated() == false
            assertThat(result).isPresent();
            Animal animal = result.get();
            assertThat(animal.isVaccinated()).isFalse();
        }
    }

    @Nested
    @DisplayName("When parsing malformed rows")
    class MalformedRows {

        @Test
        @DisplayName("returns empty for null input")
        void shouldReturnEmptyForNull() {
            // Call parser.parseRow(null)
            Optional<Animal> result = parser.parseRow(null);
            // Assert result isEmpty()
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty for blank input")
        void shouldReturnEmptyForBlank() {
            // Call parser.parseRow("   ")
            Optional<Animal> result = parser.parseRow("   ");
            // Assert result isEmpty()
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when row has fewer than 5 fields")
        void shouldReturnEmptyForTooFewFields() {
            // Call parser.parseRow("Buddy,Dog,3")
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3");
            // Assert result isEmpty()
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when name is missing")
        void shouldReturnEmptyForMissingName() {
            // Call parser.parseRow(",Dog,3,true,2026-01-15")
            Optional<Animal> result = parser.parseRow(",Dog,3,true,2026-01-15");
            // Assert result isEmpty()
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when age is not a number")
        void shouldReturnEmptyForBadAge() {
            // Call parser.parseRow("Buddy,Dog,old,true,2026-01-15")
            Optional<Animal> result = parser.parseRow("Buddy,Dog,old,true,2026-01-15");
            // Assert result isEmpty()
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when age is negative")
        void shouldReturnEmptyForNegativeAge() {
            // Call parser.parseRow("Buddy,Dog,-1,true,2026-01-15")
            Optional<Animal> result = parser.parseRow("Buddy,Dog,-1,true,2026-01-15");
            // Assert result isEmpty()
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when date is invalid")
        void shouldReturnEmptyForBadDate() {
            // Call parser.parseRow("Buddy,Dog,3,true,not-a-date")
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true,not-a-date");
            // Assert result isEmpty()
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("When handling edge cases")
    class EdgeCases {

        @Test
        @DisplayName("handles vaccinated field as any non-true string → false")
        void shouldTreatNonTrueAsFalse() {
            // Parse a row with vaccinated="maybe"
            // Parse a row with "false" for vaccinated
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,maybe,2026-01-15");
            // Assert isVaccinated() returns false (Boolean.parseBoolean behavior)
            assertThat(result).isPresent();
            Animal animal = result.get();
            assertThat(animal.isVaccinated()).isFalse();
        }

        @Test
        @DisplayName("handles age 0 as valid")
        void shouldAcceptAgeZero() {
            // Parse a row with age=0

            Optional<Animal> result =
                    parser.parseRow("Buddy,Dog,0,true,2026-01-15");

            // Assert result isPresent() and age is 0
            assertThat(result).isPresent();
            Animal animal = result.get();
            assertThat(animal.getAge()).isZero();
        }
    }

    // ==================== parseFile tests ====================

    @Nested
    @DisplayName("When parsing a CSV file")
    class ParseFile {

        @Test
        @DisplayName("parses valid rows and counts skipped rows")
        void shouldParseFileAndCountSkipped() throws IOException {
            // Create a temp file with a header + 3 valid rows + 1 malformed row
            Path tempFile = Files.createTempFile("test-intake", ".csv");
            try {
                String content = """
                        name,species,age,vaccinated,intakeDate
                        Buddy,Dog,3,true,2026-01-15
                        Luna,Cat,2,true,2026-01-10
                        Max,Dog,5,false,2026-01-20
                        Buddy,Dog,old,true,2026-01-15
                        """;
                Files.writeString(tempFile, content, StandardCharsets.UTF_8);

                // Call parser.parseFile(tempFile)
                AnimalCsvParser.ParseResult result = parser.parseFile(tempFile);

                // Assert result.animals() has size 3
                assertThat(result.animals()).hasSize(3);
                // Assert result.skippedRows() == 1
                assertThat(result.skippedRows()).isEqualTo(1);

            } finally {
                // Clean up: Files.deleteIfExists(tempFile)
                Files.deleteIfExists(tempFile);
            }
        }

        @Test
        @DisplayName("returns empty result for file with only a header")
        void shouldReturnEmptyForHeaderOnly() throws IOException {
            // Create a temp file with just "name,species,age,vaccinated,intakeDate"
            Path tempFile = Files.createTempFile("test-intake", ".csv");
            try {
                String content = "name,species,age,vaccinated,intakeDate";
                Files.writeString(tempFile, content, StandardCharsets.UTF_8);

                // Call parser.parseFile(tempFile)
                AnimalCsvParser.ParseResult result = parser.parseFile(tempFile);

                // Assert result.animals() is empty and skippedRows == 0
                assertThat(result.animals()).isEmpty();
                assertThat(result.skippedRows()).isZero();

            } finally {
                // Clean up: Files.deleteIfExists(tempFile)
                Files.deleteIfExists(tempFile);
            }
        }

        @Test
        @DisplayName("returns empty result for empty file")
        void shouldReturnEmptyForEmptyFile() throws IOException {
            Path file = Files.createTempFile("animals", ".csv");
            try {
                AnimalCsvParser.ParseResult result = parser.parseFile(file);
                assertThat(result.animals()).isEmpty();
                assertThat(result.skippedRows()).isZero();
            } finally {
                // Clean up: Files.deleteIfExists(tempFile)
                Files.deleteIfExists(file);
            }
        }

        @Test
        @DisplayName("throws IOException for non-existent file")
        void shouldThrowForMissingFile() {
            // Call parser.parseFile(Path.of("does-not-exist.csv"))
            // Assert it throws IOException
            assertThrows(IOException.class, () -> parser.parseFile(Path.of("does-not-exist.csv")));
        }
    }
}
