package com.liyuan.springmvc;

import com.luv2code.springmvc.MvcTestingExampleApplication;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
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
}
