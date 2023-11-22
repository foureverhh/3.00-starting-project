package com.liyuan.springmvc;

import com.luv2code.springmvc.MvcTestingExampleApplication;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
@TestPropertySource("/application.properties")
public class StudentAndGradeStudentTest {

    @Autowired
    StudentDao studentDao;

    @Autowired
    StudentAndGradeService studentService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    // set up sample date
    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("insert into student(id, firstname, lastname, email_address) values (1,'eric','roby','eric.roby@gmail.com')");
    }

    @AfterEach
    public void teardown() {
        // jdbcTemplate.execute("delete from student where id = 1");
        jdbcTemplate.execute("delete from student where id = 1");
    }
    @Test
    public void createStudentService() {
        studentService.createStudent("Chad", "Darby","chad.daby@gmail.com");
        CollegeStudent student = studentDao.findByEmailAddress("chad.daby@gmail.com");
        assertEquals("chad.daby@gmail.com", student.getEmailAddress(),"find by email");
    }

    @Test
    public void isStudentAvailable() {
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentByIdTest() {
        assertTrue(studentDao.findById(1).isPresent());
        studentService.deleteStudentById(1);
        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    public void getGradeBookService() {
        Iterable<CollegeStudent> collegeStudentIterable = studentService.getGradeBook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for (CollegeStudent collegeStudent : collegeStudentIterable) {
            collegeStudents.add(collegeStudent);
            System.out.println(collegeStudent.getId() + " " + collegeStudent.getEmailAddress());
        }
        assertEquals(1, collegeStudents.size());
    }

     // get sql from /main/resources
    @Test
    @Sql("/insertData.sql")
    public void sqlInsertion() {
        Iterable<CollegeStudent> collegeStudentIterable = studentService.getGradeBook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for (CollegeStudent collegeStudent : collegeStudentIterable) {
            collegeStudents.add(collegeStudent);
        }
        //1 from setup, the other 5 from insertData.sql
        assertEquals(6, collegeStudents.size());
    }
}
