package com.liyuan.springmvc;

import com.luv2code.springmvc.MvcTestingExampleApplication;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Student;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@SpringBootTest(classes = MvcTestingExampleApplication.class)
@AutoConfigureMockMvc
public class GradBookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private StudentAndGradeService studentAndGradeServiceMock;

    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("insert into student(id, firstname, lastname, email_address) values (1,'eric','roby','eric.roby@gmail.com')");
    }

    @AfterEach
    public void teardown() {
        // jdbcTemplate.execute("delete from student where id = 1");
        jdbcTemplate.execute("delete from student");
    }

    @Test
    public void MockingServiceToTestMvc() {
        CollegeStudent studentOne = new CollegeStudent("Eric", "Roby", "eric.roby@gmail.com");
        CollegeStudent studentTwo = new CollegeStudent("Chad", "Darby", "chad.darby@gmail.com");
        List<CollegeStudent> students = new ArrayList<>(Arrays.asList(studentOne, studentTwo));
        when(studentAndGradeServiceMock.getGradeBook()).thenReturn(students);
        assertIterableEquals(students, studentAndGradeServiceMock.getGradeBook());
    }

    @Test
    public void getStudentHttpRequestTest() throws Exception {
        CollegeStudent studentOne = new CollegeStudent("Eric", "Roby", "eric.roby@gmail.com");
        CollegeStudent studentTwo = new CollegeStudent("Chad", "Darby", "chad.darby@gmail.com");
        List<CollegeStudent> students = new ArrayList<>(Arrays.asList(studentOne, studentTwo));
        when(studentAndGradeServiceMock.getGradeBook()).thenReturn(students);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView,"index");
    }
}
