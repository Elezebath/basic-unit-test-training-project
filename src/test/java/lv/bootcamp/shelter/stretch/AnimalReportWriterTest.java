package lv.bootcamp.shelter.stretch;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Stretch goal: Testing file output
 * <p>
 * Practice:
 * - Writing to temp files and reading them back
 * - String content assertions
 * - Cleanup with Files.deleteIfExists
 * <p>
 * Instructions:
 * These tests verify that AnimalReportWriter produces correct output.
 * This task is optional — attempt it after completing tasks 1–6.
 */
@DisplayName("AnimalReportWriter (stretch)")
class AnimalReportWriterTest {

    private final AnimalReportWriter writer = new AnimalReportWriter();

    @Test
    @DisplayName("writes report file that contains total count")
    void shouldWriteTotalCount() throws IOException {
        // Create a list of 3 animals
        List<Animal> animals = List.of(
            new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15)),
            new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 10)),
            new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 20))
        );

        // Create a temp file
        Path output = Files.createTempFile("report-test", ".txt");
        try {
            writer.writeReport(animals, output);
            String content = Files.readString(output, StandardCharsets.UTF_8);
            // Assert content contains "Total animals: 3"
            assertThat(content).contains("Total animals: 3");
        }
        finally {
            // Clean up: Files.deleteIfExists(output)
            Files.deleteIfExists(output);
        }
    }

    @Test
    @DisplayName("writes per-species breakdown in alphabetical order")
    void shouldWriteSpeciesBreakdown() throws IOException {
        // Create animals of different species (Dog, Cat)
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15)),
                new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 10)),
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 20))
        );

        // Create a temp file
        Path output = Files.createTempFile("report-test", ".txt");
        try {
            // Write report to temp file
            writer.writeReport(animals, output);
            // Read content and verify "Cat:" appears before "Dog:" (alphabetical)
            String content = Files.readString(output, StandardCharsets.UTF_8);
            assertThat(content.indexOf("Cat:"))
                    .isLessThan(content.indexOf("Dog:"));

            // Verify vaccinated counts are correct
            assertThat(content).contains("Cat: 1 total, 1 vaccinated");
            assertThat(content).contains("Dog: 2 total, 1 vaccinated");
        }
        finally {
            // Clean up: Files.deleteIfExists(output)
            Files.deleteIfExists(output);
        }
    }

    @Test
    @DisplayName("writes oldest animal per species")
    void shouldWriteOldestPerSpecies() throws IOException {
        // Create animals where Max (age 5) is the oldest Dog
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15)),
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 20)),
                new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 10)),
                new Animal("Bella", "Cat", 1, true, LocalDate.of(2026, 1, 5))
        );

        Path output = Files.createTempFile("report-test", ".txt");
        try {
            // Write report to temp file
            writer.writeReport(animals, output);
            String content = Files.readString(output, StandardCharsets.UTF_8);
            // Read content and verify it contains "Dog: Max (age 5)"
            assertThat(content)
                    .contains("Dog: Max (age 5)")
                    .contains("Cat: Luna (age 2)");

        } finally {
            Files.deleteIfExists(output);
        }
    }
}
