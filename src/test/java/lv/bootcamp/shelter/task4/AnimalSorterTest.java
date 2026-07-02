package lv.bootcamp.shelter.task4;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Task 4: Collection and sorting tests
 *
 * Practice:
 * - AssertJ list assertions (extracting, containsExactly)
 * - Testing sort order
 * - Testing null/empty input handling
 *
 * Instructions:
 * Write tests for AnimalSorter. Use AssertJ's extracting() and containsExactly()
 * to verify the order of results.
 */
@DisplayName("AnimalSorter")
class AnimalSorterTest {

    private AnimalSorter sorter;

    private Animal buddy;
    private Animal luna;
    private Animal max;
    private Animal bella;

    @BeforeEach
    void setUp() {
        sorter = new AnimalSorter();
        buddy = new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15));
        luna = new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 10));
        max = new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 20));
        bella = new Animal("Bella", "Cat", 1, true, LocalDate.of(2026, 1, 5));
    }

    // --- sortByAge ---

    @Test
    @DisplayName("sortByAge: returns animals ordered youngest to oldest")
    void shouldSortByAgeAscending() {
        // Call sorter.sortByAge with [buddy, luna, max, bella]
        List<Animal> result = sorter.sortByAge(List.of(buddy, luna, max, bella));
        // Use assertThat(result).extracting(Animal::getName)
        //       .containsExactly("Bella", "Luna", "Buddy", "Max")
        assertThat(result).extracting(Animal::getName)
               .containsExactly("Bella", "Luna", "Buddy", "Max");
    }

    @Test
    @DisplayName("sortByAge: returns empty list for null input")
    void shouldReturnEmptyForNullInput() {
        //  Call sorter.sortByAge(null)
        List<Animal> result = sorter.sortByAge(null);
        // Assert result is empty
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("sortByAge: returns empty list for empty input")
    void shouldReturnEmptyForEmptyInput() {
        // Call sorter.sortByAge(List.of())
        List<Animal> animals = sorter.sortByAge(List.of());
        // Assert result is empty
        assertThat(animals).isEmpty();
    }

    @Test
    @DisplayName("sortByAge: does not modify the original list")
    void shouldNotModifyOriginalList() {
        // Create a list,
        List<Animal> animals = List.of(buddy, luna, max, bella);
        // sort the list
        sorter.sortByAge(animals);
        // verify the original list order is unchanged
        assertThat(animals).extracting(Animal::getName)
                .containsExactly("Buddy", "Luna", "Max", "Bella");
    }

    // --- sortByName ---

    @Test
    @DisplayName("sortByName: returns animals in alphabetical order")
    void shouldSortByNameAlphabetically() {
        // Call sorter.sortByName with [buddy, luna, max, bella]
        List<Animal> result = sorter.sortByName(List.of(buddy, luna, max, bella));
        // Verify order is Bella, Buddy, Luna, Max
        assertThat(result).extracting(Animal::getName)
                .containsExactly("Bella", "Buddy", "Luna", "Max");
    }

    @Test
    @DisplayName("sortByName: is case-insensitive")
    void shouldSortNamesCaseInsensitively() {
        // Create animals with mixed case names (e.g., "zebra", "Alpha")
        Animal zebra = new Animal("zebra", "Dog", 3, true, LocalDate.of(2026, 1, 15));
        Animal alpha = new Animal("Alpha", "Cat", 2, true, LocalDate.of(2026, 1, 10));
        Animal neon = new Animal("neON", "Dog", 5, false, LocalDate.of(2026, 1, 20));


        List<Animal> result = sorter.sortByName(List.of(zebra, alpha, neon));

        // Verify alphabetical order ignores case
        assertThat(result).extracting(Animal::getName)
                .containsExactly("Alpha", "neON", "zebra");
    }

    // --- sortByIntakeDate ---

    @Test
    @DisplayName("sortByIntakeDate: returns animals from earliest to latest")
    void shouldSortByIntakeDateAscending() {
        // Call sorter.sortByIntakeDate with [buddy, luna, max, bella]
        List<Animal> result = sorter.sortByIntakeDate(List.of(buddy, luna, max, bella));

        // Verify order by date: bella (Jan 5), luna (Jan 10), buddy (Jan 15), max (Jan 20)
        assertThat(result).extracting(Animal::getName)
                .containsExactly("Bella", "Luna", "Buddy", "Max");
    }

    // --- sortBySpeciesThenAgeDescending ---

    @Test
    @DisplayName("sortBySpeciesThenAgeDescending: groups by species then orders by age desc")
    void shouldSortBySpeciesThenAgeDesc() {
        // Call sorter.sortBySpeciesThenAgeDescending with [buddy, luna, max, bella]
        List<Animal> result = sorter.sortBySpeciesThenAgeDescending(List.of(buddy, luna, max, bella));
        // Expected order: Cat group (Luna age 2, Bella age 1), Dog group (Max age 5, Buddy age 3)
        // Verify with extracting(Animal::getName).containsExactly(...)
        assertThat(result).extracting(Animal::getName)
                .containsExactly("Luna", "Bella", "Max", "Buddy");
    }
}
