package com.liyuan.springmvc;

import com.luv2code.springmvc.MvcTestingExampleApplication;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
@TestPropertySource("/application.properties")
public class StudentAndGradeStudentTest {

    @Autowired
    StudentDao studentDao;

    @Autowired
    StudentAndGradeService studentService;

    @Test
    public void createStudentService() {
        studentService.createStudent("Chad", "Darby","chad.daby@gmail.com");
        CollegeStudent student = studentDao.findByEmailAddress("chad.daby@gmail.com");
        assertEquals("chad.daby@gmail.com", student.getEmailAddress(),"find by email");
    }
}
