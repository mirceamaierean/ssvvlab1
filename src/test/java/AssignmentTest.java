import domain.Tema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.TemaXMLRepo;
import service.Service;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {
    private static TemaXMLRepo temaRepo;
    private static TemaValidator temaValidator;
    private static Service service;
    private static String xmlFile;

    @BeforeAll
    static void setUp() {
        xmlFile = "src/test/resources/assignments_test_" + System.currentTimeMillis() + ".xml";

        // Create the directory if it doesn't exist
        new File("src/test/resources").mkdirs();

        temaRepo = new TemaXMLRepo(xmlFile);
        temaValidator = new TemaValidator();
        service = new Service(null, null, temaRepo, temaValidator, null, null);
    }

    @Test
    void testAddValidAssignment() {
        // Test adding a valid assignment - covers the successful path through addTema
        Tema tema = new Tema("1", "Test Assignment", 10, 8);
        Tema result = service.addTema(tema);

        assertNull(result); // Should be null when successfully added
    }

    @Test
    void testAddAssignmentWithEmptyDescription() {
        // Test empty description validation - covers the description validation branch
        Tema tema = new Tema("1", "", 10, 8);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Descriere invalida!", exception.getMessage());
    }

    @Test
    void testAddAssignmentWithInvalidDeadline() {
        // Test invalid deadline validation - covers the deadline validation branch
        Tema tema = new Tema("1", "Test Assignment", 15, 8);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Deadlineul trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    void testAddAssignmentWithInvalidStartWeek() {
        // Test invalid start week validation - covers the start week validation branch
        Tema tema = new Tema("1", "Test Assignment", 10, 0);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", exception.getMessage());
    }
}