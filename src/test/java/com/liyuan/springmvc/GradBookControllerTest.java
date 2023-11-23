package com.liyuan.springmvc;

import com.luv2code.springmvc.MvcTestingExampleApplication;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Student;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@SpringBootTest(classes = MvcTestingExampleApplication.class)
@AutoConfigureMockMvc
public class GradBookControllerTest {
    private static MockHttpServletRequest mockHttpServletRequest;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private StudentAndGradeService studentAndGradeServiceMock;

    @Autowired
    private StudentDao studentDao;

    @BeforeAll
    public static void setup() {
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addParameter("firstname", "Chad");
        mockHttpServletRequest.setParameter("lastname","Darby");
        mockHttpServletRequest.setParameter("emailAddress", "chad.darby@gmail.com");
    }

    @BeforeEach
    public void beforeEach() {
        jdbcTemplate.execute("insert into student(id, firstname, lastname, email_address) values (1,'eric','roby','eric.roby@gmail.com')");
    }

    @AfterEach
    public void afterEach() {
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
        verify(studentAndGradeServiceMock, times(1)).getGradeBook();
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

    @Test
    public void createStudentHttpRequest() throws Exception {
        CollegeStudent student = new CollegeStudent("Chad", "Darby", "chad.darby@gmail.com");
        List<CollegeStudent> students = List.of(student);
        MvcResult mvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname",mockHttpServletRequest.getParameterValues("firstname"))
                .param("lastname", mockHttpServletRequest.getParameterValues("lastname"))
                .param("emailAddress", mockHttpServletRequest.getParameterValues("emailAddress"))
        ).andExpect(status().isOk()).andReturn();
        // test studentAndGradeService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
        // with AutoWire real instance
        CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad.darby@gmail.com");
        assertNotNull(verifyStudent, "Student should not null");

        // test Iterable<CollegeStudent> newCollegeStudents = studentAndGradeService.getGradeBook(); in controller
        // with mock is good enough
        when(studentAndGradeServiceMock.getGradeBook()).thenReturn(students);
        assertIterableEquals(students, studentAndGradeServiceMock.getGradeBook());
        verify(studentAndGradeServiceMock, times(1)).getGradeBook();


        ModelAndView modelAndView = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView,"index");
    }
}
