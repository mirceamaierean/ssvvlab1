import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import service.Service;
import domain.Student;
import repository.StudentXMLRepo;
import validation.StudentValidator;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ExamplePack")
class StudentTests {

    private StudentXMLRepo studentRepo = new StudentXMLRepo("a.txt");
    private StudentValidator studentValidator = new StudentValidator();
    private Service service = new Service(studentRepo, studentValidator, null, null, null, null);

    @Test
    void testAddValidStudent() {
        Student student = new Student("1", "John", 935, "john@example.com");
        Student result = service.addStudent(student);
        assertNotNull(result, "Added student should not be null");
        assertEquals("1", result.getID(), "Student ID should match");
        assertEquals("John", result.getNume(), "Student name should match");
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
    void testAddNullStudent() {
        Student student = null;
        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void testNullId() {
        Student student = new Student(null, "Alex", 933, "alex@gmail.com");
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Id incorect!", ex.getMessage());
    }

    @Test
    void testEmptyId_BVA() {
        Student student = new Student("", "Alex", 933, "alex@gmail.com");
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Id incorect!", ex.getMessage());
    }

    @Test
    void testNullName_EC() {
        Student student = new Student("1", null, 933, "alex@gmail.com");
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Nume incorect!", ex.getMessage());
    }

    @Test
    void testEmptyName_BVA() {
        Student student = new Student("1", "", 933, "alex@gmail.com");
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Nume incorect!", ex.getMessage());
    }

    @Test
    void testNegativeGroup_EC_BVA() {
        Student student = new Student("1", "Alex", -1, "alex@gmail.com");
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Grupa incorecta!", ex.getMessage());
    }

    @Test
    void testGroupBelowBoundary_BVA() {
        Student student = new Student("1", "Alex", -1, "alex@gmail.com");
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Grupa incorecta!", ex.getMessage());
    }

    @Test
    void testGroupAtBoundary_BVA() {
        Student student = new Student("1", "Alex", 0, "alex@gmail.com");
        assertDoesNotThrow(() -> service.addStudent(student));
    }

    @Test
    void testGroupAboveBoundary_BVA() {
        Student student = new Student("1", "Alex", 1, "alex@gmail.com");
        assertDoesNotThrow(() -> service.addStudent(student));
    }

    @Test
    void testNullEmail_EC() {
        Student student = new Student("1", "Alex", 933, null);
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Email incorect!", ex.getMessage());
    }

    @Test
    void testEmptyEmail_BVA() {
        Student student = new Student("1", "Alex", 933, "");
        ValidationException ex = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertEquals("Email incorect!", ex.getMessage());
    }

    @Test
    void testValidStudent_AllValid() {
        Student student = new Student("1", "Alex", 0, "alex@gmail.com");
        assertDoesNotThrow(() -> service.addStudent(student));
    }

    @Test
    void addDuplicateStudent() {
        Student student = new Student("1", "Alex", 0, "alex@gmail.com");
        service.addStudent(student);
        Student s = service.addStudent(student);
        assert (student == s);
    }
}