import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import service.Service;
import domain.Student;
import repository.StudentXMLRepo;
import org.junit.jupiter.api.BeforeAll;
import validation.StudentValidator;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

@Tag("ExamplePack")
class StudentTests {
    private static StudentXMLRepo studentRepo;
    private static StudentValidator studentValidator;
    private static Service service;
    private static String xmlFile;

    @BeforeAll
    static void setUp() {
        xmlFile = "src/test/resources/students_test_" + System.currentTimeMillis() + ".xml";
        // Create the directory if it doesn't exist
        new File("src/test/resources").mkdirs();

        studentRepo = new StudentXMLRepo(xmlFile);
        studentValidator = new StudentValidator();
        service = new Service(studentRepo, studentValidator, null, null, null, null);
    }

    @Test
    void testAddValidStudent() {
        Student student = new Student("1", "John", 935, "john@example.com");
        Student result = service.addStudent(student);
        assertNull(result);
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
    void test_AddDuplicateStudent() {
        Student student = new Student("1", "Alex", 0, "alex@gmail.com");
        service.addStudent(student);
        Student s = service.addStudent(student);
        assert (student == s);
    }
}