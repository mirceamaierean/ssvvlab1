import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import service.Service;
import domain.Student;
import repository.StudentXMLRepo;
import validation.StudentValidator;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ExamplePack")
class ExampleTest {

    private StudentXMLRepo studentRepo = new StudentXMLRepo("a.txt");
    private StudentValidator studentValidator = new StudentValidator();
    private Service service = new Service(studentRepo, studentValidator, null, null, null, null);

    @Test
    void testAddValidStudent() {
        Student student = new Student("1", "John Doe", 935, "john@example.com");
        Student result = service.addStudent(student);
        assertNotNull(result, "Added student should not be null");
        assertEquals("1", result.getID(), "Student ID should match");
        assertEquals("John Doe", result.getNume(), "Student name should match");
        assertEquals(935, result.getGrupa(), "Student group should match");
        assertEquals("john@example.com", result.getEmail(), "Student email should match");
    }

    @Test
    void testAddInvalidStudent() {
        Student invalidStudent = new Student("", "", -1, "invalid-email");

        assertThrows(ValidationException.class, () -> {
            service.addStudent(invalidStudent);
        }, "Adding invalid student should throw ValidationException");
    }

    @Test
    void exampleTestCase() {
        System.out.println("Running an example test...");
        assertTrue(true, "Example assertion passed");
    }
}
