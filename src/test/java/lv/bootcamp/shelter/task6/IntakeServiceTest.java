package lv.bootcamp.shelter.task6;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Task 7: Mocking a dependency
 *
 * Practice:
 * - @Mock and @InjectMocks
 * - when(...).thenReturn(...)
 * - verify(...) and verify(..., never())
 * - Testing with mocked dependencies
 *
 * Instructions:
 * Write tests for IntakeService. The AnimalRepository is mocked — you control what it returns.
 * Focus on verifying that IntakeService calls the repository correctly.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("IntakeService")
class IntakeServiceTest {

    @Mock
    private AnimalRepository repository;

    @InjectMocks
    private IntakeService service;

    private final Animal buddy = new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15));

    // ==================== intake() ====================

    @Nested
    @DisplayName("intake")
    class Intake {

        @Test
        @DisplayName("saves valid animal and returns it")
        void shouldSaveValidAnimal() {
            // Stub repository.save(buddy) to return buddy
            when(repository.save(buddy)).thenReturn(buddy);
            // Call service.intake(buddy)
            Animal result = service.intake(buddy);
            // Assert the returned animal has name "Buddy"
            assertThat(result.getName()).isEqualTo("Buddy");
            // Verify that repository.save(buddy) was called exactly once
            verify(repository).save(buddy);
        }

        @Test
        @DisplayName("throws for null animal without calling repository")
        void shouldThrowForNullAnimal() {
            // Call service.intake(null)
            // Assert it throws NullPointerException
            assertThrows(
                    NullPointerException.class,
                    () -> service.intake(null));

            // Verify that repository.save(any()) was NEVER called
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("throws for invalid animal without calling repository")
        void shouldThrowForInvalidAnimal() {
            // Create an Animal with blank name
            Animal invalid = new Animal("", "Dog", 3, true, LocalDate.of(2026, 1, 15));
            // Assert that service.intake(invalid) throws IllegalArgumentException
            assertThrows(IllegalArgumentException.class, () -> service.intake(invalid));
            // Verify that repository.save(any()) was NEVER called
            verify(repository, never()).save(any());
        }
    }

    // ==================== findByName() ====================

    @Nested
    @DisplayName("findByName")
    class FindByName {

        @Test
        @DisplayName("returns animal when repository finds it")
        void shouldReturnAnimalWhenFound() {
            // Stub repository.findByName("Buddy") to return Optional.of(buddy)
            when(repository.findByName("Buddy"))
                    .thenReturn(Optional.of(buddy));
            // Call service.findByName("Buddy")
            Animal result = service.findByName("Buddy");
            // Assert result is not null and name equals "Buddy"
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Buddy");

            verify(repository).findByName("Buddy");
        }

        @Test
        @DisplayName("returns null when repository does not find it")
        void shouldReturnNullWhenNotFound() {
            // Stub repository.findByName("Ghost") to return Optional.empty()
            when(repository.findByName("Ghost"))
                    .thenReturn(Optional.empty());

            // Call service.findByName("Ghost")
            Animal result = service.findByName("Ghost");

            // Assert result is null
            assertThat(result).isNull();

            verify(repository).findByName("Ghost");
        }

        @Test
        @DisplayName("throws for blank name without calling repository")
        void shouldThrowForBlankName() {
            // Call service.findByName("")
            // Assert it throws IllegalArgumentException
            assertThrows(
                    IllegalArgumentException.class,
                    () -> service.findByName(""));

            verify(repository, never()).findByName(any());
            // Verify repository.findByName(any()) was NEVER called
        }
    }

    // ==================== findBySpecies() ====================

    @Nested
    @DisplayName("findBySpecies")
    class FindBySpecies {

        @Test
        @DisplayName("returns list from repository for valid species")
        void shouldReturnAnimalsForValidSpecies() {
            // Stub repository.findBySpecies("Dog") to return List.of(buddy)
            when(repository.findBySpecies("Dog"))
                    .thenReturn(List.of(buddy));

            // Call service.findBySpecies("Dog")
            List<Animal> result = service.findBySpecies("Dog");

            // Assert result has size 1 and contains buddy
            assertThat(result)
                    .hasSize(1)
                    .containsExactly(buddy);

            verify(repository).findBySpecies("Dog");
        }

        @Test
        @DisplayName("returns empty list for blank species without calling repository")
        void shouldReturnEmptyForBlankSpecies() {
            // Call service.findBySpecies("")
            List<Animal> result = service.findBySpecies("");
            // Assert result is empty
            assertThat(result).isEmpty();
            // Verify repository.findBySpecies(any()) was NEVER called
            verify(repository, never()).findBySpecies(any());
        }

        @Test
        @DisplayName("returns empty list for null species without calling repository")
        void shouldReturnEmptyForNullSpecies() {
            // Call service.findBySpecies(null)
            List<Animal> result = service.findBySpecies(null);
            // Assert result is empty
            assertThat(result).isEmpty();
            // Verify repository.findBySpecies(any()) was NEVER called
            verify(repository, never()).findBySpecies(any());
        }
    }

    // ==================== count() ====================

    @Nested
    @DisplayName("count")
    class Count {

        @Test
        @DisplayName("returns the size of all animals from repository")
        void shouldReturnCountFromRepository() {
            // Stub repository.findAll() to return a list of 3 animals
            when(repository.findAll())
                    .thenReturn(List.of(
                            buddy,
                            new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 15)),
                            new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 15))
                    ));

            // Call service.count()
            int count = service.count();
            // Assert result equals 3
            assertThat(count).isEqualTo(3);

            verify(repository).findAll();
        }

        @Test
        @DisplayName("returns 0 when repository is empty")
        void shouldReturnZeroWhenEmpty() {
            // Stub repository.findAll() to return List.of()
            when(repository.findAll()).thenReturn(List.of());
            // Call service.count()
            int count = service.count();
            // Assert result equals 0
            assertThat(count).isZero();

            verify(repository).findAll();
        }
    }
}
